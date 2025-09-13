package com.example.configuration.transformer;

import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webmvc.ui.SwaggerIndexPageTransformer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.resource.ResourceTransformerChain;
import org.springframework.web.servlet.resource.TransformedResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SwaggerPadlockTransformer extends SwaggerIndexPageTransformer {

    private static final String SWAGGER_INDEX_FILE = "index.html";

    public SwaggerPadlockTransformer(SwaggerUiConfigProperties swaggerUiConfig, SwaggerUiOAuthProperties swaggerUiOAuthProperties,
                                     SwaggerWelcomeCommon swaggerWelcomeCommon, ObjectMapperProvider objectMapperProvider) {
        super(swaggerUiConfig, swaggerUiOAuthProperties, swaggerWelcomeCommon, objectMapperProvider);
    }

    @Override
    public @NonNull Resource transform(HttpServletRequest request, Resource resource, ResourceTransformerChain transformer) throws IOException {
        if (SWAGGER_INDEX_FILE.equals(resource.getFilename())) {
            try (InputStream inputStream = resource.getInputStream()) {
                return new TransformedResource(resource, new String(inputStream.readAllBytes(), StandardCharsets.UTF_8)
                        .replace("</body>", "  <script src=\"/script.js\" charset=\"UTF-8\"> </script>\n  </body>")
                        .getBytes(StandardCharsets.UTF_8));
            }
        } else
            return super.transform(request, resource, transformer);
    }
}