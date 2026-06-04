package vn.hn.hninventoryservice.dao.service;

import vn.hn.hninventoryservice.dao.model.InventoryItem;

import java.util.List;
import java.util.Optional;

public interface InventoryService {
	InventoryItem save(InventoryItem item);
	
	List<InventoryItem> findByDeletedFalseOrderByIdDesc();
	
	Optional<InventoryItem> findByIdAndDeletedFalse(Long id);
	
	Optional<InventoryItem> findByProductIdAndDeletedFalse(Long productId);
	
	Optional<InventoryItem> findById(Long id);
}
