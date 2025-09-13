# Swagger Padlock Color 🎨

A fully working **example project** demonstrating how to color-code padlock icons in Swagger UI for a Java Spring Boot application.

This approach combines backend OpenAPI specification customization with frontend JavaScript injection to control the color of each secured endpoint’s padlock, allowing visual categorization by permission level or resource type.

![Swagger Padlock Color Example](images/screenshot.png)

---

## Prerequisites

This project has been tested and works with the following versions:

- **Java 17**
- **Spring Boot 3.5.x**
- **Springdoc OpenAPI 2.8.x**

Using earlier versions of Java, Spring Boot, or Springdoc OpenAPI may require modifications to the code or configuration.

---

## How It Works

The solution consists of two main parts: a **backend** that enriches the OpenAPI specification with color metadata, and a **frontend script** that applies these colors in Swagger UI.

---

### Backend: Adding Color Metadata and Injecting the Script

1. **Define Security Schemes:**  
   In the `OpenAPI` bean, the **real security schemes** used by your API are defined (e.g., `bearerAuth`, `basicAuth`). These ensure Swagger UI displays a padlock icon for secured endpoints.

2. **Customize OpenAPI with `OpenApiCustomizer`:**  
   This bean iterates through all API paths. For each secured endpoint:
   - Assigns the proper `SecurityRequirement` corresponding to the real security scheme.
   - Adds a custom extension `x-padlock-color` with a color value for that endpoint.
     The color must be **CSS-compatible**, for example:
       - Hex: `#FF0000`
       - RGB: `rgb(255,0,0)`
       - Named color: `red`

   The resulting OpenAPI JSON contains both the **real authentication requirement** and the **color metadata**.

3. **Inject the Frontend Script:**  
   The `SwaggerPadlockTransformer` is a **Spring bean** that intercepts the Swagger UI `index.html` before it is sent to the browser. It injects a `<script>` tag pointing to `/script.js`.

---

### Frontend: Applying Colors in the Browser

The client-side script (`/resources/static/script.js`) runs in the browser once Swagger UI has rendered. For each API operation:

1. Reads the `x-padlock-color` extension from the OpenAPI JSON.
2. Finds the corresponding padlock SVG in the Swagger UI DOM and applies the color via CSS `fill`.
3. Uses a `MutationObserver` with a debounce timer to handle dynamic UI changes (e.g., filtering endpoints) and recolor padlocks automatically.

**Notes:**

- The script relies on Swagger UI’s DOM structure (`.opblock`, `svg.locked/unlocked`). Changes to Swagger UI may require updates to the script.
- Only operations with `x-padlock-color` defined will be colored.
- Uses JSDoc typedefs (`Operation`, `PathItem`, `Spec`) for clarity and editor autocomplete.

---

### ⚠️ Warning: Browser Cache

If colored padlocks do not appear:

- Your browser may be caching old Swagger UI files.
- **Recommended steps:**
   1. Open Swagger UI in a **private/incognito window**.
   2. Clear your browser cache or force reload (`Ctrl + Shift + R`).
   3. Ensure your backend is running and serving the latest `script.js`.

> Cached `index.html` or `script.js` may prevent the color script from working.

---

## How To Use

1. **Clone the Repository**
    ```bash
    git clone https://github.com/paul-lartigau/swagger-padlock-color.git
    cd swagger-padlock-color
    ```

2. **Build the Project**
   ```
   ./mvnw clean install
   ```

3. **Run the Spring Boot Application**
   ```
   ./mvnw spring-boot:run
   ```

4. **Open Swagger UI**
   ```
   http://localhost:8080/swagger-ui/index.html
   ```

5. **See Colored Padlocks**  
 Padlock icons for secured endpoints will appear in the colors defined via `x-padlock-color`.

---