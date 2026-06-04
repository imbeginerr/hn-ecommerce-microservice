package vn.hn.hninventoryservice.dao.service;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.hn.hninventoryservice.dao.model.InventoryItem;

import java.util.List;
import java.util.Optional;

public interface InventoryRepo extends JpaRepository<InventoryItem, Long> {
	List<InventoryItem> findByDeletedFalseOrderByIdDesc();
	
	Optional<InventoryItem> findByIdAndDeletedFalse(Long id);
	
	Optional<InventoryItem> findByProductIdAndDeletedFalse(Long productId);
}
