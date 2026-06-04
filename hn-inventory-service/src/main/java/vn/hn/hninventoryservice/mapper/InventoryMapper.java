package vn.hn.hninventoryservice.mapper;

import org.springframework.stereotype.Component;
import vn.hn.hninventoryservice.dao.model.InventoryItem;
import vn.hn.hninventoryservice.data.request.InventoryRequest;
import vn.hn.hninventoryservice.data.response.InventoryResponse;

@Component
public class InventoryMapper {
	
	public InventoryItem toInventoryItem(InventoryRequest request) {
		InventoryItem item = new InventoryItem();
		updateInventoryItem(item, request);
		return item;
	}
	
	public void updateInventoryItem(InventoryItem item, InventoryRequest request) {
		item.setProductId(request.getProductId());
		item.setSku(request.getSku());
		item.setQuantity(request.getQuantity());
		item.setReservedQuantity(request.getReservedQuantity() == null ? 0 : request.getReservedQuantity());
		item.setWarehouseLocation(request.getWarehouseLocation());
	}
	
	public InventoryResponse toResponse(InventoryItem item) {
		return InventoryResponse.builder()
				.id(item.getId())
				.productId(item.getProductId())
				.sku(item.getSku())
				.quantity(item.getQuantity())
				.reservedQuantity(item.getReservedQuantity())
				.availableQuantity(item.getQuantity() - item.getReservedQuantity())
				.warehouseLocation(item.getWarehouseLocation())
				.build();
	}
}
