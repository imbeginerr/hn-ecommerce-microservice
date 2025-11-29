package vn.hn.hncoreservice.config;

import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.hn.hncoreservice.dao.model.Permission;
import vn.hn.hncoreservice.dao.model.Role;
import vn.hn.hncoreservice.dao.model.User;
import vn.hn.hncoreservice.dao.service.PermissionService;
import vn.hn.hncoreservice.dao.service.RoleService;
import vn.hn.hncoreservice.dao.service.UserService;
import vn.hn.hncoreservice.enums.Roles;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitConfig {
	
	@NonFinal
	static final String ADMIN_USER_NAME = "admin";
	
	@NonFinal
	static final String ADMIN_PASSWORD = "admin";
	
	private final PermissionService permissionService;
	private final RoleService roleService;
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	
	@Bean
	public ApplicationRunner initializeData() {
		return args -> {
			log.info("Initializing default roles and admin user...");
			
			initAdminRole();
			initUserRole();
			initAdminUser();
			
			log.info(" Data initialization completed");
		};
	}
	
	private void initAdminRole() {
		if (roleService.findByName(Roles.ADMIN.getValue()).isEmpty()) {
			Set<Permission> allPermissions = new HashSet<>(permissionService.findAll());
			log.info("Created ADMIN role with {} permissions", allPermissions);
			Role adminRole = new Role();
			adminRole.setName(Roles.ADMIN.getValue());
			adminRole.setPermissions(allPermissions);
			adminRole.setDecription("Full system access");
			roleService.save(adminRole);
			log.info("Created ADMIN role with {} permissions", allPermissions.size());
		}
	}
	
	private void initUserRole() {
		if (roleService.findByName(Roles.USER.getValue()).isEmpty()) {
			Set<Permission> userPermissions = new HashSet<>();
			
			addPermissionIfExists(userPermissions);
			
			Role userRole = new Role();
			userRole.setName(Roles.USER.getValue());
			userRole.setPermissions(userPermissions);
			userRole.setDecription("Basic user access");
			roleService.save(userRole);
			log.info("Created USER role");
		}
	}
	
	private void initAdminUser() {
		if (userService.findByUsername("admin").isEmpty()) {
			Role adminRole = roleService.findByName(Roles.ADMIN.getValue())
					.orElseThrow(() -> new RuntimeException("ADMIN role not found"));
			
			User admin = new User();
			admin.setUsername(ADMIN_USER_NAME);
			admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
			admin.setFullName("Administrator");
			admin.setRoles(Set.of(adminRole));
			userService.save(admin);
			log.warn("Created admin user: admin / admin");
		}
	}
	
	private void addPermissionIfExists(Set<Permission> permissions) {
		permissionService.findByName("CoreUserDetail").ifPresent(permissions::add);
	}
}
