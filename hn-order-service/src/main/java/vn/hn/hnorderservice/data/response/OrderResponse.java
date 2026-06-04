package vn.hn.hnorderservice.data.response;

import lombok.Builder;
import lombok.Data;
import vn.hn.hnorderservice.dao.model.OrderStatus;
import vn.hn.hnorderservice.dao.model.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class OrderResponse {
	private Long id;
	private String orderNumber;
	private String customerUsername;
	private PaymentMethod paymentMethod;
	private OrderStatus status;
	private BigDecimal totalAmount;
	private List<OrderItemResponse> items;
}
