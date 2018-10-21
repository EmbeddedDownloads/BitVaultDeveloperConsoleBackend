package org.bitvault.appstore.cloud.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.exception.BitVaultException;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import net.dongliu.apk.parser.bean.CertificateMeta;

public class APKparser {
	private String filePath;
	private static String cnName = "", organization = "";

	public APKparser(String filePath) {

		this.filePath = filePath;
	}

	// public static final String FILE_PATH =
	// "/home/linchpin/Documents/bitvaultappstore/app-release.apk";

	public static String execCmd(String cmd) throws java.io.IOException {
		java.util.Scanner s = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream())
				.useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	public static String parseCMDResult(String result) {
		String[] splitWithNewLine = null;
		splitWithNewLine = result.split("\n");
		int len = splitWithNewLine.length;
		String cnString = null;
		for (int i = 0; i < len; i++) {
			if (splitWithNewLine[i].contains("CN=")) {
				cnString = splitWithNewLine[i];
				break;
			}

		}
		System.out.println(cnString);
		return cnString;
	}

	public static String parseCnString(String cnString) {
		String result[] = null;
		String cn = null;
		if (null != cnString && !cnString.trim().isEmpty() && cnString.contains("CN=")) {
			result = cnString.split(", ");
			for (int i = 0; i < result.length; i++) {
				if (result[i].contains("CN=")) {
					System.out.println("result[i]:: " + result[i]);
					cn = result[i].split("CN=")[1].equals("Android Debug") ? Constants.DEBUG_MODE
							: Constants.RELEASE_MODE;
				}

			}
		}
		return cn;
	}
	//
	// public static void main(String[] args) {
	// String mArchiveSourcePath =
	// "/home/linchpin/Documents/bitvaultappstore/woobly_unsigned.apk";
	//// String mArchiveSourcePath =
	// "/home/linchpin/Documents/bitvaultappstore/9_Jan_Woobly.apk";
	// getAPKDetail(mArchiveSourcePath);
	// }

	public static APKDetail getAPKDetail(String filePath) {
		APKDetail apkDetail = new APKDetail();
		try {

			ApkFile apkFile = new ApkFile(new File(filePath));
			ApkMeta apkMeta = apkFile.getApkMeta();

			/*
			 * List<String> permissionList = apkMeta.getUsesPermissions();
			 * 
			 * for(String mpermission : permissionList) { System.out.println(
			 * "Permissionn Name ::: " + mpermission); }
			 */

			String mode = checkWhetherSignedOrUnsigned(filePath);

			System.out.println("app name ::: " + apkMeta.getName());
			System.out.println("app label ::: " + apkMeta.getLabel());
			System.out.println("app package ::: " + apkMeta.getPackageName());
			System.out.println("app versionname ::: " + apkMeta.getVersionName());
			System.out.println("app version code ::: " + apkMeta.getVersionCode());
			System.out.println("Mode ::: " + mode);
			System.out.println("Organization ::: " + organization);

			List<CertificateMeta> certs = apkFile.getCertificateMetaList();
			apkDetail.setMode(mode);
			apkDetail.setAppName(apkMeta.getName());
			apkDetail.setVersionName(apkMeta.getVersionName());
			apkDetail.setVersionNo(apkMeta.getVersionCode().toString());
			apkDetail.setPackageName(apkMeta.getPackageName());
			apkDetail.setPermissionList(apkMeta.getUsesPermissions());
			apkDetail.setOrganization(organization);
			if (certs.size() > 0) {
				apkDetail.setCertBase64Md5(certs.get(0).getCertBase64Md5());
				apkDetail.setCertMd5(certs.get(0).getCertMd5());
				apkDetail.setSignAlgorithm(certs.get(0).getSignAlgorithm());
				System.out.println("CertBase64Md5:::" + certs.get(0).getCertBase64Md5());
				System.out.println("certs.get(0).getCertMd5()::::" + certs.get(0).getCertMd5());
				System.out.println("certs.get(0).getSignAlgorithm():::" + certs.get(0).getSignAlgorithm());

				System.out.println(certs.get(0).getSignAlgorithmOID());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BitVaultException(ErrorMessageConstant.UNABLE_TO_PARSE_FILE);

		}
		return apkDetail;
	}

	private static String checkWhetherSignedOrUnsigned(String filePath) {
		// TODO Auto-generated method stub
		final Object mSync = new Object();
		Certificate[] certificates;
		Enumeration entries;
		WeakReference<byte[]> readBufferRef;
		WeakReference<byte[]> mReadBuffer = null;
		byte[] readBuffer = null;

		// synchronized (mSync) {
		readBufferRef = mReadBuffer;
		if (readBufferRef != null) {
			mReadBuffer = null;
			readBuffer = readBufferRef.get();
		}
		if (readBuffer == null) {
			readBuffer = new byte[8192];
			readBufferRef = new WeakReference<byte[]>(readBuffer);
		}
		// }

		try {
			JarFile jarFile = new JarFile(filePath);

			entries = jarFile.entries();

			while (entries.hasMoreElements()) {
				JarEntry jarEntry = (JarEntry) entries.nextElement();

				if (loadCertificates(jarFile, jarEntry, readBuffer)) {
					return Constants.RELEASE_MODE;
				} else {
					return Constants.DEBUG_MODE;
				}
			}

			jarFile.close();

			synchronized (mSync) {
				mReadBuffer = readBufferRef;
			}

		} catch (IOException e) {
			System.err.println("Exception reading " + filePath + "\n" + e);
		} catch (RuntimeException e) {
			System.err.println("Exception reading " + filePath + "\n" + e);
		}
		return filePath;
	}

	private static boolean loadCertificates(JarFile jarFile, JarEntry jarEntry, byte[] readBuffer) {

		try {

			InputStream inputStream = jarFile.getInputStream(jarEntry);

			while (inputStream.read(readBuffer, 0, readBuffer.length) != -1) {

			}

			return isSignedAPK(jarEntry);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	private static boolean isSignedAPK(JarEntry jarEntry) {
		try {

			if (jarEntry != null && jarEntry.getCertificates() != null && jarEntry.getCertificates().length > 0) {

				X509Certificate certificate = (X509Certificate) jarEntry.getCertificates()[0];
				X500Name x500name = new JcaX509CertificateHolder(certificate).getSubject();

				if (x500name.getRDNs(BCStyle.CN) != null && x500name.getRDNs(BCStyle.CN).length > 0) {
					RDN rdn = x500name.getRDNs(BCStyle.CN)[0];
					cnName = IETFUtils.valueToString(rdn.getFirst().getValue());
				}

				if (x500name.getRDNs(BCStyle.O) != null && x500name.getRDNs(BCStyle.O).length > 0) {
					RDN rdn_organization = x500name.getRDNs(BCStyle.O)[0];
					organization = IETFUtils.valueToString(rdn_organization.getFirst().getValue());
				} else if (x500name.getRDNs(BCStyle.OU) != null && x500name.getRDNs(BCStyle.OU).length > 0) {
					RDN rdn_organization = x500name.getRDNs(BCStyle.OU)[0];
					organization = IETFUtils.valueToString(rdn_organization.getFirst().getValue());
				}

				System.out.println("CN Name : " + cnName);
				System.out.println("Organization : " + organization);
				if (cnName.equalsIgnoreCase("Android Debug")) {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}