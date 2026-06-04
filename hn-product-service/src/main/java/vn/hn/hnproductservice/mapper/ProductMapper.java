package vn.hn.hnproductservice.mapper;

import org.springframework.stereotype.Component;
import vn.hn.hnproductservice.dao.model.Product;
import vn.hn.hnproductservice.dao.model.ProductImage;
import vn.hn.hnproductservice.data.request.ProductRequest;
import vn.hn.hnproductservice.data.response.ProductImageResponse;
import vn.hn.hnproductservice.data.response.ProductResponse;

@Component
public class ProductMapper {
	
	public Product toProduct(ProductRequest request) {
		Product product = new Product();
		updateProduct(product, request);
		return product;
	}
	
	public void updateProduct(Product product, ProductRequest request) {
		product.setSku(request.getSku());
		product.setName(request.getName());
		product.setCategory(request.getCategory());
		product.setDescription(request.getDescription());
		product.setPrice(request.getPrice());
		product.setActive(request.isActive());
	}
	
	public ProductResponse toResponse(Product product) {
		return ProductResponse.builder()
				.id(product.getId())
				.sku(product.getSku())
				.name(product.getName())
				.category(product.getCategory())
				.description(product.getDescription())
				.price(product.getPrice())
				.active(product.isActive())
				.images(product.getImages().stream().map(this::toImageResponse).toList())
				.build();
	}
	
	private ProductImageResponse toImageResponse(ProductImage image) {
		return ProductImageResponse.builder()
				.id(image.getId())
				.fileName(image.getFileName())
				.url(image.getUrl())
				.build();
	}
}
