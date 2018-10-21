package org.bitvault.appstore.cloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {             
	
	public static final String SWAGGER_API_VERSION = "1.0.0";
	public static final String LICENSE = "License";
	public static final String LICENSE_URL = "http://www.vvdntech.com/license.html";
	public static final String TITLE = "Bitvault Appstore";
	public static final String DESCRIPTION = "Bitvault Appstore APIs";
	public static final String TERMS_OF_SERVICE_URL = "http://www.vvdntech.com/termsofuses.html";
	public static final Contact contact = new Contact("VVDN Technologies", "http://www.vvdntech.com", "support@vvdntech.in");
	
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)
//        		.host("http://localhost:8083")
          .pathMapping("/")
          .select()                                  
          .apis(RequestHandlerSelectors.basePackage("org.bitvault"))              
          .paths(PathSelectors.any())                          
          .build()
          .apiInfo(apiInfo());                                           
    }
    
    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(TITLE,DESCRIPTION,SWAGGER_API_VERSION,TERMS_OF_SERVICE_URL,contact,LICENSE,LICENSE_URL);
        return apiInfo;
    }
    
    private ApiKey apiKey() {
        return new ApiKey("mykey", "X-Authorization", "header");
      }
    
    @Bean
    SecurityConfiguration security() {
      return new SecurityConfiguration(
          null,
          null,
          null,
          null,
          "Bearer access_token",
          ApiKeyVehicle.HEADER, 
          "X-Authorization", 
          "," /*scope separator*/);
    }
}
