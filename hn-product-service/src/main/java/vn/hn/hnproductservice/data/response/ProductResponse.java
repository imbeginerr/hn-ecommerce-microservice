package vn.hn.hnproductservice.data.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ProductResponse {
	private Long id;
	private String sku;
	private String name;
	private String category;
	private String description;
	private BigDecimal price;
	private boolean active;
	private List<ProductImageResponse> images;
}
