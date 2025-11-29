package vn.hn.hncommonservice.exception;

import jakarta.validation.ConstraintViolation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vn.hn.hncommonservice.entity.ApiResponse;

import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static final String ERROR_VALUE = "min";
	
	@ExceptionHandler(value = RuntimeException.class)
	ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException e) {
		ApiResponse apiResponse = new ApiResponse<>();
		apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
		apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
		return ResponseEntity.badRequest().body(apiResponse);
	}
	
	@ExceptionHandler(value = AppException.class)
	ResponseEntity<ApiResponse> handlingAppException(AppException e) {
		ApiResponse apiResponse = new ApiResponse<>();
		ErrorCode errorCode = e.getErrorCode();
		apiResponse.setMessage(errorCode.getMessage());
		apiResponse.setCode(errorCode.getCode());
		return ResponseEntity.status(errorCode.getHttpStatuscode()).body(apiResponse);
	}
	
	@ExceptionHandler(value = AccessDeniedException.class)
	ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException e) {
		ApiResponse apiResponse = new ApiResponse<>();
		ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
		apiResponse.setMessage(errorCode.getMessage());
		apiResponse.setCode(errorCode.getCode());
		return ResponseEntity.status(errorCode.getHttpStatuscode()).body(apiResponse);
		
	}
	
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException e) {
		String errorKey = Objects.requireNonNull(e.getFieldError()).getDefaultMessage();
		
		ErrorCode errorCode = ErrorCode.INVALID_KEY;
		Map<String, Object> attributes = null;
		try {
			errorCode = ErrorCode.valueOf(errorKey);
			
			var constraintViolations = e.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);
			attributes = constraintViolations.getConstraintDescriptor().getAttributes();
			
		} catch (IllegalArgumentException _) {
			
		}
		
		ApiResponse apiResponse = new ApiResponse<>();
		
		apiResponse.setMessage(Objects.nonNull(attributes) ? mapAttributes(errorCode.getMessage(), attributes) : errorCode.getMessage());
		apiResponse.setCode(errorCode.getCode());
		
		return ResponseEntity.badRequest().body(apiResponse);
	}
	
	private String mapAttributes(String message, Map<String, Object> attributes) {
		String minValue = String.valueOf(attributes.get(ERROR_VALUE));
		return message.replace("{" + ERROR_VALUE + "}", minValue);
	}
}
