package vn.hn.hncommonservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import vn.hn.hncommonservice.entity.ApiResponse;
import vn.hn.hncommonservice.exception.ErrorCode;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
		response.setStatus(errorCode.getHttpStatuscode().value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		ApiResponse<?> apiResponse = new ApiResponse<>();
		apiResponse.setMessage(errorCode.getMessage());
		apiResponse.setCode(errorCode.getCode());
		
		ObjectMapper mapper = new ObjectMapper();
		
		response.getWriter().write(mapper.writeValueAsString(apiResponse));
		response.flushBuffer();
	}
}
