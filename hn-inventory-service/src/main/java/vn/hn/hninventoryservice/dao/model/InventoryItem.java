package vn.hn.hninventoryservice.dao.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.hn.hncommonservice.entity.BaseEntity;

@Entity
@Table(name = "inventory_item")
@Getter
@Setter
public class InventoryItem extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "product_id", nullable = false, unique = true)
	private Long productId;
	
	@Column(name = "sku", nullable = false)
	private String sku;
	
	@Column(name = "quantity", nullable = false)
	private Integer quantity = 0;
	
	@Column(name = "reserved_quantity", nullable = false)
	private Integer reservedQuantity = 0;
	
	@Column(name = "warehouse_location")
	private String warehouseLocation;
}
