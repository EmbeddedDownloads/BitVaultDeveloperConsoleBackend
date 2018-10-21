package org.bitvault.appstore.cloud.user.dev.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.bitvault.appstore.cloud.constant.APIConstants;
import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.constant.ValidationConstants;
import org.bitvault.appstore.cloud.dto.AppAddonServicesDto;
import org.bitvault.appstore.cloud.dto.AppApplication;
import org.bitvault.appstore.cloud.model.AddonService;
import org.bitvault.appstore.cloud.model.AppAddonServices;
import org.bitvault.appstore.cloud.user.admin.service.AddonServiceService;
import org.bitvault.appstore.cloud.user.common.GeneralResponseModel;
import org.bitvault.appstore.cloud.user.common.service.AppAddonServicesService;
import org.bitvault.appstore.cloud.utils.Response;
import org.bitvault.appstore.cloud.validator.AddonServiceValidator;
import org.bitvault.appstore.cloud.validator.ApkValidator;
import org.bitvault.appstore.commons.application.service.AppApplicationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = APIConstants.COMMON_API_BASE)
public class AppAddonServicesController {

	@Autowired
	private AppAddonServicesService addonServicesService;

	@Autowired
	private AddonServiceService addonService;

	@Autowired
	private AppApplicationService appApplicationService;

	private Map<String, Object> resultMap = null;

	private String applicationKey = null;
	private String webServerKey = null;
	private String packageName = null;
	private String description = null;

