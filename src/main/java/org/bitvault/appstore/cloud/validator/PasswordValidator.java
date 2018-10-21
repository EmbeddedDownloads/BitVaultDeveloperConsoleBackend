package org.bitvault.appstore.cloud.validator;

public class PasswordValidator {

	public static boolean compareNewAndConfirmPassword(String newPassword, String confirmPassword) {
		return newPassword.equals(confirmPassword);
	}

	public static boolean isValidPassword(String newPassword) {
		// return SignupValidator.isValidPassword(newPassword);
		return true;
	}
	// public static void main(String...strings){
	// String pass = "l5@8A0";
	// System.out.println(isValidPassword(pass));
	// }
}
