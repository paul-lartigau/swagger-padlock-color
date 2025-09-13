package com.example.configuration;

import com.example.configuration.transformer.SwaggerPadlockTransformer;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webmvc.ui.SwaggerIndexTransformer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.constants.ActuatorEndpoints.*;


@Configuration
public class SwaggerConfiguration {

    private static final String BEARER = "bearer";
    private static final String JWT = "JWT";

    private static final String X_PADLOCK_COLOR = "x-padlock-color";

    private static final String RED_PADLOCK = "Red Padlock";
    private static final String BLUE_PADLOCK = "Blue Padlock";
    private static final String GREEN_PADLOCK = "Green Padlock";
    private static final String ORANGE_PADLOCK = "Orange Padlock";
    private static final String DEFAULT_PADLOCK = "Default Padlock";

    private static final String RED = "red";
    private static final String BLUE = "blue";
    private static final String GREEN = "rgb(0,255,0)";
    private static final String ORANGE = "#FA900F";

    @Bean
    public SwaggerIndexTransformer swaggerIndexTransformer(SwaggerUiConfigProperties swaggerUiConfig, SwaggerUiOAuthProperties swaggerUiOAuthProperties,
                                                           SwaggerWelcomeCommon swaggerWelcomeCommon, ObjectMapperProvider objectMapperProvider) {
        return new SwaggerPadlockTransformer(swaggerUiConfig, swaggerUiOAuthProperties, swaggerWelcomeCommon, objectMapperProvider);
    }

    @Bean
    public OpenAPI swaggerAuthorizationAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(RED_PADLOCK, toSecurityScheme())
                        .addSecuritySchemes(BLUE_PADLOCK, toSecurityScheme())
                        .addSecuritySchemes(GREEN_PADLOCK, toSecurityScheme())
                        .addSecuritySchemes(ORANGE_PADLOCK, toSecurityScheme())
                        .addSecuritySchemes(DEFAULT_PADLOCK, toSecurityScheme()));
    }

    /**
     * Creates a {@code SecurityScheme} using the desired authentication type.
     */
    private static SecurityScheme toSecurityScheme() {
        return new SecurityScheme()
                .scheme(BEARER)
                .bearerFormat(JWT)
                .type(SecurityScheme.Type.HTTP);
    }

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        SecurityRequirement redPadlockRequirement = new SecurityRequirement()
                .addList(RED_PADLOCK);
        SecurityRequirement bluePadlockRequirement = new SecurityRequirement()
                .addList(BLUE_PADLOCK);
        SecurityRequirement greenPadlockRequirement = new SecurityRequirement()
                .addList(GREEN_PADLOCK);
        SecurityRequirement orangePadlockRequirement = new SecurityRequirement()
                .addList(ORANGE_PADLOCK);
        SecurityRequirement defaultPadlockRequirement = new SecurityRequirement()
                .addList(DEFAULT_PADLOCK);

        return openApi -> openApi.getPaths().forEach((path, pathItem) -> {
            if (ACTUATOR_HEALTH.equals(path))
                pathItem.readOperations()
                        .forEach(operation -> operation.addSecurityItem(redPadlockRequirement)
                                .addExtension(X_PADLOCK_COLOR, RED));
            else if (ACTUATOR_HEAP_DUMP.equals(path))
                pathItem.readOperations()
                        .forEach(operation -> operation.addSecurityItem(bluePadlockRequirement)
                                .addExtension(X_PADLOCK_COLOR, BLUE));
            else if (ACTUATOR_INFO.equals(path))
                pathItem.readOperations()
                        .forEach(operation -> operation.addSecurityItem(greenPadlockRequirement)
                                .addExtension(X_PADLOCK_COLOR, GREEN));
            else if (ACTUATOR_SCHEDULED_TASKS.equals(path))
                pathItem.readOperations()
                        .forEach(operation -> operation.addSecurityItem(orangePadlockRequirement)
                                .addExtension(X_PADLOCK_COLOR, ORANGE));
            else if (ACTUATOR_THREAD_DUMP.equals(path))
                pathItem.readOperations()
                        .forEach(operation -> operation.addSecurityItem(defaultPadlockRequirement)); //default black color
            //else no padlock
        });
    }
}
