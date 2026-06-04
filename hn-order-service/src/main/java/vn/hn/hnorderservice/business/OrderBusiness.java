package vn.hn.hnorderservice.business;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hn.hnorderservice.client.InventoryClient;
import vn.hn.hnorderservice.client.ProductClient;
import vn.hn.hnorderservice.dao.model.CustomerOrder;
import vn.hn.hnorderservice.dao.model.CustomerOrderItem;
import vn.hn.hnorderservice.dao.service.CustomerOrderService;
import vn.hn.hnorderservice.data.request.OrderItemRequest;
import vn.hn.hnorderservice.data.request.OrderRequest;
import vn.hn.hnorderservice.data.request.OrderStatusRequest;
import vn.hn.hnorderservice.data.request.OrderUpdateRequest;
import vn.hn.hnorderservice.data.response.OrderResponse;
import vn.hn.hnorderservice.mapper.OrderMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderBusiness {
	
	private final CustomerOrderService orderService;
	private final ProductClient productClient;
	private final InventoryClient inventoryClient;
	private final OrderMapper orderMapper;
	
	@Transactional(readOnly = true)
	public List<OrderResponse> findAll() {
		return orderService.findByDeletedFalseOrderByIdDesc()
				.stream()
				.map(orderMapper::toResponse)
				.toList();
	}
	
	@Transactional(readOnly = true)
	public OrderResponse findById(Long id) {
		return orderMapper.toResponse(getOrder(id));
	}
	
	@Transactional
	public OrderResponse create(OrderRequest request) {
		CustomerOrder order = new CustomerOrder();
		order.setOrderNumber("ORD-" + Instant.now().toEpochMilli());
		order.setCustomerUsername(request.getCustomerUsername());
		order.setPaymentMethod(request.getPaymentMethod());
		
		for (OrderItemRequest itemRequest : request.getItems()) {
			ProductClient.ProductResponse product = productClient.findById(itemRequest.getProductId());
			inventoryClient.reserve(itemRequest.getProductId(), itemRequest.getQuantity());
			
			CustomerOrderItem item = new CustomerOrderItem();
			item.setOrder(order);
			item.setProductId(product.getId());
			item.setSku(product.getSku());
			item.setProductName(product.getName());
			item.setQuantity(itemRequest.getQuantity());
			item.setTransactionType(itemRequest.getTransactionType());
			item.setUnitPrice(product.getPrice());
			item.setLineTotal(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
			
			order.getItems().add(item);
			order.setTotalAmount(order.getTotalAmount().add(item.getLineTotal()));
		}
		
		return orderMapper.toResponse(orderService.save(order));
	}
	
	@Transactional
	public OrderResponse update(Long id, OrderUpdateRequest request) {
		CustomerOrder order = getOrder(id);
		if (request.getCustomerUsername() != null) {
			order.setCustomerUsername(request.getCustomerUsername());
		}
		if (request.getStatus() != null) {
			order.setStatus(request.getStatus());
		}
		return orderMapper.toResponse(orderService.save(order));
	}
	
	@Transactional
	public OrderResponse updateStatus(Long id, OrderStatusRequest request) {
		CustomerOrder order = getOrder(id);
		order.setStatus(request.getStatus());
		return orderMapper.toResponse(orderService.save(order));
	}
	
	@Transactional
	public void delete(Long id) {
		CustomerOrder order = getOrder(id);
		order.softDelete();
		orderService.save(order);
	}
	
	@Transactional
	public void restore(Long id) {
		CustomerOrder order = orderService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Order not found"));
		order.restore();
		orderService.save(order);
	}
	
	private CustomerOrder getOrder(Long id) {
		return orderService.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new IllegalArgumentException("Order not found"));
	}
	
}
