package org.bitvault.appstore.cloud.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = { "classpath:applications-${spring.profiles.active}.properties" })
public class PropertiesConfig {
	
	@Value("${server.host}")
	private String host;

	public String getHost() {
		return host;
	}
}
