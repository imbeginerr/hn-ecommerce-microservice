package vn.hn.hnorderservice.data.request;

import lombok.Data;
import vn.hn.hnorderservice.dao.model.OrderStatus;

@Data
public class OrderUpdateRequest {
	private String customerUsername;
	
	private OrderStatus status;
}
