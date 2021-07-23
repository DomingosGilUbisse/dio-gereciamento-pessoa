package domingos.co.mz.diop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@SpringBootApplication
public class DioGereciamentoPessoaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DioGereciamentoPessoaApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("")
			     .version("1.0")
			     .termsOfService("http://swaggwe.io/terms")
				.license(new License().name("Apache 2.0").url("http://springdoc.org")));
	}
}
