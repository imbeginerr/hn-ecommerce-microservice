package vn.hn.hncoreservice.dao.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hn.hncommonservice.entity.BaseEntity;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "core_role")
@Getter
@Setter
@NoArgsConstructor
public class Role extends BaseEntity {
	
	@Id
	@Column(name = "name")
	private String name;
	
	@Column(name = "decription")
	private String decription;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "core_role_permissions",
			joinColumns = @JoinColumn(name = "role_name"),
			inverseJoinColumns = @JoinColumn(name = "permission_name")
	)
	private Set<Permission> permissions = new HashSet<>();
	
}
