package vn.hn.hninventoryservice.dao.service.Iml;

import org.springframework.stereotype.Service;
import vn.hn.hninventoryservice.dao.model.InventoryItem;
import vn.hn.hninventoryservice.dao.service.InventoryRepo;
import vn.hn.hninventoryservice.dao.service.InventoryService;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceIml implements InventoryService {
	
	private final InventoryRepo repo;
	
	public InventoryServiceIml(InventoryRepo repo) {
		this.repo = repo;
	}
	
	@Override
	public InventoryItem save(InventoryItem item) {
		return repo.save(item);
	}
	
	@Override
	public List<InventoryItem> findByDeletedFalseOrderByIdDesc() {
		return repo.findByDeletedFalseOrderByIdDesc();
	}
	
	@Override
	public Optional<InventoryItem> findByIdAndDeletedFalse(Long id) {
		return repo.findByIdAndDeletedFalse(id);
	}
	
	@Override
	public Optional<InventoryItem> findByProductIdAndDeletedFalse(Long productId) {
		return repo.findByProductIdAndDeletedFalse(productId);
	}
	
	@Override
	public Optional<InventoryItem> findById(Long id) {
		return repo.findById(id);
	}
}
