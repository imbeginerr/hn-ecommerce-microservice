package vn.hn.hnorderservice.data.response;

import lombok.Builder;
import lombok.Data;
import vn.hn.hnorderservice.dao.model.TransactionType;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemResponse {
	private Long productId;
	private String sku;
	private String productName;
	private Integer quantity;
	private TransactionType transactionType;
	private BigDecimal unitPrice;
	private BigDecimal lineTotal;
}
