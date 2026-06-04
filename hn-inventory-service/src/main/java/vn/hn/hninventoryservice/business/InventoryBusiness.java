package vn.hn.hninventoryservice.business;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hn.hninventoryservice.dao.model.InventoryItem;
import vn.hn.hninventoryservice.dao.service.InventoryService;
import vn.hn.hninventoryservice.data.request.InventoryRequest;
import vn.hn.hninventoryservice.data.request.InventoryReserveRequest;
import vn.hn.hninventoryservice.data.response.InventoryResponse;
import vn.hn.hninventoryservice.mapper.InventoryMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryBusiness {
	
	private final InventoryService inventoryService;
	private final InventoryMapper inventoryMapper;
	
	@Transactional(readOnly = true)
	public List<InventoryResponse> findAll() {
		return inventoryService.findByDeletedFalseOrderByIdDesc()
				.stream()
				.map(inventoryMapper::toResponse)
				.toList();
	}
	
	@Transactional(readOnly = true)
	public InventoryResponse findById(Long id) {
		return inventoryMapper.toResponse(getInventory(id));
	}
	
	@Transactional
	public InventoryResponse create(InventoryRequest request) {
		InventoryItem item = inventoryMapper.toInventoryItem(request);
		return inventoryMapper.toResponse(inventoryService.save(item));
	}
	
	@Transactional
	public InventoryResponse update(Long id, InventoryRequest request) {
		InventoryItem item = getInventory(id);
		inventoryMapper.updateInventoryItem(item, request);
		return inventoryMapper.toResponse(inventoryService.save(item));
	}
	
	@Transactional
	public InventoryResponse reserve(InventoryReserveRequest request) {
		InventoryItem item = inventoryService.findByProductIdAndDeletedFalse(request.getProductId())
				.orElseThrow(() -> new IllegalArgumentException("Inventory item not found"));
		
		int availableQuantity = item.getQuantity() - item.getReservedQuantity();
		if (availableQuantity < request.getQuantity()) {
			throw new IllegalArgumentException("Insufficient inventory");
		}
		
		item.setReservedQuantity(item.getReservedQuantity() + request.getQuantity());
		return inventoryMapper.toResponse(inventoryService.save(item));
	}
	
	@Transactional
	public void delete(Long id) {
		InventoryItem item = getInventory(id);
		item.softDelete();
		inventoryService.save(item);
	}
	
	@Transactional
	public void restore(Long id) {
		InventoryItem item = inventoryService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Inventory item not found"));
		item.restore();
		inventoryService.save(item);
	}
	
	private InventoryItem getInventory(Long id) {
		return inventoryService.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new IllegalArgumentException("Inventory item not found"));
	}
	
}
