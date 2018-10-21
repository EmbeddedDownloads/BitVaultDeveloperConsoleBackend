package org.bitvault.appstore.cloud.validator;

import java.util.Map;

import org.bitvault.appstore.cloud.constant.ValidationConstants;

public class AddonServiceValidator {

	public static Map<String, String> validateAddonParam(Map<String, String> input, String flag) {
		String applicationKey = null;
		String webServerKey = null;
		if (flag.equals("save")) {
			applicationKey = input.get("applicationKey");
			webServerKey = input.get("webServerKey");
		}
		String packageName = input.get("packageName");
		String description = input.get("description");
		input.clear();
		if (flag.equals("save")) {
			if (applicationKey == null || applicationKey.isEmpty()) {
				input.put("applicationKey", ValidationConstants.REQUIRED_PARAMETER_MISSING);
			}
			if (webServerKey == null || webServerKey.isEmpty()) {
				input.put("webServerKey", ValidationConstants.REQUIRED_PARAMETER_MISSING);
			}
		}
		if (packageName == null || packageName.isEmpty()) {
			input.put("packageName", ValidationConstants.REQUIRED_PARAMETER_MISSING);
		}
		if (description == null || description.isEmpty()) {
			input.put("description", ValidationConstants.REQUIRED_PARAMETER_MISSING);
		}
		return input;
	}
}
