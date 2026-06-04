package vn.hn.hnproductservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.hn.hncommonservice.service.IsTokenInvalidated;

@Configuration
public class TokenInvalidationConfig {
	
	@Bean
	public IsTokenInvalidated isTokenInvalidated() {
		return tokenId -> false;
	}
}
