package vn.hn.hnproductservice.dao.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.hn.hncommonservice.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
public class Product extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "sku", nullable = false, unique = true)
	private String sku;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "category")
	private String category;
	
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;
	
	@Column(name = "price", nullable = false)
	private BigDecimal price;
	
	@Column(name = "active", nullable = false)
	private boolean active = true;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductImage> images = new ArrayList<>();
}
