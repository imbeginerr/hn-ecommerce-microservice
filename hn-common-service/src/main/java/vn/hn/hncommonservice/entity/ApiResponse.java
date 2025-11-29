package vn.hn.hncommonservice.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;

/**
 * Generic API Response wrapper for all REST endpoints
 *
 * @param <T>
 * 		The type of data being returned
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> implements Serializable {
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	private int code;
	
	private String message;
	
	private T data;
	
	// ============= SUCCESS RESPONSES =============
	
	/**
	 * Success response with data
	 */
	public static <T> ApiResponse<T> success(T data) {
		return success("Operation successful", data);
	}
	
	/**
	 * Success response with custom message and data
	 */
	public static <T> ApiResponse<T> success(String message, T data) {
		return ApiResponse.<T>builder()
				.code(HttpStatus.OK.value())
				.message(message)
				.data(data)
				.build();
	}
	
	/**
	 * Success response without data
	 */
	public static <T> ApiResponse<T> success() {
		return success("Operation successful", null);
	}
	
	/**
	 * Success response with custom message only
	 */
	public static <T> ApiResponse<T> success(String message) {
		return success(message, null);
	}
	
	/**
	 * Created response (201) - for POST requests
	 */
	public static <T> ApiResponse<T> created(T data) {
		return created("Resource created successfully", data);
	}
	
	/**
	 * Created response with custom message
	 */
	public static <T> ApiResponse<T> created(String message, T data) {
		return ApiResponse.<T>builder()
				.code(HttpStatus.CREATED.value())
				.message(message)
				.data(data)
				.build();
	}
	
	/**
	 * No content response (204) - for DELETE requests
	 */
	public static <T> ApiResponse<T> deleted() {
		return ApiResponse.<T>builder()
				.code(HttpStatus.NO_CONTENT.value())
				.message("Operation successful")
				.build();
	}
	
	// ============= ERROR RESPONSES =============
	
	/**
	 * Bad request error (400)
	 */
	public static <T> ApiResponse<T> badRequest(String message) {
		return error(HttpStatus.BAD_REQUEST.value(), message);
	}
	
	/**
	 * Unauthorized error (401)
	 */
	public static <T> ApiResponse<T> unauthorized(String message) {
		return error(HttpStatus.UNAUTHORIZED.value(), message);
	}
	
	/**
	 * Forbidden error (403)
	 */
	public static <T> ApiResponse<T> forbidden(String message) {
		return error(HttpStatus.FORBIDDEN.value(), message);
	}
	
	/**
	 * Not found error (404)
	 */
	public static <T> ApiResponse<T> notFound(String message) {
		return error(HttpStatus.NOT_FOUND.value(), message);
	}
	
	/**
	 * Conflict error (409) - for duplicate resources
	 */
	public static <T> ApiResponse<T> conflict(String message) {
		return error(HttpStatus.CONFLICT.value(), message);
	}
	
	/**
	 * Internal server error (500)
	 */
	public static <T> ApiResponse<T> error(String message) {
		return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
	}
	
	/**
	 * Generic error with custom code
	 */
	public static <T> ApiResponse<T> error(int code, String message) {
		return ApiResponse.<T>builder()
				.code(code)
				.message(message)
				.build();
	}
	
	/**
	 * Error with data (validation errors, etc.)
	 */
	public static <T> ApiResponse<T> error(int code, String message, T data) {
		return ApiResponse.<T>builder()
				.code(code)
				.message(message)
				.data(data)
				.build();
	}
	
	// ============= UTILITY METHODS =============
	
	/**
	 * Builder pattern support
	 */
	public static <T> RBuilder<T> builder() {
		return new RBuilder<>();
	}
	
	/**
	 * Custom builder class
	 */
	public static class RBuilder<T> {
		private int code;
		private String message;
		private T data;
		
		public RBuilder<T> code(int code) {
			this.code = code;
			return this;
		}
		
		public RBuilder<T> message(String message) {
			this.message = message;
			return this;
		}
		
		public RBuilder<T> data(T data) {
			this.data = data;
			return this;
		}
		
		public ApiResponse<T> build() {
			ApiResponse<T> response = new ApiResponse<>();
			response.code = this.code;
			response.message = this.message;
			response.data = this.data;
			return response;
		}
	}
}
