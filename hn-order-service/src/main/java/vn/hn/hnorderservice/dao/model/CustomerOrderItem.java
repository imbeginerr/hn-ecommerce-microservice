package vn.hn.hnorderservice.dao.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "customer_order_item")
@Getter
@Setter
public class CustomerOrderItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private CustomerOrder order;
	
	@Column(name = "product_id", nullable = false)
	private Long productId;
	
	@Column(name = "sku", nullable = false)
	private String sku;
	
	@Column(name = "product_name", nullable = false)
	private String productName;
	
	@Column(name = "quantity", nullable = false)
	private Integer quantity;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "transaction_type", nullable = false)
	private TransactionType transactionType = TransactionType.INCOME;
	
	@Column(name = "unit_price", nullable = false)
	private BigDecimal unitPrice;
	
	@Column(name = "line_total", nullable = false)
	private BigDecimal lineTotal;
}
