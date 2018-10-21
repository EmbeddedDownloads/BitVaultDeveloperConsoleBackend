package org.bitvault.appstore.cloud.security;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;

import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;

public class EncryptAndGenerateWithAES {
	
	private static final int KEY_SIZE = 128 ;
	private static final String SYMMETRIC_ALGO = "AES" ;
	// buffer size for encrypt data in chunks
	private static final int bufferSize = 1024*1024 ;

	
	public EncryptAndGenerateWithAES() {
		
	}
	
	
	
    public static SecretKey getSecretEncryptionKey() throws Exception{
        KeyGenerator generator = KeyGenerator.getInstance(SYMMETRIC_ALGO);
        generator.init(KEY_SIZE); // The AES key size in number of bits
        SecretKey secKey = generator.generateKey();
        return secKey;
    }
    
    public static String getKeyFromDecretKey(SecretKey secKey) throws Exception{
        
       return new String(Base64.getEncoder().encode(secKey.getEncoded()));
    }
    
    public static byte[] encryptFile(byte[] inputBytes ,SecretKey secKey) throws Exception{
        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance(SYMMETRIC_ALGO);
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] byteCipherText = aesCipher.doFinal(inputBytes);
        return byteCipherText;
    }

    public static File encryptFileAESInChunks(File mFile,SecretKey key,int mode,File outputFile) throws Exception{
    	try {

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(mode, key);

            FileInputStream is=new FileInputStream(mFile);
            BufferedInputStream bis=new BufferedInputStream(is,(int) mFile.length());
            FileOutputStream os=new FileOutputStream(outputFile,true);
            byte[] buffer=new byte[bufferSize];
            int count=0;
            byte[] encryptedByte ;
            count=bis.read(buffer);
            while (count >= 0) {
            	
            	encryptedByte =  cipher.update(buffer,0,count) ;
                os.write(cipher.update(buffer,0,count));
                count = bis.read(buffer);
            }
            
            encryptedByte = cipher.doFinal() ;
            
            if(encryptedByte != null)
            os.write(cipher.doFinal());
            os.flush();
            os.close();
            is.close();
            bis.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    	
    	return outputFile ;
    }
    
    
    public static File encryptFileAESInChunksInput(InputStream mFile,SecretKey key,int mode,File outputFile) throws Exception{
    	try {

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(mode, key);

            FileOutputStream os=new FileOutputStream(outputFile,true);
            byte[] buffer=new byte[bufferSize];
            int count=0 , writeCount = 0 ;
            byte[] encryptedByte ;
            count=mFile.read(buffer);
            while (count >= 0) {
            	System.out.println(writeCount++);
            	encryptedByte =  cipher.update(buffer,0,count) ;
                os.write(encryptedByte,0,encryptedByte.length);
                count = mFile.read(buffer);
            }
            
            encryptedByte = cipher.doFinal() ;
            
            if(encryptedByte != null)
            os.write(encryptedByte,0,encryptedByte.length);
            os.flush();
            os.close();
            mFile.close();
//            bis.close();

        } catch (Exception e) {
            throw new BitVaultException(ErrorMessageConstant.SOME_ERROR_OCCURED);
        }
    	
    	return outputFile ;
    }
    
    
    public static String decryptText(byte[] byteCipherText, SecretKey secKey) throws Exception {
        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance(SYMMETRIC_ALGO);
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
        return new String(bytePlainText);

    }

    private static String  bytesToHex(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash);
        
    }	
}
