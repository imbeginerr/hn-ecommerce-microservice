package vn.hn.hnorderservice.mapper;

import org.springframework.stereotype.Component;
import vn.hn.hnorderservice.dao.model.CustomerOrder;
import vn.hn.hnorderservice.dao.model.CustomerOrderItem;
import vn.hn.hnorderservice.data.response.OrderItemResponse;
import vn.hn.hnorderservice.data.response.OrderResponse;

@Component
public class OrderMapper {
	
	public OrderResponse toResponse(CustomerOrder order) {
		return OrderResponse.builder()
				.id(order.getId())
				.orderNumber(order.getOrderNumber())
				.customerUsername(order.getCustomerUsername())
				.paymentMethod(order.getPaymentMethod())
				.status(order.getStatus())
				.totalAmount(order.getTotalAmount())
				.items(order.getItems().stream().map(this::toItemResponse).toList())
				.build();
	}
	
	private OrderItemResponse toItemResponse(CustomerOrderItem item) {
		return OrderItemResponse.builder()
				.productId(item.getProductId())
				.sku(item.getSku())
				.productName(item.getProductName())
				.quantity(item.getQuantity())
				.transactionType(item.getTransactionType())
				.unitPrice(item.getUnitPrice())
				.lineTotal(item.getLineTotal())
				.build();
	}
}
