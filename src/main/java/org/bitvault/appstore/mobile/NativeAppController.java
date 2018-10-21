package org.bitvault.appstore.mobile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.bitvault.appstore.cloud.constant.APIConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = APIConstants.MOBILE_API_BASE)
public class NativeAppController {

	@RequestMapping(value = "/fee/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getFees(@PathVariable int id) {

		return new ResponseEntity<String>(fetchAllNativeApp("document.json")+""+id, HttpStatus.OK);
	}

	private String fetchAllNativeApp(String filepath) {
		String allApps = null;
		try {
			FileInputStream required = new FileInputStream(new File(filepath));
			allApps = IOUtils.toString(required);
			required.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return allApps;
	}

	@RequestMapping(value = "/nativeapps", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getAllNativeAppList() {

		return new ResponseEntity<String>(fetchAllNativeApp("allAap.json"),
				HttpStatus.OK);
	}

}
