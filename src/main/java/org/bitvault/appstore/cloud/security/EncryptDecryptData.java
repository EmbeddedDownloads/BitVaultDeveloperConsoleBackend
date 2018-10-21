package org.bitvault.appstore.cloud.security;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.jcajce.provider.util.BadBlockException;

/**
 * Class for performing encryption and decryption
 */

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptDecryptData {

	public static final Logger logger = LoggerFactory.getLogger(EncryptDecryptData.class);

	final private static String BTCprivKey = "18E14A7B6A307F426A94F8114701E7C8E774E7F9A47E2C2035DB29A206321725";

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	/**
	 * @param rawData
	 *            raw data which needs to be encrypted
	 * @param strPubKey
	 *            -- public key
	 * @return
	 */
	public byte[] encryptData(final byte[] rawData, final String strPubKey) {

		// Generate Symmetric Keys from TXID
		final SecretKey SymKey = null;
		try {

			// Get Bitcoin Keys from receiver address
			final PublicKey pubKey = EncryptDecryptHelper.getBTCPublicKey(strPubKey);

			// Encrypt session key with public key
			return EncryptDecryptHelper.asymEncryption(rawData, pubKey);
			// byte[] EncryptedSessionKey = Base64.getEncoder().encode(kcipher);

		} catch (final Exception e) {
			throw new BitVaultException(ErrorMessageConstant.INVALID_BVK);
		}
	}

	/**
	 * @param rawData
	 *            raw data which needs to be encrypted
	 * @param strPubKey
	 *            -- public key
	 * @return
	 */
	public byte[] encryptDataAES(String key, byte[] file) {

		// Generate Symmetric Keys from TXID
		final SecretKey SymKey = null;
		try {

			// Encrypt session key with public key
			return EncryptDecryptHelper.symEncryptionAES(key, file);
			// byte[] EncryptedSessionKey = Base64.getEncoder().encode(kcipher);

		} catch (final Exception e) {
			throw new BitVaultException(ErrorMessageConstant.INVALID_BVK);
		}
	}

	/* ----------------------- Decryption ------------------ *//*
																*//* Method used to decrypt data for dpk file to apk */
	public byte[] decryptMessage(final byte[] decryptedInput, String privateKey) {

		try {
			PrivateKey PrivKey = EncryptDecryptHelper.getBTCPrivateKey(privateKey);

			// decrypt with private key
			return EncryptDecryptHelper.asymDecryption(decryptedInput, PrivKey);

		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof InvalidCipherTextException) {
				throw new BitVaultException(ErrorMessageConstant.INVALID_BITVAULT_KEY);
			} else if (e instanceof BadBlockException) {
				throw new BitVaultException(ErrorMessageConstant.INVALID_BVK);
			} else {
				throw new BitVaultException(ErrorMessageConstant.INVALID_BVK);
			}
		}

	}

	/**
	 * Method to encrypt data on the basis of public key
	 *
	 * @param sourceFile
	 * @return
	 */
	public byte[] encryptData(final String sourceFile, final String publicKey) {
		final File file = new File(sourceFile);
		try {
			return encryptData(Files.readAllBytes(file.toPath()), publicKey.trim());
		} catch (final IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	/**
	 * Method to encrypt data on the basis of public key
	 *
	 * @param sourceFile
	 * @return
	 */
	public byte[] encryptDataSymm(byte[] keyBytes, final String publicKey) {

		try {
			return encryptData(keyBytes, publicKey.trim());
		} catch (final Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}

	/* ----------------------- Decryption ------------------ *//*
																*//* Method used to decrypt data for dpk file to apk */
	public File decryptMessageAES(File inputFile, File outputFile, SecretKey key) {

		try {

			logger.info("Decrypting Encrypted file form symmetric key");

			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);

			FileInputStream is = new FileInputStream(inputFile);
			BufferedInputStream bis = new BufferedInputStream(is, (int) inputFile.length());
			FileOutputStream os = new FileOutputStream(outputFile, true);
			byte[] buffer = new byte[1024 * 1024];
			byte[] decryptedByte;
			int count, stepCount = 0;

			count = bis.read(buffer);
			while (count >= 0) {
				// Log.v("number of times loop", stepCount++ + "");
				decryptedByte = cipher.update(buffer, 0, count);
				os.write(decryptedByte, 0, decryptedByte.length);
				count = bis.read(buffer);
			}

			decryptedByte = cipher.doFinal();

			if (decryptedByte != null)
				os.write(decryptedByte, 0, decryptedByte.length);

			os.flush();
			os.close();
			is.close();
			bis.close();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return outputFile;
	}
}
