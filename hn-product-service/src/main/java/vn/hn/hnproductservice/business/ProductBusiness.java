package vn.hn.hnproductservice.business;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.hn.hnproductservice.dao.model.Product;
import vn.hn.hnproductservice.dao.model.ProductImage;
import vn.hn.hnproductservice.dao.service.ProductService;
import vn.hn.hnproductservice.data.request.ProductRequest;
import vn.hn.hnproductservice.data.response.ProductResponse;
import vn.hn.hnproductservice.mapper.ProductMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductBusiness {
	
	private final ProductService productService;
	private final ProductMapper productMapper;
	
	@Value("${app.upload-dir:uploads}")
	private String uploadDir;
	
	@Value("${app.public-url-prefix:/api/v1/product}")
	private String publicUrlPrefix;
	
	@Transactional(readOnly = true)
	public List<ProductResponse> findAll() {
		return productService.findByDeletedFalseOrderByIdDesc()
				.stream()
				.map(productMapper::toResponse)
				.toList();
	}
	
	@Transactional(readOnly = true)
	public List<ProductResponse> findPublicProducts(String keyword, String category) {
		List<Product> products;
		if (keyword != null && !keyword.isBlank()) {
			products = productService.searchPublicProducts(keyword.trim());
		} else if (category != null && !category.isBlank()) {
			products = productService.findPublicProductsByCategory(category.trim());
		} else {
			products = productService.findPublicProducts();
		}
		
		return products.stream()
				.map(productMapper::toResponse)
				.toList();
	}
	
	@Transactional(readOnly = true)
	public ProductResponse findById(Long id) {
		return productMapper.toResponse(getProduct(id));
	}
	
	@Transactional
	public ProductResponse create(ProductRequest request) {
		if (productService.existsBySkuAndDeletedFalse(request.getSku())) {
			throw new IllegalArgumentException("SKU already exists");
		}
		
		Product product = productMapper.toProduct(request);
		return productMapper.toResponse(productService.save(product));
	}
	
	@Transactional
	public ProductResponse update(Long id, ProductRequest request) {
		Product product = getProduct(id);
		productMapper.updateProduct(product, request);
		return productMapper.toResponse(productService.save(product));
	}
	
	@Transactional
	public ProductResponse uploadImages(Long id, List<MultipartFile> images) {
		Product product = getProduct(id);
		Path productDirectory = Path.of(uploadDir, "products", String.valueOf(id)).toAbsolutePath().normalize();
		
		try {
			Files.createDirectories(productDirectory);
			for (MultipartFile image : images) {
				if (image.isEmpty()) {
					continue;
				}
				
				String originalFileName = image.getOriginalFilename() == null ? "image" : image.getOriginalFilename();
				String extension = "";
				int extensionIndex = originalFileName.lastIndexOf('.');
				if (extensionIndex >= 0) {
					extension = originalFileName.substring(extensionIndex);
				}
				String storedFileName = UUID.randomUUID() + extension;
				Path destination = productDirectory.resolve(storedFileName);
				Files.copy(image.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
				
				ProductImage productImage = new ProductImage();
				productImage.setProduct(product);
				productImage.setFileName(originalFileName);
				productImage.setUrl(buildImageUrl(id, storedFileName));
				product.getImages().add(productImage);
			}
		} catch (IOException e) {
			throw new IllegalStateException("Cannot upload product images", e);
		}
		
		return productMapper.toResponse(productService.save(product));
	}

	@Transactional
	public ProductResponse deleteImage(Long id, Long imageId) {
		Product product = getProduct(id);
		boolean removed = product.getImages().removeIf(image -> image.getId().equals(imageId));
		if (!removed) {
			throw new IllegalArgumentException("Product image not found");
		}
		return productMapper.toResponse(productService.save(product));
	}
	
	@Transactional
	public void delete(Long id) {
		Product product = getProduct(id);
		product.softDelete();
		productService.save(product);
	}
	
	@Transactional
	public void restore(Long id) {
		Product product = productService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Product not found"));
		product.restore();
		productService.save(product);
	}
	
	private Product getProduct(Long id) {
		return productService.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new IllegalArgumentException("Product not found"));
	}
	
	private String buildImageUrl(Long productId, String fileName) {
		return publicUrlPrefix + "/uploads/products/" + productId + "/" + fileName;
	}
}
