package vn.hn.hnproductservice.data.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductImageResponse {
	private Long id;
	private String fileName;
	private String url;
}
