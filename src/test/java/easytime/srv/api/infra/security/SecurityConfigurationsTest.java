// src/test/java/easytime/srv/api/infra/security/SecurityConfigurationsTest.java
package easytime.srv.api.infra.security;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityConfigurationsTest {

    @Test
    void passwordEncoderBean_returnsBCryptPasswordEncoder() {
        SecurityConfigurations config = new SecurityConfigurations();
        PasswordEncoder encoder = config.passwordEncoder();
        assertNotNull(encoder);
        assertTrue(encoder instanceof org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder);
    }

    @Test
    void authenticationManagerBean_returnsAuthenticationManager() throws Exception {
        AuthenticationConfiguration configuration = mock(AuthenticationConfiguration.class);
        AuthenticationManager manager = mock(AuthenticationManager.class);
        when(configuration.getAuthenticationManager()).thenReturn(manager);

        SecurityConfigurations config = new SecurityConfigurations();
        AuthenticationManager result = config.authenticationManager(configuration);

        assertNotNull(result);
        assertEquals(manager, result);
    }

    @Test
    void securityFilterChainBean_configuresSecurity() throws Exception {
        SecurityFilter securityFilter = mock(SecurityFilter.class);
        SecurityConfigurations config = new SecurityConfigurations();
        // Inject the mock filter
        var field = SecurityConfigurations.class.getDeclaredField("securityFilter");
        field.setAccessible(true);
        field.set(config, securityFilter);

        // Use a mock HttpSecurity
        var http = mock(org.springframework.security.config.annotation.web.builders.HttpSecurity.class, RETURNS_DEEP_STUBS);
        when(http.csrf(any())).thenReturn(http);
        when(http.sessionManagement(any())).thenReturn(http);
        when(http.authorizeHttpRequests(any())).thenReturn(http);
        when(http.addFilterBefore(any(), any())).thenReturn(http);
        //when(http.build()).thenReturn(mock(SecurityFilterChain.class)); // <-- fix here

        SecurityFilterChain chain = config.securityFilterChain(http);
        assertNotNull(chain);
        verify(http).addFilterBefore(securityFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
    }
}