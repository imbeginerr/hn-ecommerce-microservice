package vn.hn.hncoreservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import vn.hn.hncommonservice.annotation.RequirePermission;
import vn.hn.hncoreservice.dao.model.Permission;
import vn.hn.hncoreservice.dao.service.PermissionRepo;

import java.lang.reflect.Method;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class PermissionScanner {
	
	private final ApplicationContext applicationContext;
	private final PermissionRepo permissionRepo;
	
	@Value("${spring.application.name:unknown}")
	private String applicationName;
	
	/**
	 * Tự động scan và tạo permissions khi application startup EventListener này chạy SAU KHI app đã start xong
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void scanAndRegisterPermissions() {
		log.info("Starting permission scanning for service: {}", applicationName);
		
		Set<PermissionInfo> discoveredPermissions = new HashSet<>();
		
		// Lấy tất cả beans có annotation @RestController hoặc @Controller
		Map<String, Object> controllers = new HashMap<>();
		controllers.putAll(applicationContext.getBeansWithAnnotation(RestController.class));
		controllers.putAll(applicationContext.getBeansWithAnnotation(Controller.class));
		
		// Scan từng controller
		for (Object controller : controllers.values()) {
			Class<?> controllerClass = controller.getClass();
			
			// Xử lý CGLIB proxy class
			if (controllerClass.getName().contains("$$")) {
				controllerClass = controllerClass.getSuperclass();
			}
			
			// Kiểm tra class-level annotation
			if (controllerClass.isAnnotationPresent(RequirePermission.class)) {
				RequirePermission classPermission = controllerClass.getAnnotation(RequirePermission.class);
				discoveredPermissions.add(extractPermissionInfo(classPermission));
			}
			
			// Scan methods trong controller
			for (Method method : controllerClass.getDeclaredMethods()) {
				if (method.isAnnotationPresent(RequirePermission.class)) {
					RequirePermission methodPermission = method.getAnnotation(RequirePermission.class);
					discoveredPermissions.add(extractPermissionInfo(methodPermission));
				}
			}
		}
		
		// Sync vào database
		syncPermissionsToDatabase(discoveredPermissions);
		
		log.info("Permission scanning completed. Registered {} permissions", discoveredPermissions.size());
	}
	
	private PermissionInfo extractPermissionInfo(RequirePermission annotation) {
		String permissionName = annotation.name();
		String description = annotation.description().isEmpty()
				? "Auto-generated permission: " + permissionName
				: annotation.description();
		
		return new PermissionInfo(permissionName, description);
	}
	
	/**
	 * Sync permissions vào database - Tạo mới nếu chưa tồn tại - Update description nếu thay đổi
	 */
	private void syncPermissionsToDatabase(Set<PermissionInfo> discoveredPermissions) {
		List<Permission> permissionsToSave = new ArrayList<>();
		
		for (PermissionInfo info : discoveredPermissions) {
			Optional<Permission> existingPermission = permissionRepo.findById(info.name);
			
			if (existingPermission.isEmpty()) {
				// Tạo mới permission
				Permission newPermission = new Permission();
				newPermission.setName(info.name);
				newPermission.setDecription(info.description);
				permissionsToSave.add(newPermission);
				log.info("New permission discovered: {}", info.name);
			} else {
				// Update description nếu thay đổi
				Permission existing = existingPermission.get();
				if (!info.description.equals(existing.getDecription())) {
					existing.setDecription(info.description);
					permissionsToSave.add(existing);
					log.info("Updated permission description: {}", info.name);
				}
			}
		}
		
		if (!permissionsToSave.isEmpty()) {
			permissionRepo.saveAll(permissionsToSave);
			log.info("Saved {} permissions to database", permissionsToSave.size());
		}
	}
	
	// Inner class để lưu thông tin permission tạm thời
	private record PermissionInfo(String name, String description) {
	}
}