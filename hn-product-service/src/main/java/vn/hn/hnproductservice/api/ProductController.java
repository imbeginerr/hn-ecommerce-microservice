package vn.hn.hnproductservice.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.hn.hncommonservice.annotation.RequirePermission;
import vn.hn.hncommonservice.entity.ApiResponse;
import vn.hn.hnproductservice.business.ProductBusiness;
import vn.hn.hnproductservice.data.request.ProductRequest;
import vn.hn.hnproductservice.data.response.ProductResponse;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductBusiness productBusiness;
	
	@GetMapping("/public")
	public ApiResponse<List<ProductResponse>> findPublicProducts(
			@RequestParam(required = false) String keyword,
			@RequestParam(required = false) String category
	) {
		return ApiResponse.success(productBusiness.findPublicProducts(keyword, category));
	}
	
	@GetMapping("/public/{id}")
	public ApiResponse<ProductResponse> findPublicById(@PathVariable Long id) {
		return ApiResponse.success(productBusiness.findById(id));
	}
	
	@GetMapping
	@RequirePermission(name = "ProductList", description = "List products")
	@PreAuthorize("hasAuthority('ProductList')")
	public ApiResponse<List<ProductResponse>> findAll() {
		return ApiResponse.success(productBusiness.findAll());
	}
	
	@GetMapping("/{id}")
	@RequirePermission(name = "ProductDetail", description = "View product detail")
	@PreAuthorize("hasAnyAuthority('ProductDetail', 'OrderCreate', 'ROLE_USER')")
	public ApiResponse<ProductResponse> findById(@PathVariable Long id) {
		return ApiResponse.success(productBusiness.findById(id));
	}
	
	@PostMapping
	@RequirePermission(name = "ProductCreate", description = "Create product")
	@PreAuthorize("hasAuthority('ProductCreate')")
	public ApiResponse<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
		return ApiResponse.created(productBusiness.create(request));
	}
	
	@PutMapping("/{id}")
	@RequirePermission(name = "ProductUpdate", description = "Update product")
	@PreAuthorize("hasAuthority('ProductUpdate')")
	public ApiResponse<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
		return ApiResponse.success(productBusiness.update(id, request));
	}
	
	@PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@RequirePermission(name = "ProductUpdate", description = "Upload product images")
	@PreAuthorize("hasAuthority('ProductUpdate')")
	public ApiResponse<ProductResponse> uploadImages(@PathVariable Long id, @RequestPart("images") List<MultipartFile> images) {
		return ApiResponse.success(productBusiness.uploadImages(id, images));
	}

	@DeleteMapping("/{id}/images/{imageId}")
	@RequirePermission(name = "ProductUpdate", description = "Delete product image")
	@PreAuthorize("hasAuthority('ProductUpdate')")
	public ApiResponse<ProductResponse> deleteImage(@PathVariable Long id, @PathVariable Long imageId) {
		return ApiResponse.success(productBusiness.deleteImage(id, imageId));
	}
	
	@DeleteMapping("/{id}")
	@RequirePermission(name = "ProductDelete", description = "Delete product")
	@PreAuthorize("hasAuthority('ProductDelete')")
	public ApiResponse<Void> delete(@PathVariable Long id) {
		productBusiness.delete(id);
		return ApiResponse.deleted();
	}
	
	@PostMapping("/{id}/restore")
	@RequirePermission(name = "ProductUpdate", description = "Restore product")
	@PreAuthorize("hasAuthority('ProductUpdate')")
	public ApiResponse<Void> restore(@PathVariable Long id) {
		productBusiness.restore(id);
		return ApiResponse.success();
	}
}
