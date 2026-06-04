package vn.hn.hnorderservice.data.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import vn.hn.hnorderservice.dao.model.PaymentMethod;

import java.util.List;

@Data
public class OrderRequest {
	private String customerUsername;
	private PaymentMethod paymentMethod = PaymentMethod.CASH;
	
	@Valid
	@NotEmpty
	private List<OrderItemRequest> items;
}
