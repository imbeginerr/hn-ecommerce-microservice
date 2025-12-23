package vn.hn.hncoreservice.api;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.hn.hncommonservice.annotation.RequirePermission;
import vn.hn.hncommonservice.entity.ApiResponse;
import vn.hn.hncoreservice.business.PermissionBusiness;
import vn.hn.hncoreservice.data.criteria.PermissionSearchCriteria;
import vn.hn.hncoreservice.data.request.PermissionRequest;
import vn.hn.hncoreservice.data.response.PermissionResponse;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionController {
	PermissionBusiness permissionBusiness;
	
	@PostMapping(value = {""})
	@RequirePermission(name = "CorePermissionCreate", description = "Create new permission")
	@PreAuthorize("hasAuthority('CorePermissionCreate')")
	public ApiResponse<PermissionResponse> create(@Valid @RequestBody PermissionRequest permissionRequest) {
		PermissionResponse permissionCreateResponse = permissionBusiness.create(permissionRequest);
		return ApiResponse.created(permissionCreateResponse);
	}
	
	@DeleteMapping(value = {"/{name}"})
	@RequirePermission(name = "CorePermissionDelete", description = "Delete permission")
	@PreAuthorize("hasAuthority('CorePermissionDelete')")
	public ApiResponse<Void> delete(@PathVariable("name") String name) throws EntityNotFoundException {
		permissionBusiness.delete(name);
		return ApiResponse.deleted();
	}
	
	@GetMapping(value = {"/", ""})
	@RequirePermission(name = "CorePermissionList", description = "List permission")
	@PreAuthorize("hasAuthority('CorePermissionList')")
	public ApiResponse<Page<PermissionResponse>> findAll(PermissionSearchCriteria criteria) {
		Page<PermissionResponse> pagePermissionResponse = permissionBusiness.findAll(criteria);
		return ApiResponse.success(pagePermissionResponse);
	}
	
	@GetMapping(value = "/{name}")
	@RequirePermission(name = "CorePermissionDetail", description = "Detail permission")
	@PreAuthorize("hasAuthority('CorePermissionDetail')")
	public ApiResponse<PermissionResponse> findById(@PathVariable("name") String name) throws EntityNotFoundException {
		PermissionResponse permissionResponse = permissionBusiness.findByName(name);
		return ApiResponse.success(permissionResponse);
	}
	
	@PutMapping(value = {"/{name}"})
	@RequirePermission(name = "CorePermissionUpdate", description = "Update permission")
	@PreAuthorize("hasAuthority('CorePermissionUpdate')")
	public ApiResponse<PermissionResponse> update(@PathVariable("name") String name,
			@Valid @RequestBody PermissionRequest permissionRequest) throws
			EntityNotFoundException {
		PermissionResponse permissionUpdateResponse = permissionBusiness.update(name, permissionRequest);
		return ApiResponse.success(permissionUpdateResponse);
	}
	
	@PostMapping(value = {"/restore/{name}"})
	@RequirePermission(name = "CorePermissionRestore", description = "Restore permission")
	@PreAuthorize("hasAuthority('CorePermissionRestore')")
	public ApiResponse<Void> update(@PathVariable("name") String name) throws
			RuntimeException {
		permissionBusiness.restore(name);
		return ApiResponse.success();
	}
}
