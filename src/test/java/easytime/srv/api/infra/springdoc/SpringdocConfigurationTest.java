package easytime.srv.api.infra.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class SpringdocConfigurationTest {

    @Test
    void shouldCreateCustomOpenAPIBean() {
        // Arrange
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringdocConfiguration.class);

        // Act
        OpenAPI openAPI = context.getBean(OpenAPI.class);

        // Assert
        assertNotNull(openAPI);
        Components components = openAPI.getComponents();
        assertNotNull(components);
        SecurityScheme securityScheme = components.getSecuritySchemes().get("bearer-key");
        assertNotNull(securityScheme);
        assertEquals(SecurityScheme.Type.HTTP, securityScheme.getType());
        assertEquals("bearer", securityScheme.getScheme());
        assertEquals("JWT", securityScheme.getBearerFormat());
    }
}