package vn.hn.hnorderservice.data.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import vn.hn.hnorderservice.dao.model.OrderStatus;

@Data
public class OrderStatusRequest {
	
	@NotNull
	private OrderStatus status;
}
