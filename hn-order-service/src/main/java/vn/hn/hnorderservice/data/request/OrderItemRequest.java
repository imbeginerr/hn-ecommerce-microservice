package vn.hn.hnorderservice.data.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import vn.hn.hnorderservice.dao.model.TransactionType;

@Data
public class OrderItemRequest {
	
	@NotNull
	private Long productId;
	
	@NotNull
	@Min(1)
	private Integer quantity;
	
	private TransactionType transactionType = TransactionType.INCOME;
}
