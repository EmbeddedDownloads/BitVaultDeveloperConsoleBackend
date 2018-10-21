package org.bitvault.appstore.cloud.main;

import java.util.Map;

import org.apache.catalina.connector.Connector;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.bitvault.appstore.cloud.config.JpaConfiguration;
import org.bitvault.appstore.cloud.config.WebSecurityConfig;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.exception.BitVaultResponse;
import org.bitvault.appstore.cloud.utils.AuditorAwareImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Import({ JpaConfiguration.class, WebSecurityConfig.class })
@SpringBootApplication(scanBasePackages = { "org.bitvault" })
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAutoConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableJpaRepositories(basePackages = "org.bitvault.appstore.commons.application.dao")
@EnableElasticsearchRepositories(basePackages = "org.bitvault.appstore.commons.application.elasticdao")
public class BitvaultMain extends WebMvcConfigurerAdapter {

	public static void main(String... args) {
		SpringApplication.run(BitvaultMain.class, args);
	}

	@Bean
	public AuditorAware<String> auditorAware() {
		return new AuditorAwareImpl();
	}
	// @Override
	// public void addFormatters(FormatterRegistry registry) {
	// registry.addConverter(new RoleIdToRoleConverter());
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
	 * #addViewControllers(org.springframework.web.servlet.config.annotation.
	 * ViewControllerRegistry)
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:/index.html");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
		super.addViewControllers(registry);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*");
	}

	/**
	 * Override this method to controller the error/exception response body Provides
	 * access to error attributes which can be logged or presented to the user.
	 * 
	 * @return ErrorAttributes
	 */
	@Bean
	public ErrorAttributes errorAttributes() {
		return new DefaultErrorAttributes() {

			@Override
			public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes,
					boolean includeStackTrace) {
				Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
				errorAttributes.remove("exception");
				errorAttributes.remove("path");
				errorAttributes.remove("timestamp");
				errorAttributes.remove("error");
				errorAttributes.remove("message");
				Throwable error = getError(requestAttributes);
				if (error instanceof Exception) {
					errorAttributes.put("result", new BitVaultResponse(error.getMessage()));
					errorAttributes.put("status", Constants.FAILED);
				} else if (errorAttributes.get("status").toString().equals("404")) {
					errorAttributes.put("result", new BitVaultResponse("Page not found"));
					errorAttributes.put("status", Constants.FAILED);
				} else {
					errorAttributes.put("result", new BitVaultResponse("Error encountered"));
					errorAttributes.put("status", Constants.FAILED);
				}
				return errorAttributes;
			}

		};
	}

	@Bean
	public TomcatEmbeddedServletContainerFactory tomcatFactory() {
		TomcatEmbeddedServletContainerFactory tomcatFactory = new TomcatEmbeddedServletContainerFactory();

		tomcatFactory.addContextCustomizers((context) -> {

			StandardRoot standardRoot = new StandardRoot(context);
			standardRoot.setCacheMaxSize(20 * 1024);

			context.setResources(standardRoot);
		});

		tomcatFactory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
			@Override
			public void customize(Connector connector) {
				((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(-1);
			}
		});

		// tomcatFactory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
		// @Override
		// public void customize(Connector connector) {
		// ((AbstractProtocol)
		// connector.getProtocolHandler()).setConnectionTimeout(1000);
		// }
		// });
		return tomcatFactory;
	}
}
