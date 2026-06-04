package vn.hn.hnorderservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class InventoryClient {
	
	private final RestClient restClient;
	
	@Value("${services.inventory-url}")
	private String inventoryUrl;
	
	public void reserve(Long productId, Integer quantity) {
		try {
			restClient.post()
					.uri(inventoryUrl + "/inventories/reserve")
					.body(Map.of("productId", productId, "quantity", quantity))
					.retrieve()
					.toBodilessEntity();
		} catch (RestClientResponseException e) {
			throw new IllegalArgumentException("Cannot reserve inventory for product " + productId);
		}
	}
}
