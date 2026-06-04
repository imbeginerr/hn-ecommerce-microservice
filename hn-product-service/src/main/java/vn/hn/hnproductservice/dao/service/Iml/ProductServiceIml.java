package vn.hn.hnproductservice.dao.service.Iml;

import org.springframework.stereotype.Service;
import vn.hn.hnproductservice.dao.model.Product;
import vn.hn.hnproductservice.dao.service.ProductRepo;
import vn.hn.hnproductservice.dao.service.ProductService;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceIml implements ProductService {
	
	private final ProductRepo repo;
	
	public ProductServiceIml(ProductRepo repo) {
		this.repo = repo;
	}
	
	@Override
	public Product save(Product product) {
		return repo.save(product);
	}
	
	@Override
	public List<Product> findByDeletedFalseOrderByIdDesc() {
		return repo.findByDeletedFalseOrderByIdDesc();
	}
	
	@Override
	public List<Product> findPublicProducts() {
		return repo.findByDeletedFalseAndActiveTrueOrderByIdDesc();
	}
	
	@Override
	public List<Product> searchPublicProducts(String keyword) {
		return repo.searchPublicProducts(keyword);
	}
	
	@Override
	public List<Product> findPublicProductsByCategory(String category) {
		return repo.findByDeletedFalseAndActiveTrueAndCategoryIgnoreCaseOrderByIdDesc(category);
	}
	
	@Override
	public Optional<Product> findByIdAndDeletedFalse(Long id) {
		return repo.findByIdAndDeletedFalse(id);
	}
	
	@Override
	public Optional<Product> findById(Long id) {
		return repo.findById(id);
	}
	
	@Override
	public boolean existsBySkuAndDeletedFalse(String sku) {
		return repo.existsBySkuAndDeletedFalse(sku);
	}
}
