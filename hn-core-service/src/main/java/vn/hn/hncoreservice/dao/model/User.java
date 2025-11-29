package vn.hn.hncoreservice.dao.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import vn.hn.hncommonservice.entity.BaseEntity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "core_user")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NonNull
	@Column(name = "username", unique = true, nullable = false)
	private String username;
	
	@NonNull
	@Column(name = "password", nullable = false)
	private String password;
	
	@NonNull
	@Column(name = "fullName")
	private String fullName;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Column(name = "dob")
	private LocalDate dob;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "core_user_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_name")
	)
	private Set<Role> roles = new HashSet<>();
}
