package vn.hn.hncoreservice.api;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vn.hn.hncommonservice.annotation.RequirePermission;
import vn.hn.hncommonservice.entity.ApiResponse;
import vn.hn.hncoreservice.business.RoleBusiness;
import vn.hn.hncoreservice.data.criteria.RoleSearchCriteria;
import vn.hn.hncoreservice.data.request.RoleRequest;
import vn.hn.hncoreservice.data.response.RoleResponse;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
	RoleBusiness roleBusiness;
	
	@PostMapping(value = {""})
	@RequirePermission(name = "CoreRoleCreate", description = "Create new role")
	@PreAuthorize("hasAuthority('CoreRoleCreate')")
	public ApiResponse<RoleResponse> create(@Valid @RequestBody RoleRequest roleRequest) {
		RoleResponse roleCreateResponse = roleBusiness.create(roleRequest);
		return ApiResponse.created(roleCreateResponse);
	}
	
	@DeleteMapping(value = {"/{name}"})
	@RequirePermission(name = "CoreRoleDelete", description = "Delete role")
	@PreAuthorize("hasAuthority('CoreRoleDelete')")
	public ApiResponse<Void> delete(@PathVariable("name") String name) throws EntityNotFoundException {
		roleBusiness.delete(name);
		return ApiResponse.deleted();
	}
	
	@GetMapping(value = {"/", ""})
	@RequirePermission(name = "CoreRoleList", description = "List role")
	@PreAuthorize("hasAuthority('CoreRoleList')")
	public ApiResponse<Page<RoleResponse>> findAll(RoleSearchCriteria criteria) {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		log.info("Rolename: {}", authentication.getName());
		authentication.getAuthorities().forEach(authority -> {
			log.info("Authorities: {}", authority.getAuthority());
		});
		Page<RoleResponse> pageRoleResponse = roleBusiness.findAll(criteria);
		return ApiResponse.success(pageRoleResponse);
	}
	
	@GetMapping(value = "/{name}")
	@RequirePermission(name = "CoreRoleDetail", description = "Detail role")
	@PreAuthorize("hasAuthority('CoreRoleDetail')")
	public ApiResponse<RoleResponse> findById(@PathVariable("name") String name) throws EntityNotFoundException {
		RoleResponse roleResponse = roleBusiness.findByName(name);
		return ApiResponse.success(roleResponse);
	}
	
	@PutMapping(value = {"/{name}"})
	@RequirePermission(name = "CoreRoleUpdate", description = "Update role")
	@PreAuthorize("hasAuthority('CoreRoleUpdate')")
	public ApiResponse<RoleResponse> update(@PathVariable("name") String name, @Valid @RequestBody RoleRequest roleRequest) throws
			EntityNotFoundException {
		RoleResponse roleUpdateResponse = roleBusiness.update(name, roleRequest);
		return ApiResponse.success(roleUpdateResponse);
	}
	
	@PostMapping(value = {"/restore/{name}"})
	@RequirePermission(name = "CoreRoleRestore", description = "Restore role")
	@PreAuthorize("hasAuthority('CoreRoleRestore')")
	public ApiResponse<Void> update(@PathVariable("name") String name) throws
			RuntimeException {
		roleBusiness.restore(name);
		return ApiResponse.success();
	}
	
}
