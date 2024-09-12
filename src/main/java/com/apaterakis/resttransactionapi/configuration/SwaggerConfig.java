package com.apaterakis.resttransactionapi.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Rest Transactions API")
                        .version("1.0")
                        .description("API documentation for RestTransactionsAPI project")
                        .contact(new Contact().name("Andreas Paterakis").email("andreas_pat@hotmail.gr").url("https://github.com/Andreaspat94")));
    }

}
