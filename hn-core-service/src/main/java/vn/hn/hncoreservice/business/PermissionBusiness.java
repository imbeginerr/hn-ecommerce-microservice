package vn.hn.hncoreservice.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hn.hncommonservice.utils.CoreUtils;
import vn.hn.hncoreservice.dao.model.Permission;
import vn.hn.hncoreservice.dao.service.PermissionService;
import vn.hn.hncoreservice.data.criteria.PermissionSearchCriteria;
import vn.hn.hncoreservice.data.request.PermissionRequest;
import vn.hn.hncoreservice.data.response.PermissionResponse;
import vn.hn.hncoreservice.mapper.PermissionMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionBusiness {
	
	private final PermissionService permissionService;
	private final PermissionMapper permissionMapper;
	
	@Transactional
	public PermissionResponse create(PermissionRequest request) {
		
		Permission permission = permissionMapper.toPermission(request);
		Permission savedPermission = permissionService.save(permission);
		
		log.info("Permission created with id: {}", savedPermission.getName());
		
		return permissionMapper.toPermissionResponse(savedPermission);
	}
	
	@Transactional
	public PermissionResponse update(String name, PermissionRequest request) {
		log.info("Updating permission id: {}", name);
		
		permissionService.findByNameAndDeletedFalse(name)
				.orElseThrow(() -> new RuntimeException(STR."Không tìm thấy permission với name: \{name}"));
		
		Permission permission = permissionMapper.toPermission(request);
		
		Permission updatedPermission = permissionService.save(permission);
		
		log.info("Permission updated successfully");
		
		return permissionMapper.toPermissionResponse(updatedPermission);
	}
	
	@Transactional(readOnly = true)
	public PermissionResponse findByName(String name) {
		log.info("Fetching permission id: {}", name);
		
		Permission permission = permissionService.findByNameAndDeletedFalse(name)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy permission với name: " + name));
		
		return permissionMapper.toPermissionResponse(permission);
	}
	
	@Transactional(readOnly = true)
	public Page<PermissionResponse> findAll(PermissionSearchCriteria criteria) {
		Page<Permission> pagePermission = permissionService.findAll(criteria,
				CoreUtils.getPageRequest(criteria.getPage(), criteria.getSize(), criteria.getSortBy(), criteria.getSortDir()));
		return pagePermission.map(permissionMapper::toPermissionResponse);
	}
	
	@Transactional
	public void delete(String name) {
		log.info("Deleting permission id: {}", name);
		
		Permission permission = permissionService.findByNameAndDeletedFalse(name)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy permission với name: " + name));
		
		permission.softDelete();
		permissionService.save(permission);
		
		log.info("Permission deleted successfully");
	}
	
	@Transactional
	public void restore(String name) {
		log.info("Restoring permission id: {}", name);
		
		Permission permission = permissionService.findByName(name)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy permission với name: " + name));
		
		permission.restore();
		permissionService.save(permission);
		
		log.info("Permission restored successfully");
	}
}
