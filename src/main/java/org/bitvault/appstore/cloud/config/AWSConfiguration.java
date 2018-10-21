package org.bitvault.appstore.cloud.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AWSConfiguration {
	
	@Value("${aws.access.key}")
	private String access;
	
	@Value("${aws.secret.key}")
	private String secret;
	
	@Bean
	public AWSCredentials credential() {
		return new BasicAWSCredentials(access, secret);
	}
	
	@Bean
	public AmazonS3 s3client() {
		return AmazonS3ClientBuilder.standard().withRegion(Regions.US_WEST_2).withCredentials(new AWSStaticCredentialsProvider(credential())).build();
//		return new AmazonS3Client(credential()); // deprecated
	}
}