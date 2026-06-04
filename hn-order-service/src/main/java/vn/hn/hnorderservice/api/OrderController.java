package vn.hn.hnorderservice.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.hn.hncommonservice.annotation.RequirePermission;
import vn.hn.hncommonservice.entity.ApiResponse;
import vn.hn.hnorderservice.business.OrderBusiness;
import vn.hn.hnorderservice.data.request.OrderRequest;
import vn.hn.hnorderservice.data.request.OrderStatusRequest;
import vn.hn.hnorderservice.data.request.OrderUpdateRequest;
import vn.hn.hnorderservice.data.response.OrderResponse;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
	
	private final OrderBusiness orderBusiness;
	
	@GetMapping
	@RequirePermission(name = "OrderList", description = "List orders")
	@PreAuthorize("hasAuthority('OrderList')")
	public ApiResponse<List<OrderResponse>> findAll() {
		return ApiResponse.success(orderBusiness.findAll());
	}
	
	@GetMapping("/{id}")
	@RequirePermission(name = "OrderDetail", description = "View order detail")
	@PreAuthorize("hasAuthority('OrderDetail')")
	public ApiResponse<OrderResponse> findById(@PathVariable Long id) {
		return ApiResponse.success(orderBusiness.findById(id));
	}
	
	@PostMapping
	@RequirePermission(name = "OrderCreate", description = "Create order")
	@PreAuthorize("hasAnyAuthority('OrderCreate', 'ROLE_USER')")
	public ApiResponse<OrderResponse> create(@Valid @RequestBody OrderRequest request) {
		return ApiResponse.created(orderBusiness.create(request));
	}
	
	@PutMapping("/{id}")
	@RequirePermission(name = "OrderUpdate", description = "Update order")
	@PreAuthorize("hasAuthority('OrderUpdate')")
	public ApiResponse<OrderResponse> update(@PathVariable Long id, @Valid @RequestBody OrderUpdateRequest request) {
		return ApiResponse.success(orderBusiness.update(id, request));
	}
	
	@PutMapping("/{id}/status")
	@RequirePermission(name = "OrderUpdate", description = "Update order")
	@PreAuthorize("hasAuthority('OrderUpdate')")
	public ApiResponse<OrderResponse> updateStatus(@PathVariable Long id, @Valid @RequestBody OrderStatusRequest request) {
		return ApiResponse.success(orderBusiness.updateStatus(id, request));
	}
	
	@DeleteMapping("/{id}")
	@RequirePermission(name = "OrderDelete", description = "Delete order")
	@PreAuthorize("hasAuthority('OrderDelete')")
	public ApiResponse<Void> delete(@PathVariable Long id) {
		orderBusiness.delete(id);
		return ApiResponse.deleted();
	}
	
	@PostMapping("/{id}/restore")
	@RequirePermission(name = "OrderUpdate", description = "Restore order")
	@PreAuthorize("hasAuthority('OrderUpdate')")
	public ApiResponse<Void> restore(@PathVariable Long id) {
		orderBusiness.restore(id);
		return ApiResponse.success();
	}
}
