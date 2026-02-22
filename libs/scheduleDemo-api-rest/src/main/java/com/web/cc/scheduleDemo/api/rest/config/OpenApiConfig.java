package com.web.cc.scheduleDemo.api.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;

@Configuration
public class OpenApiConfig {
	
	@Bean
	OpenAPI initOpenAPI() {
	    String securitySchemeName = "Authorization";
	    return new OpenAPI()
	        .info(new Info()
	            .title("排程系統")
	            .description("排程系統")
	            .version("v1.0"))
	        .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
	        .components(new Components()
        		.addSecuritySchemes(securitySchemeName,
                        new SecurityScheme()
                            .name("Authorization")         
                            .type(SecurityScheme.Type.APIKEY) 
                            .in(In.HEADER))); 
	}
}