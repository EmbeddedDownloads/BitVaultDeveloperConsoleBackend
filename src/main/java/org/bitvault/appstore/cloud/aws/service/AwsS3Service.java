package org.bitvault.appstore.cloud.aws.service;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

public interface AwsS3Service {

	public String uploadFile(MultipartFile file, String userId, String avatarFilename) throws IOException;

	public String uploadApkFiles(String rawFilePath, String filePath, String fileName) throws IOException;
	
	public String uploadApkImages(MultipartFile file, String filePath, String fileName) throws IOException;

	public void deleteImage(String filePath);

	public InputStream getS3Object(String filepath);
	/**
	 * Get metadata of the s3 object
	 * 
	 * @param filepath
	 *            : filepath(url) of s3 object
	 * @return ObjectMetadata :metadata of object
	 */
	public ObjectMetadata getMetaData(String filepath);

	String uploadCategoryIcon(MultipartFile file, String fileName,String oldIconUrl) throws IOException;
	
	String uploadCategoryBanner(MultipartFile file, String fileName,String oldBanerUrl) throws IOException;
	
}
