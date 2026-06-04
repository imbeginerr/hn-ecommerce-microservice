package vn.hn.hninventoryservice.data.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventoryRequest {
	
	@NotNull
	private Long productId;
	
	@NotBlank
	private String sku;
	
	@NotNull
	@Min(0)
	private Integer quantity;
	
	@Min(0)
	private Integer reservedQuantity = 0;
	
	private String warehouseLocation;
}
