package org.bitvault.appstore.cloud.user.common.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.RandomStringUtils;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileService {

	private String srcFolder = "raw";
	
	private final Path location = Paths.get(srcFolder);
	
	public String uploadFile(MultipartFile file){
		try {
				String fileName = file.getOriginalFilename();
				String fileExtension = fileName.substring(fileName.lastIndexOf("."));
				fileName = RandomStringUtils.randomAlphanumeric(50)+fileExtension;
				Files.copy(file.getInputStream(), location.resolve(fileName));
				return fileName;
		} catch (IOException e) {
			e.printStackTrace();
			throw new BitVaultException(null, null);
		}
	}
	
	public Resource getFile(String filename){
        try {
            Path file = location.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }else{
            	throw new BitVaultException(null, null);
            }
        } catch (MalformedURLException e) {
        	throw new BitVaultException(null, null);
        }
	}
}
