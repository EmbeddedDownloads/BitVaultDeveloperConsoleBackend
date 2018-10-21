package org.bitvault.appstore.cloud.exception;

import javax.servlet.http.HttpServletResponse;

import org.bitvault.appstore.cloud.constant.Constants;
import org.bitvault.appstore.cloud.constant.ErrorMessageConstant;
import org.bitvault.appstore.cloud.user.common.GeneralResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

//purpose of this class is to handle with the global exception
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InvalidTokenException.class)
	public String handleInvalidTokenException() {
		return "{\"error\":\"Invalid Token.\"}";
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public String handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
		return "{\"error\":\"" + e.getMessage() + "\"}";
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public Object handleHttpMessageNotReadableException() {
		return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(ErrorMessageConstant.INVALID_REQUIRED_REQUEST_BODY)));
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException e) {
		return ResponseEntity.ok(e);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public String handleUsernameNotFoundException(final UsernameNotFoundException e, HttpServletResponse response) {
		response.setStatus(HttpStatus.OK.value());
		return "{\"error\":\"" + e.getMessage() + ".\"}";
	}
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public Object handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletResponse response) {
		return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(e.getMessage())));
	}
	
	@ExceptionHandler(MultipartException.class)
	public ResponseEntity<?> handleMultipartException(MultipartException e) {
		String errorMessage = e.getMessage();		
		if(errorMessage.contains("SizeLimitExceededException")){
			errorMessage = errorMessage.substring(errorMessage.lastIndexOf(": ") + 2);
			int actualSize = Integer.parseInt(errorMessage.substring(errorMessage.indexOf("size (") + 6, errorMessage.indexOf(") ex")));
			int permittedSize = Integer.parseInt(errorMessage.substring(errorMessage.lastIndexOf("(") + 1, errorMessage.lastIndexOf(")")));;
			return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse("Server allowed maximum file size exceeded. Allowed: "+ (permittedSize/1024)/1024 + "Mb" + " Sent: "+ (actualSize/1024)/1024 + "Mb")));
			
		}
		return ResponseEntity.ok(GeneralResponseModel.of(Constants.FAILED, new BitVaultResponse(errorMessage)));
	}
}
