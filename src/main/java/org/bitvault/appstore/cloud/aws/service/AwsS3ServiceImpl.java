package org.bitvault.appstore.cloud.aws.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.lang3.RandomStringUtils;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;

@Service
public class AwsS3ServiceImpl implements AwsS3Service {

	@Autowired
	private AmazonS3 s3client;

	private final String profileFolder = Constants.AVATAR_FOLDER;

	@Value("${aws.base.bucket}")
	private String bucket;

	/*
	 * upload file to folder and set it to public
	 */
	public String uploadFile(MultipartFile file, String userId, String avatarFilename) throws IOException {
		String nameCardBucket = bucket;
		FileInputStream stream = (FileInputStream) file.getInputStream();
		FileInputStream istream = (FileInputStream) file.getInputStream();
		try {

			String filename = file.getOriginalFilename();
			String fileExtension = filename.substring(filename.lastIndexOf("."));
			filename = RandomStringUtils.randomAlphanumeric(50) + fileExtension;
			nameCardBucket = nameCardBucket + "/" + userId + "/" + profileFolder;
			if (avatarFilename != null) {
				String key = avatarFilename.substring(avatarFilename.lastIndexOf("/") + 1);
				boolean imageExists = s3client.doesObjectExist(nameCardBucket,
						avatarFilename.substring(avatarFilename.lastIndexOf("/") + 1));
				if (imageExists) {
					deleteKeyFile(nameCardBucket, key);
				}
			}
			s3client.putObject(new PutObjectRequest(nameCardBucket, filename, istream, convert(stream))
					.withCannedAcl(CannedAccessControlList.PublicRead));
			GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(nameCardBucket,
					filename);
			generatePresignedUrlRequest.setMethod(HttpMethod.GET);
			URL avatarURL = s3client.generatePresignedUrl(generatePresignedUrlRequest);
			if(bucket.contains("assets.bitvaultdeveloper.com"))
				return avatarURL.toString().substring(0, avatarURL.toString().indexOf("?")).replace("https://s3-us-west-2.amazonaws.com/", "http://");
			else
				return avatarURL.toString().substring(0, avatarURL.toString().indexOf("?"));
		} catch (Exception e) {
			System.out.print(e.getMessage());
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_UPLOAD);
		} finally {
			if (stream != null) {
				stream.close();
			}
			if (istream != null) {
				istream.close();
			}
		}

	}

	private S3Object getS3Object(String bucket , String key) {
		return s3client.getObject(new GetObjectRequest(bucket,key));
	}
	
	@Override
	public InputStream getS3Object(final String filepath) {
		try {
			String nameCardBucket = bucket;
			String key = filepath.split(nameCardBucket + "/")[1];

			boolean fileExist = isFileExist(s3client, nameCardBucket, key);
			if (fileExist) {
				return getS3Object(nameCardBucket, key).getObjectContent();
			} else {
				throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_GET_FILE);
			}
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_GET_METADATA);
		}
	}
	
	private ObjectMetadata convert(FileInputStream stream) throws IOException {
		byte[] contentBytes;
		contentBytes = IOUtils.toByteArray(stream);
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(Long.valueOf(contentBytes.length));
		return objectMetadata;
	}

	public void deleteKeyFile(String bucket, String key) {
		s3client.deleteObject(new DeleteObjectRequest(bucket, key));
	}

	public Boolean isFileExist(AmazonS3 s3client, String bucket, String fileName) {

		return s3client.doesObjectExist(bucket, fileName);
	}

	@Override
	public String uploadApkFiles(String rawFilePath, String filePath, String fileName) throws IOException {
		String nameCardBucket = bucket;
		
		try {
			nameCardBucket = nameCardBucket + "/" + filePath;

			boolean imageExists = isFileExist(s3client, nameCardBucket, fileName);
			if (imageExists) {
				deleteKeyFile(nameCardBucket, fileName);
			}
			File apkRawFile = new File(rawFilePath);
			s3client.putObject(new PutObjectRequest(nameCardBucket, fileName,apkRawFile)
					.withCannedAcl(CannedAccessControlList.PublicRead));
			GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(nameCardBucket,
					fileName);
			generatePresignedUrlRequest.setMethod(HttpMethod.GET);
			URL fileUrl = s3client.generatePresignedUrl(generatePresignedUrlRequest);
			apkRawFile = null;
			return fileUrl.toString().substring(0, fileUrl.toString().indexOf("?"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_UPLOAD);
		}
	}

	@Override
	public void deleteImage(String filePath) {
		try {
			String nameCardBucket = bucket;
			String key = filePath.substring(filePath.lastIndexOf("/") + 1);
			nameCardBucket = nameCardBucket + "/" + filePath.substring(0, filePath.lastIndexOf("/"));
			boolean imageExists = isFileExist(s3client, nameCardBucket, key);
			if (imageExists) {
				deleteKeyFile(nameCardBucket, key);
			}
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_DELETE);
		}

	}

	@Override
	public String uploadCategoryIcon(MultipartFile file, String fileName, String oldIconUrl) throws IOException {
		String nameCardBucket = bucket;
		FileInputStream stream = (FileInputStream) file.getInputStream();
		FileInputStream istream = (FileInputStream) file.getInputStream();

		try {
			nameCardBucket = nameCardBucket + Constants.CATEGORY_ICON_FILE_PATH;
			if (null != oldIconUrl) {
				boolean imageExists = isFileExist(s3client, nameCardBucket,
						oldIconUrl.substring(oldIconUrl.lastIndexOf("/") + 1));
				if (imageExists) {
					deleteKeyFile(nameCardBucket, fileName);
				}
			}
			s3client.putObject(new PutObjectRequest(nameCardBucket, fileName, istream, convert(stream))
					.withCannedAcl(CannedAccessControlList.PublicRead));
			GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(nameCardBucket,
					fileName);
			generatePresignedUrlRequest.setMethod(HttpMethod.GET);
			URL fileUrl = s3client.generatePresignedUrl(generatePresignedUrlRequest);
			return fileUrl.toString().substring(0, fileUrl.toString().indexOf("?"));
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_UPLOAD);
		} finally {
			if (stream != null) {
				stream.close();
			}
			if (istream != null) {
				istream.close();
			}
		}
	}
	
	
	@Override
	public String uploadCategoryBanner(MultipartFile file, String fileName, String oldIconUrl) throws IOException {
		String nameCardBucket = bucket;
		FileInputStream stream = (FileInputStream) file.getInputStream();
		FileInputStream istream = (FileInputStream) file.getInputStream();

		try {
			nameCardBucket = nameCardBucket + Constants.CATEGORY_BANNER_FILE_PATH;
			if (null != oldIconUrl) {
				boolean imageExists = isFileExist(s3client, nameCardBucket,
						oldIconUrl.substring(oldIconUrl.lastIndexOf("/") + 1));
				if (imageExists) {
					deleteKeyFile(nameCardBucket, fileName);
				}
			}
			s3client.putObject(new PutObjectRequest(nameCardBucket, fileName, istream, convert(stream))
					.withCannedAcl(CannedAccessControlList.PublicRead));
			GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(nameCardBucket,
					fileName);
			generatePresignedUrlRequest.setMethod(HttpMethod.GET);
			URL fileUrl = s3client.generatePresignedUrl(generatePresignedUrlRequest);
			return fileUrl.toString().substring(0, fileUrl.toString().indexOf("?"));
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_UPLOAD);
		} finally {
			if (stream != null) {
				stream.close();
			}
			if (istream != null) {
				istream.close();
			}
		}
	}

	@Override
	public ObjectMetadata getMetaData(final String fileUrl) {
		try {
			String nameCardBucket = bucket;
			String key = fileUrl.split(nameCardBucket + "/")[1];

			boolean fileExist = isFileExist(s3client, nameCardBucket, key);
			if (fileExist) {
				return getMetaData(nameCardBucket, key);
			} else {
				throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_GET_FILE);
			}
		} catch (Exception e) {
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_GET_METADATA);
		}
	}

	private ObjectMetadata getMetaData(String nameCardBucket, String key) {
		return s3client.getObjectMetadata(nameCardBucket, key);

	}
	@Override
	public String uploadApkImages(MultipartFile file, String filePath, String fileName) throws IOException {
		String nameCardBucket = bucket;
		FileInputStream stream = (FileInputStream) file.getInputStream();
		FileInputStream istream = (FileInputStream) file.getInputStream();

		try {
			nameCardBucket = nameCardBucket + "/" + filePath;

			boolean imageExists = isFileExist(s3client, nameCardBucket, fileName);
			if (imageExists) {
				deleteKeyFile(nameCardBucket, fileName);
			}
			s3client.putObject(new PutObjectRequest(nameCardBucket, fileName, istream, convert(stream))
					.withCannedAcl(CannedAccessControlList.PublicRead));
			GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(nameCardBucket,
					fileName);
			generatePresignedUrlRequest.setMethod(HttpMethod.GET);
			URL fileUrl = s3client.generatePresignedUrl(generatePresignedUrlRequest);
			return fileUrl.toString().substring(0, fileUrl.toString().indexOf("?"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_UPLOAD);
		} finally {
			if (stream != null) {
				stream.close();
			}
			if (istream != null) {
				istream.close();
			}
		}
	}

}
