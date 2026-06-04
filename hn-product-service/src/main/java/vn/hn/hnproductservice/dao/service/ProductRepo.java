package vn.hn.hnproductservice.dao.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.hn.hnproductservice.dao.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Long> {
	List<Product> findByDeletedFalseOrderByIdDesc();
	
	List<Product> findByDeletedFalseAndActiveTrueOrderByIdDesc();
	
	List<Product> findByDeletedFalseAndActiveTrueAndNameContainingIgnoreCaseOrderByIdDesc(String name);
	
	@Query("""
			select p from Product p
			where p.deleted = false
			  and p.active = true
			  and (
			    lower(p.name) like lower(concat('%', :keyword, '%'))
			    or lower(coalesce(p.category, '')) like lower(concat('%', :keyword, '%'))
			  )
			order by p.id desc
			""")
	List<Product> searchPublicProducts(@Param("keyword") String keyword);
	
	List<Product> findByDeletedFalseAndActiveTrueAndCategoryIgnoreCaseOrderByIdDesc(String category);
	
	Optional<Product> findByIdAndDeletedFalse(Long id);
	
	boolean existsBySkuAndDeletedFalse(String sku);
}
