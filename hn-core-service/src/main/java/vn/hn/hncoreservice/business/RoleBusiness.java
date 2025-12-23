package vn.hn.hncoreservice.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hn.hncommonservice.utils.CoreUtils;
import vn.hn.hncoreservice.dao.model.Role;
import vn.hn.hncoreservice.dao.service.PermissionService;
import vn.hn.hncoreservice.dao.service.RoleService;
import vn.hn.hncoreservice.data.criteria.RoleSearchCriteria;
import vn.hn.hncoreservice.data.request.RoleRequest;
import vn.hn.hncoreservice.data.response.RoleResponse;
import vn.hn.hncoreservice.mapper.RoleMapper;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleBusiness {
	
	private final RoleService roleService;
	private final RoleMapper roleMapper;
	private final PermissionService permissionService;
	
	@Transactional
	public RoleResponse create(RoleRequest request) {
		
		var role = roleMapper.toRole(request);
		
		var permissions = permissionService.findAllByNameIn(request.getPermissions());
		role.setPermissions(new HashSet<>(permissions));
		
		role = roleService.save(role);
		return roleMapper.toRoleResponse(role);
	}
	
	@Transactional
	public RoleResponse update(String name, RoleRequest request) {
		
		roleService.findByNameAndDeletedFalse(name)
				.orElseThrow(() -> new RuntimeException(STR."Không tìm thấy role với name: \{name}"));
		
		Role role = roleMapper.toRole(request);
		
		Role updatedRole = roleService.save(role);
		
		return roleMapper.toRoleResponse(updatedRole);
	}
	
	@Transactional(readOnly = true)
	public RoleResponse findByName(String name) {
		
		Role role = roleService.findByNameAndDeletedFalse(name)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy role với name: " + name));
		
		return roleMapper.toRoleResponse(role);
	}
	
	@Transactional(readOnly = true)
	public Page<RoleResponse> findAll(RoleSearchCriteria criteria) {
		Page<Role> pageRole = roleService.findAll(criteria,
				CoreUtils.getPageRequest(criteria.getPage(), criteria.getSize(), criteria.getSortBy(), criteria.getSortDir()));
		return pageRole.map(roleMapper::toRoleResponse);
	}
	
	@Transactional
	public void delete(String name) {
		
		Role role = roleService.findByNameAndDeletedFalse(name)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy role với name: " + name));
		
		role.softDelete();
		roleService.save(role);
	}
	
	@Transactional
	public void restore(String name) {
		Role role = roleService.findByName(name)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy role với name: " + name));
		
		role.restore();
		roleService.save(role);
		
	}
	
}
