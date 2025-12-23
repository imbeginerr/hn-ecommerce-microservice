package vn.hn.hncoreservice.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.hn.hncommonservice.entity.ApiResponse;
import vn.hn.hncoreservice.business.AuthBusiness;
import vn.hn.hncoreservice.data.request.AuthenticationRequest;
import vn.hn.hncoreservice.data.request.IntrospectRequest;
import vn.hn.hncoreservice.data.request.LogOutRequest;
import vn.hn.hncoreservice.data.request.RefreshTokenRequest;
import vn.hn.hncoreservice.data.response.AuthenticationResponse;
import vn.hn.hncoreservice.data.response.IntrospectRespone;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthBusiness authBusiness;
	
	@PostMapping("/token")
	public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
		return ApiResponse.success(authBusiness.authenticate(request));
	}
	
	@PostMapping("/refresh-token")
	public ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
		return ApiResponse.success(authBusiness.refreshToken(request));
	}
	
	@PostMapping("/introspect")
	public ApiResponse<IntrospectRespone> introspect(@RequestBody IntrospectRequest request) {
		return ApiResponse.success(authBusiness.introspect(request));
	}
	
	@PostMapping("/logout")
	public ApiResponse<Void> logOut(
			@RequestHeader(value = "Authorization", required = false) String authHeader,
			@RequestBody LogOutRequest request
	                               ) {
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String accessToken = authHeader.substring(7);
			request.setAccessToken(accessToken);
		}
		
		authBusiness.logOut(request);
		return ApiResponse.success();
	}
}