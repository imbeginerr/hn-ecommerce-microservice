package vn.hn.hncoreservice.dao.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import vn.hn.hncommonservice.entity.BaseEntity;

@Entity
@Table(name = "core_permission")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Permission extends BaseEntity {
	
	@Id
	private String name;
	
	@Column(name = "decription")
	private String decription;
	
}
