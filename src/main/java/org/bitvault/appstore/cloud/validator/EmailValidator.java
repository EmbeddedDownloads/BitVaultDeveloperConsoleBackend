package org.bitvault.appstore.cloud.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
	private static final String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static boolean isValidEmailID(String emailId) {
		Pattern pattern = Pattern.compile(emailPattern);
		Matcher matcher = pattern.matcher(emailId);
		return matcher.matches();
	}
//	public static void main(String ...strings ){
//		System.out.println(isValidEmailID("asd@gmail.com"));
//	}
}
