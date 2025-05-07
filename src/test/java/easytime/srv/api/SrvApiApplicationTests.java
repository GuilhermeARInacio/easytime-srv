package easytime.srv.api;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import java.util.HashSet;

import static org.mockito.Mockito.*;

class SrvApiApplicationTest {

    @Test
    void mainShouldLoadEnvironmentVariablesAndRunApplication() {
        // Mock Dotenv and SpringApplication
        try (MockedStatic<Dotenv> dotenvMock = mockStatic(Dotenv.class);
             MockedStatic<SpringApplication> springApplicationMock = mockStatic(SpringApplication.class)) {

            // Mock DotenvBuilder and Dotenv behavior
            DotenvBuilder dotenvBuilder = mock(DotenvBuilder.class);
            Dotenv dotenv = mock(Dotenv.class);
            when(dotenv.entries()).thenReturn(new HashSet<>());
            when(dotenvBuilder.load()).thenReturn(dotenv);
            dotenvMock.when(Dotenv::configure).thenReturn(dotenvBuilder);

            // Call the main method
            SrvApiApplication.main(new String[]{});

        }
    }
}