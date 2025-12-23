package vn.hn.hncoreservice.business;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import vn.hn.hncommonservice.config.JwtService;
import vn.hn.hncommonservice.exception.AppException;
import vn.hn.hncommonservice.exception.ErrorCode;
import vn.hn.hncoreservice.dao.model.InvalidatedToken;
import vn.hn.hncoreservice.dao.model.User;
import vn.hn.hncoreservice.dao.service.Iml.InvalidatedTokenServiceIml;
import vn.hn.hncoreservice.dao.service.UserService;
import vn.hn.hncoreservice.data.request.AuthenticationRequest;
import vn.hn.hncoreservice.data.request.IntrospectRequest;
import vn.hn.hncoreservice.data.request.LogOutRequest;
import vn.hn.hncoreservice.data.request.RefreshTokenRequest;
import vn.hn.hncoreservice.data.response.AuthenticationResponse;
import vn.hn.hncoreservice.data.response.IntrospectRespone;

import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthBusiness {
	
	private final UserService userService;
	private final InvalidatedTokenServiceIml invalidatedTokenService;
	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;
	
	/**
	 * Login - Trả về access token + refresh token
	 */
	@Transactional
	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		// 1. Xác thực user
		User user = userService.findByUsernameAndDeletedFalse(request.getUsername())
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
		
		boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
		if (!matches) {
			throw new AppException(ErrorCode.UNAUTHENTICATED);
		}
		
		// 2. Generate tokens
		String scope = buildScope(user);
		String accessToken = jwtService.generateAccessToken(user.getUsername(), scope);
		String refreshToken = jwtService.generateRefreshToken(user.getUsername());
		
		// 3. Trả về cả 2 tokens
		AuthenticationResponse response = new AuthenticationResponse();
		response.setToken(accessToken);
		response.setRefreshToken(refreshToken);
		response.setAuthenticated(true);
		return response;
	}
	
	/**
	 * Refresh - Dùng refresh token để lấy access token mới
	 */
	@Transactional
	public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
		// 1. Verify refresh token
		DecodedJWT jwt = jwtService.verifyToken(request.getRefreshToken());
		String username = jwt.getSubject();
		
		// 2. Kiểm tra refresh token có bị invalidated không
		if (invalidatedTokenService.isTokenInvalidated(jwt.getId())) {
			log.error("Refresh token has been invalidated");
			throw new AppException(ErrorCode.TOKEN_INVALID);
		}
		
		// 3. Lấy user và generate access token mới
		User user = userService.findByUsernameAndDeletedFalse(username)
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
		
		String scope = buildScope(user);
		String newAccessToken = jwtService.generateAccessToken(username, scope);
		
		// 4. Trả về access token mới, giữ nguyên refresh token
		AuthenticationResponse response = new AuthenticationResponse();
		response.setToken(newAccessToken);
		response.setRefreshToken(request.getRefreshToken()); // Same refresh token
		response.setAuthenticated(true);
		return response;
	}
	
	/**
	 * Logout - Invalidate cả access token và refresh token
	 */
	@Transactional
	public void logOut(LogOutRequest request) {
		try {
			// Invalidate access token nếu có
			if (request.getAccessToken() != null && !request.getAccessToken().isEmpty()) {
				DecodedJWT accessJwt = jwtService.verifyToken(request.getAccessToken());
				invalidateToken(accessJwt);
			}
			
			// Invalidate refresh token
			if (request.getRefreshToken() != null && !request.getRefreshToken().isEmpty()) {
				DecodedJWT refreshJwt = jwtService.verifyToken(request.getRefreshToken());
				invalidateToken(refreshJwt);
			}
			
		} catch (AppException e) {
			log.error("Logout failed: {}", e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Introspect - Kiểm tra token có hợp lệ không
	 */
	@Transactional(readOnly = true)
	public IntrospectRespone introspect(IntrospectRequest request) {
		IntrospectRespone response = new IntrospectRespone();
		
		try {
			DecodedJWT jwt = jwtService.verifyToken(request.getToken());
			
			// Check if token is invalidated
			boolean isInvalidated = invalidatedTokenService.isTokenInvalidated(jwt.getId());
			response.setValid(!isInvalidated);
			
		} catch (AppException e) {
			log.error("Token introspection failed: {}", e.getMessage());
			response.setValid(false);
		}
		
		return response;
	}
	
	// ==================== Private Helper Methods ====================
	
	private void invalidateToken(DecodedJWT jwt) {
		InvalidatedToken invalidatedToken = InvalidatedToken.builder()
				.id(jwt.getId())
				.expiryTime(jwt.getExpiresAt())
				.build();
		
		invalidatedTokenService.save(invalidatedToken);
		log.debug("Token invalidated: {}", jwt.getId());
	}
	
	private String buildScope(User user) {
		StringJoiner stringJoiner = new StringJoiner(" ");
		
		if (!CollectionUtils.isEmpty(user.getRoles())) {
			user.getRoles().forEach(role -> {
				stringJoiner.add(STR."ROLE_\{role.getName()}");
				if (!CollectionUtils.isEmpty(role.getPermissions())) {
					role.getPermissions().forEach(permission -> {
						stringJoiner.add(permission.getName());
					});
				}
			});
		}
		
		return stringJoiner.toString();
	}
}