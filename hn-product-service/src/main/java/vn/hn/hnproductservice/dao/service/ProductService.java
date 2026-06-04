package vn.hn.hnproductservice.dao.service;

import vn.hn.hnproductservice.dao.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
	Product save(Product product);
	
	List<Product> findByDeletedFalseOrderByIdDesc();
	
	List<Product> findPublicProducts();
	
	List<Product> searchPublicProducts(String keyword);
	
	List<Product> findPublicProductsByCategory(String category);
	
	Optional<Product> findByIdAndDeletedFalse(Long id);
	
	Optional<Product> findById(Long id);
	
	boolean existsBySkuAndDeletedFalse(String sku);
}
