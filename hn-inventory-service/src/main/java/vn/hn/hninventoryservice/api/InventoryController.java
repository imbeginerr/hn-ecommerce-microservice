package vn.hn.hninventoryservice.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.hn.hncommonservice.annotation.RequirePermission;
import vn.hn.hncommonservice.entity.ApiResponse;
import vn.hn.hninventoryservice.business.InventoryBusiness;
import vn.hn.hninventoryservice.data.request.InventoryRequest;
import vn.hn.hninventoryservice.data.request.InventoryReserveRequest;
import vn.hn.hninventoryservice.data.response.InventoryResponse;

import java.util.List;

@RestController
@RequestMapping("/inventories")
@RequiredArgsConstructor
public class InventoryController {
	
	private final InventoryBusiness inventoryBusiness;
	
	@GetMapping
	@RequirePermission(name = "InventoryList", description = "List inventory")
	@PreAuthorize("hasAuthority('InventoryList')")
	public ApiResponse<List<InventoryResponse>> findAll() {
		return ApiResponse.success(inventoryBusiness.findAll());
	}
	
	@GetMapping("/{id}")
	@RequirePermission(name = "InventoryDetail", description = "View inventory detail")
	@PreAuthorize("hasAuthority('InventoryDetail')")
	public ApiResponse<InventoryResponse> findById(@PathVariable Long id) {
		return ApiResponse.success(inventoryBusiness.findById(id));
	}
	
	@PostMapping
	@RequirePermission(name = "InventoryCreate", description = "Create inventory item")
	@PreAuthorize("hasAuthority('InventoryCreate')")
	public ApiResponse<InventoryResponse> create(@Valid @RequestBody InventoryRequest request) {
		return ApiResponse.created(inventoryBusiness.create(request));
	}
	
	@PutMapping("/{id}")
	@RequirePermission(name = "InventoryUpdate", description = "Update inventory item")
	@PreAuthorize("hasAuthority('InventoryUpdate')")
	public ApiResponse<InventoryResponse> update(@PathVariable Long id, @Valid @RequestBody InventoryRequest request) {
		return ApiResponse.success(inventoryBusiness.update(id, request));
	}
	
	@PostMapping("/reserve")
	@RequirePermission(name = "InventoryUpdate", description = "Reserve inventory item")
	@PreAuthorize("hasAnyAuthority('InventoryUpdate', 'OrderCreate', 'ROLE_USER')")
	public ApiResponse<InventoryResponse> reserve(@Valid @RequestBody InventoryReserveRequest request) {
		return ApiResponse.success(inventoryBusiness.reserve(request));
	}
	
	@DeleteMapping("/{id}")
	@RequirePermission(name = "InventoryDelete", description = "Delete inventory item")
	@PreAuthorize("hasAuthority('InventoryDelete')")
	public ApiResponse<Void> delete(@PathVariable Long id) {
		inventoryBusiness.delete(id);
		return ApiResponse.deleted();
	}
	
	@PostMapping("/{id}/restore")
	@RequirePermission(name = "InventoryUpdate", description = "Restore inventory item")
	@PreAuthorize("hasAuthority('InventoryUpdate')")
	public ApiResponse<Void> restore(@PathVariable Long id) {
		inventoryBusiness.restore(id);
		return ApiResponse.success();
	}
}
