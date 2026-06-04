package vn.hn.hninventoryservice.data.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryResponse {
	private Long id;
	private Long productId;
	private String sku;
	private Integer quantity;
	private Integer reservedQuantity;
	private Integer availableQuantity;
	private String warehouseLocation;
}
