package easytime.srv.api;

//import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SrvApiApplication {

	public static void main(String[] args) {
		//Dotenv dotenv = Dotenv.configure().load();
		SpringApplication.run(SrvApiApplication.class, args);
	}

}
