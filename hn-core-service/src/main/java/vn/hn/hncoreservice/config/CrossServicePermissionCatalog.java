package vn.hn.hncoreservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.hn.hncoreservice.dao.model.Permission;
import vn.hn.hncoreservice.dao.service.PermissionService;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CrossServicePermissionCatalog {
	
	private final PermissionService permissionService;
	
	@Bean
	public ApplicationRunner registerCrossServicePermissions() {
		return args -> List.of(
				permission("ProductList", "List products"),
				permission("ProductDetail", "View product detail"),
				permission("ProductCreate", "Create product"),
				permission("ProductUpdate", "Update product"),
				permission("ProductDelete", "Delete product"),
				permission("InventoryList", "List inventory"),
				permission("InventoryDetail", "View inventory detail"),
				permission("InventoryCreate", "Create inventory item"),
				permission("InventoryUpdate", "Update inventory item"),
				permission("InventoryDelete", "Delete inventory item"),
				permission("OrderList", "List orders"),
				permission("OrderDetail", "View order detail"),
				permission("OrderCreate", "Create order"),
				permission("OrderUpdate", "Update order"),
				permission("OrderDelete", "Delete order")
		).forEach(this::saveIfMissing);
	}
	
	private Permission permission(String name, String description) {
		Permission permission = new Permission();
		permission.setName(name);
		permission.setDecription(description);
		return permission;
	}
	
	private void saveIfMissing(Permission permission) {
		permissionService.findByName(permission.getName())
				.ifPresentOrElse(existing -> {
					if (!permission.getDecription().equals(existing.getDecription())) {
						existing.setDecription(permission.getDecription());
						permissionService.save(existing);
					}
				}, () -> permissionService.save(permission));
	}
}
