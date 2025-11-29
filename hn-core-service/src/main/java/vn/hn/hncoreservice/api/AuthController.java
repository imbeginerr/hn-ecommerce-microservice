package vn.hn.hncoreservice.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.hn.hncommonservice.entity.ApiResponse;
import vn.hn.hncoreservice.business.AuthBusiness;
import vn.hn.hncoreservice.data.request.AuthenticationRequest;
import vn.hn.hncoreservice.data.request.IntrospectRequest;
import vn.hn.hncoreservice.data.request.LogOutRequest;
import vn.hn.hncoreservice.data.response.AuthenticationResponse;
import vn.hn.hncoreservice.data.response.IntrospectRespone;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthBusiness authBusiness;
	
	@PostMapping("/token")
	ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
		return ApiResponse.success(authBusiness.authenticate(authenticationRequest));
	}
	
	@PostMapping("/introspect")
	ApiResponse<IntrospectRespone> introspect(@RequestBody IntrospectRequest introspectRequest) {
		return ApiResponse.success(authBusiness.introspect(introspectRequest));
	}
	
	@PostMapping("/logout")
	ApiResponse<Void> logOut(@RequestBody LogOutRequest logOutRequest) {
		authBusiness.logOut(logOutRequest);
		return ApiResponse.success();
	}
}