	@PostMapping(value = APIConstants.SAVE_APP_ADDON_SERVICE)
	public ResponseEntity<?> registerAddonServiceForApp(@RequestBody Map<String, String> input,
			HttpServletRequest request) {
		resultMap = new HashMap<String, Object>();
		applicationKey = null;
		webServerKey = null;
		packageName = null;
		description = null;
		try {
			String userId = (String) request.getAttribute(Constants.USERID);
			int serviceId;
			try {
				serviceId = Integer.parseInt(input.get("serviceId"));
			} catch (Exception e) {
				input.clear();
				input.put(Constants.SERVICE_ID, ValidationConstants.REQUIRED_PARAMETER_INTEGER);
				resultMap.put(Constants.MESSAGE, input);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
			}
			applicationKey = input.get("applicationKey");
			webServerKey = input.get("webServerKey");
			packageName = input.get("packageName");
			description = input.get("description");
			input = AddonServiceValidator.validateAddonParam(input, "save");
			if (input.containsValue(ValidationConstants.REQUIRED_PARAMETER_MISSING)) {
				resultMap.put(Constants.MESSAGE, input);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
			}
			AppApplication app = null;
			if (packageName != null && !packageName.isEmpty()) {
				app = appApplicationService.findAppApplicationByPackageName(packageName);
				if (app != null) {
					String appOwner = app.getUserId(); // need to be modified
														// once the sub-users
														// concept will get
														// integrate, to
														// maintain user
														// sub-users
														// relationship
					if (!appOwner.equals(userId)) {
						resultMap.put(Constants.MESSAGE, ErrorMessageConstant.PACKAGE_NAME_UNAVAILABLE);
						return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
					}
				}

				if (ApkValidator.validatePackageName(packageName)) {
					AppAddonServices addonService = null;
					if (userId != null) {
						addonService = addonServicesService.getAppAddOnService(userId, serviceId, packageName);
						if (addonService != null) {
							if (!userId.equals(addonService.getUserId())) {
								resultMap.put(Constants.MESSAGE, ErrorMessageConstant.PACKAGE_NAME_UNAVAILABLE);
							} else
								resultMap.put(Constants.MESSAGE, ErrorMessageConstant.SERVICE_ALREADY_REGISTERED);
							return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
						}
					} else {
						resultMap.put(Constants.MESSAGE, ErrorMessageConstant.USER_NOT_FOUND);
						return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
					}
					addonService = new AppAddonServices();
					addonService.setApplicationKey(applicationKey);
					addonService.setDescription(description);
					addonService.setWebServerKey(webServerKey);
					addonService.setPackageName(packageName);
					addonService.setUserId(userId);
					addonServicesService.save(addonService, serviceId);
				} else {
					resultMap.put(Constants.MESSAGE, ErrorMessageConstant.PACKAGE_NAME_INVALID);
					return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
				}
			} else {
				resultMap.put(Constants.MESSAGE, ErrorMessageConstant.PACKAGE_NAME_NOT_EMPTY);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
			}

		} catch (Exception e) {
			resultMap.put(Constants.MESSAGE, e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
		}
		Map<String, String> result = new HashMap<>();
		result.put("applicationKey", applicationKey);
		result.put("webServerKey", webServerKey);
		result.put("description", description);
		result.put("packageName", packageName);
		resultMap.put(Constants.MESSAGE, result);
		return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, resultMap));
	}

	@GetMapping(value = APIConstants.GET_ADDON_SERVICE)
	public ResponseEntity<?> getAddOnById(@PathVariable int id) {
		resultMap = new HashMap<>();
		try {

			AppAddonServices addonService = addonServicesService.findByAppAddOnId(id);
			AddonService addon = addonService.getAddonService();
			AppAddonServicesDto dto = new AppAddonServicesDto();
			BeanUtils.copyProperties(addon, dto);
			BeanUtils.copyProperties(addonService, dto);
			resultMap.put(Constants.MESSAGE, dto);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, resultMap));
		} catch (BeansException e) {
			resultMap.put(Constants.MESSAGE, ErrorMessageConstant.INVALID_REQUEST);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
		} catch (EntityNotFoundException e) {
			resultMap.put(Constants.MESSAGE, ErrorMessageConstant.NO_REQUEST_FOUND + id);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
		}
	}

	@PutMapping(value = APIConstants.UPDATE_ADDON_SERVICE)
	public ResponseEntity<?> updateAddonService(@RequestBody Map<String, String> input, HttpServletRequest request,
			@PathVariable int id) {
		packageName = null;
		description = null;
		resultMap = new HashMap<String, Object>();
		try {
			String userId = (String) request.getAttribute(Constants.USERID);
			if (userId == null) {
				resultMap.put(Constants.MESSAGE, ErrorMessageConstant.USER_NOT_FOUND);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
			}
			AppAddonServices addonService = addonServicesService.findByAppAddOnId(id);
			if (addonService == null) {
				resultMap.put(Constants.MESSAGE, ErrorMessageConstant.INVALID_REQUEST);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
			}
			packageName = input.get("packageName");
			description = input.get("description");
			input = AddonServiceValidator.validateAddonParam(input, "update");
			if (input.containsValue(ValidationConstants.REQUIRED_PARAMETER_MISSING)) {
				resultMap.put(Constants.MESSAGE, input);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
			}

			AppApplication app = null;
			if (!ApkValidator.validatePackageName(packageName)) {
				resultMap.put(Constants.MESSAGE, ErrorMessageConstant.PACKAGE_NAME_INVALID);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
			}
			if (packageName != null && !packageName.isEmpty()) {
				app = appApplicationService.findAppApplicationByPackageName(packageName);
				String appOwner = null;
				if (app != null) {
					appOwner = app.getUserId(); // need to be modified
												// once the sub-users
												// concept will get
												// integrate, to
												// maintain user
												// sub-users
												// relationship
					if (!appOwner.equals(userId)) {
						resultMap.put(Constants.MESSAGE, ErrorMessageConstant.INVALID_APP_OWNER);
						return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
					}
				}
				String addOnOwner = addonService.getUserId();
				if (!addOnOwner.equals(userId)) {
					resultMap.put(Constants.MESSAGE, ErrorMessageConstant.INVALID_REQUEST);
					return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
				}
				addonService.setPackageName(packageName);
				addonService.setDescription(description);
				addonServicesService.update(addonService);
			} else {
				resultMap.put(Constants.MESSAGE, ErrorMessageConstant.PACKAGE_NAME_NOT_EMPTY);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
			}
		} catch (EntityNotFoundException e) {
			resultMap.put(Constants.MESSAGE, ErrorMessageConstant.NO_REQUEST_FOUND + id);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
		} catch (Exception e) {
			resultMap.put(Constants.MESSAGE, e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
		}
		Map<String, String> result = new HashMap<>();
		result.put("description", description);
		result.put("packageName", packageName);
		resultMap.put(Constants.MESSAGE, result);
		return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, resultMap));
	}

	@GetMapping(value = APIConstants.GET_ADDONS_FOR_ALL_USER_APP)
	public ResponseEntity<?> getListOfAllAddOnServicesForUser(HttpServletRequest request,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "packageName") String orderBy) {
		if (page > 0) {
			page--;
		}
		resultMap = new HashMap<String, Object>();
		try {
			String userId = (String) request.getAttribute(Constants.USERID);
			if (userId == null) {
				resultMap.put(Constants.MESSAGE, ErrorMessageConstant.INVALID_REQUEST);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
			}
			Page<AppAddonServices> list = addonServicesService.getAllAppAddonServices(userId, page, size, orderBy);
			List<AppAddonServicesDto> resultDto = getAddonServicesDTO(list);
			return ResponseEntity.ok(new Response(Constants.SUCCESS, resultDto, list.getTotalPages(),
					list.getTotalElements(), list.getSize(), orderBy));
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put(Constants.MESSAGE, ErrorMessageConstant.SOME_ERROR_OCCURED);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
		}
	}

	private List<AppAddonServicesDto> getAddonServicesDTO(Page<AppAddonServices> list) {
		List<AppAddonServicesDto> resultDto = new ArrayList<AppAddonServicesDto>();
		for (AppAddonServices addonService : list) {
			AddonService addon = addonService.getAddonService();
			AppAddonServicesDto dto = new AppAddonServicesDto();
			BeanUtils.copyProperties(addon, dto);
			BeanUtils.copyProperties(addonService, dto);
			resultDto.add(dto);
		}
		return resultDto;
	}

	@PreAuthorize(Constants.PRE_AUTH_ADMIN)
	@GetMapping(value = APIConstants.GET_ADDONS_FOR_ALL_USER_APP_ADMIN)
	public ResponseEntity<?> getListOfAllAddOnServices(@PathVariable String userId,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "packageName") String orderBy) {
		if (page > 0) {
			page--;
		}
		resultMap = new HashMap<String, Object>();
		try {
			if (userId == null || userId.isEmpty()) {
				resultMap.put(Constants.MESSAGE, ValidationConstants.REQUIRED_PARAMETER_MISSING + "userId");
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
			}
			Page<AppAddonServices> list = addonServicesService.getAllAppAddonServices(userId, page, size, orderBy);
			List<AppAddonServicesDto> resultDto = getAddonServicesDTO(list);
			return ResponseEntity.ok(new Response(Constants.SUCCESS, resultDto, list.getTotalPages(),
					list.getTotalElements(), list.getSize(), orderBy));
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put(Constants.MESSAGE, ErrorMessageConstant.SOME_ERROR_OCCURED);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
		}
	}

	@GetMapping(value = APIConstants.GET_ALL_ADDONS_FOR_AN_APP)
	public ResponseEntity<?> getListOfAllAddOnServicesForAnApp(@PathVariable String packageName,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "packageName") String orderBy) {
		if (page > 0) {
			page--;
		}
		resultMap = new HashMap<String, Object>();
		try {
			Page<AppAddonServices> list = addonServicesService.getAllAppAddonServicesForAnApp(packageName, page, size,
					orderBy);
			List<AppAddonServicesDto> resultDto = getAddonServicesDTO(list);
			return ResponseEntity.ok(new Response(Constants.SUCCESS, resultDto, list.getTotalPages(),
					list.getTotalElements(), list.getSize(), orderBy));
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put(Constants.MESSAGE, ErrorMessageConstant.SOME_ERROR_OCCURED);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
		}
	}

	@PreAuthorize(Constants.PRE_AUTH_ADMIN)
	@PostMapping(value = APIConstants.SAVE_ADDON_SERVICE)
	public ResponseEntity<?> saveAddonService(@RequestBody Map<String, String> input) {
		resultMap = new HashMap<String, Object>();
		try {
			String serviceName = input.get("serviceName");
			if (serviceName == null || serviceName.trim().isEmpty()) {
				resultMap.put(Constants.MESSAGE, ValidationConstants.REQUIRED_PARAMETER_MISSING);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
			}
			AddonService service = addonService.findByServiceName(serviceName);
			if (service != null) {
				resultMap.put(Constants.MESSAGE, ErrorMessageConstant.SERVICE_ALREADY_EXISTS);
				return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
			}
			service = new AddonService();
			service.setAddonServiceName(serviceName);
			addonService.save(service);
			resultMap.put(Constants.MESSAGE, Constants.SUCCESS);
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.SUCCESS, resultMap));
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put(Constants.MESSAGE, e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
		}
	}

	@GetMapping(value = APIConstants.GET_ALL_ADDON_SERVICE)
	public ResponseEntity<?> getAllAddonServices(@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "addonServiceName") String orderBy) {
		try {
			if (page > 0) {
				page--;
			}
			Page<AddonService> serviceList = addonService.getAllAddonService(page, size, orderBy);
			List<AppAddonServicesDto> result = new ArrayList<AppAddonServicesDto>();
			for (AddonService service : serviceList) {
				AppAddonServicesDto dto = new AppAddonServicesDto();
				BeanUtils.copyProperties(service, dto);
				result.add(dto);
			}
			return ResponseEntity.ok(new Response(Constants.SUCCESS, result, serviceList.getTotalPages(),
					serviceList.getTotalElements(), serviceList.getSize(), orderBy));
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put(Constants.MESSAGE, e.getMessage());
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, resultMap));
		}
	}

}
