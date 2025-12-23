package vn.hn.hncoreservice.api;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.hn.hncommonservice.annotation.RequirePermission;
import vn.hn.hncommonservice.entity.ApiResponse;
import vn.hn.hncoreservice.business.UserBusiness;
import vn.hn.hncoreservice.data.criteria.UserSearchCriteria;
import vn.hn.hncoreservice.data.request.UserCreateRequest;
import vn.hn.hncoreservice.data.request.UserUpdateRequest;
import vn.hn.hncoreservice.data.response.UserCreateResponse;
import vn.hn.hncoreservice.data.response.UserResponse;
import vn.hn.hncoreservice.data.response.UserUpdateResponse;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
	
	private final UserBusiness userBusiness;
	
	@PostMapping(value = {""})
//	@RequirePermission(name = "CoreUserCreate", description = "Create new user")
//	@PreAuthorize("hasAuthority('CoreUserCreate')")
	public ApiResponse<UserCreateResponse> create(@Valid @RequestBody UserCreateRequest userCreateRequest) {
		UserCreateResponse userCreateResponse = userBusiness.create(userCreateRequest);
		return ApiResponse.created(userCreateResponse);
	}
	
	@DeleteMapping(value = {"/{id}"})
	@RequirePermission(name = "CoreUserDelete", description = "Delete user")
	@PreAuthorize("hasAuthority('CoreUserDelete')")
	public ApiResponse<Void> delete(@PathVariable("id") Long id) throws EntityNotFoundException {
		userBusiness.delete(id);
		return ApiResponse.deleted();
	}
//
//	@DeleteMapping(value = {"/ids"})
//	public R<Void> deleteByIds(@RequestParam(name = "ids") List<Long> ids) {
//		userBusiness.deleteByIds(ids);
//		return R.ok();
//	}
	
	@GetMapping(value = {"/", ""})
	@RequirePermission(name = "CoreUserList", description = "List user")
	@PreAuthorize("hasAuthority('CoreUserList')")
	public ApiResponse<Page<UserResponse>> findAll(UserSearchCriteria criteria) {
		Page<UserResponse> pageUserResponse = userBusiness.findAll(criteria);
		return ApiResponse.success(pageUserResponse);
	}
	
	@GetMapping(value = "/{id}")
	@RequirePermission(name = "CoreUserDetail", description = "Detail user")
	@PreAuthorize("hasAuthority('CoreUserDetail')")
	public ApiResponse<UserResponse> findById(@PathVariable("id") Long id) throws EntityNotFoundException {
		UserResponse userResponse = userBusiness.findById(id);
		return ApiResponse.success(userResponse);
	}
	
	//
//	@GetMapping(value = "/get")
//	public R<List<UserResponse>> getAll(@RequestParam(name = "ids", required = false) List<Long> ids) {
//		List<UserResponse> userResponses = userBusiness.getAll(ids);
//		return R.success(userResponses);
//	}
//
	
	@PutMapping(value = {"/{id}"})
	@RequirePermission(name = "CoreUserUpdate", description = "Update user")
	@PreAuthorize("hasAuthority('CoreUserUpdate')")
	public ApiResponse<UserUpdateResponse> update(@PathVariable("id") Long id, @Valid @RequestBody UserUpdateRequest userUpdateRequest) throws
			EntityNotFoundException {
		UserUpdateResponse userUpdateResponse = userBusiness.update(id, userUpdateRequest);
		return ApiResponse.success(userUpdateResponse);
	}
	
	@PostMapping(value = {"/restore/{id}"})
	@RequirePermission(name = "CoreUserRestore", description = "Restore user")
	@PreAuthorize("hasAuthority('CoreUserRestore')")
	public ApiResponse<Void> update(@PathVariable("id") Long id) throws
			RuntimeException {
		userBusiness.restore(id);
		return ApiResponse.success();
	}
	
	@GetMapping(value = {"/myifo"})
	@RequirePermission(name = "CoreUserGetMySelf", description = "GetMySelf user")
	@PreAuthorize("hasAuthority('CoreUserGetMySelf')")
	public ApiResponse<UserResponse> getMyInfo() throws
			EntityNotFoundException {
		return ApiResponse.success(userBusiness.getMyInfo());
	}
}
