package vn.hn.hnorderservice.dao.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.hn.hncommonservice.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer_order")
@Getter
@Setter
public class CustomerOrder extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "order_number", nullable = false, unique = true)
	private String orderNumber;
	
	@Column(name = "customer_username")
	private String customerUsername;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "payment_method", nullable = false)
	private PaymentMethod paymentMethod = PaymentMethod.CASH;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private OrderStatus status = OrderStatus.PENDING;
	
	@Column(name = "total_amount", nullable = false)
	private BigDecimal totalAmount = BigDecimal.ZERO;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CustomerOrderItem> items = new ArrayList<>();
}
