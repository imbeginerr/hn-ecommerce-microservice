package vn.hn.hnorderservice.client;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class ProductClient {
	
	private final RestClient restClient;
	
	@Value("${services.product-url}")
	private String productUrl;
	
	public ProductResponse findById(Long id) {
		try {
			ApiResponse response = restClient.get()
					.uri(productUrl + "/products/{id}", id)
					.retrieve()
					.body(ApiResponse.class);
			return response.getData();
		} catch (RestClientResponseException e) {
			throw new IllegalArgumentException("Cannot load product " + id);
		}
	}
	
	@Data
	public static class ApiResponse {
		private ProductResponse data;
	}
	
	@Data
	public static class ProductResponse {
		private Long id;
		private String sku;
		private String name;
		private BigDecimal price;
	}
}
