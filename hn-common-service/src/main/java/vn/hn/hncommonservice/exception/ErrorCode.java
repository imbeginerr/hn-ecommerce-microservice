package vn.hn.hncommonservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
	UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception", HttpStatus.INTERNAL_SERVER_ERROR),
	INVALID_KEY(1001, "Uncategorized Exception", HttpStatus.BAD_REQUEST),
	USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
	INVALID_PASSWORD(1003,
			"Your password must be at least {min} characters long and contain at least one lowercase letter (a-z), one uppercase letter (A-Z), and one digit (0-9).",
			HttpStatus.BAD_REQUEST),
	INVALID_USERNAME(1004, "Your username must be at least {min} characters long and not have a uppercase letter (A-Z).", HttpStatus.BAD_REQUEST),
	USER_NOT_EXISTED(1005, "User Not Existed.", HttpStatus.NOT_FOUND),
	UNAUTHENTICATED(1006, "Unauthenticated.", HttpStatus.UNAUTHORIZED),
	UNAUTHORIZED(1007, "You do not have permission.", HttpStatus.FORBIDDEN),
	TOKEN_ALREADY_INVALIDATED(1008, "Token đã được logout trước đó", HttpStatus.BAD_REQUEST),
	TOKEN_INVALID(1009, "Token không hợp lệ hoặc đã hết hạn", HttpStatus.UNAUTHORIZED),
	ROLE_NOT_FOUND(1010, "Role không tồn tại", HttpStatus.NOT_FOUND),
	;
	
	ErrorCode(int code, String message, HttpStatusCode httpStatuscode) {
		this.code = code;
		this.message = message;
		this.httpStatuscode = httpStatuscode;
	}
	
	private final int code;
	private final String message;
	private final HttpStatusCode httpStatuscode;
}