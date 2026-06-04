package vn.hn.hnorderservice.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class RestClientConfig {
	
	@Bean
	public RestClient restClient(RestClient.Builder builder) {
		return builder.requestInterceptor((request, body, execution) -> {
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attributes != null) {
				HttpServletRequest currentRequest = attributes.getRequest();
				String authorization = currentRequest.getHeader("Authorization");
				if (authorization != null) {
					request.getHeaders().set("Authorization", authorization);
				}
			}
			return execution.execute(request, body);
		}).build();
	}
}
