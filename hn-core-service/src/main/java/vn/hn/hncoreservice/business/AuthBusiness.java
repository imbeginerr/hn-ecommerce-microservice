package vn.hn.hncoreservice.business;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import vn.hn.hncommonservice.exception.AppException;
import vn.hn.hncommonservice.exception.ErrorCode;
import vn.hn.hncoreservice.dao.model.User;
import vn.hn.hncoreservice.dao.service.UserService;
import vn.hn.hncoreservice.data.request.AuthenticationRequest;
import vn.hn.hncoreservice.data.request.IntrospectRequest;
import vn.hn.hncoreservice.data.request.LogOutRequest;
import vn.hn.hncoreservice.data.response.AuthenticationResponse;
import vn.hn.hncoreservice.data.response.IntrospectRespone;

import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthBusiness {
	
	private final UserService userService;
	
	@NonFinal
	@Value("${jwt.signer-key}")
	private String JWT_SECRET;
	
	@NonFinal
	@Value("${jwt.jwt-expiration}")
	private long JWT_EXPIRATIONTIME;
	
	private final PasswordEncoder passwordEncoder;
	
	@Transactional
	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		User user = userService.findByUsernameAndDeletedFalse(request.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
		boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
		if (!matches) {
			throw new AppException(ErrorCode.UNAUTHENTICATED);
		}
		String token = genarateToken(user);
		AuthenticationResponse authenticationResponse = new AuthenticationResponse();
		authenticationResponse.setToken(token);
		authenticationResponse.setAuthenticated(true);
		return authenticationResponse;
	}
	
	@Transactional
	protected String genarateToken(User user) {
		Date now = new Date();
		return JWT.create()
				.withIssuer("hn-ecommerce")
				.withSubject(user.getUsername())
				.withClaim("scope", buildScope(user))
				.withIssuedAt(now)
				.withJWTId(UUID.randomUUID().toString())
				.withExpiresAt(new Date(System.currentTimeMillis() + JWT_EXPIRATIONTIME))
				.sign(Algorithm.HMAC256(JWT_SECRET));
	}
	
	public IntrospectRespone introspect(IntrospectRequest introspectRequest) {
		IntrospectRespone introspectRespone = new IntrospectRespone();
		try {
			verifyToken(introspectRequest.getToken());
			introspectRespone.setValid(true);
			return introspectRespone;
		} catch (JWTVerificationException e) {
			introspectRespone.setValid(false);
			return introspectRespone;
		}
	}
	
	private JWTVerifier verifyToken(String token) {
		JWTVerifier verifier = JWT.require(Algorithm.HMAC256(JWT_SECRET)).build();
		verifier.verify(token);
		return verifier;
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
				} else {
					log.warn(" Role {} has no permissions!", role.getName());
				}
			});
		} else {
			log.warn("User {} has no roles!", user.getUsername());
		}
		
		return stringJoiner.toString();
	}
	
	public void logOut(LogOutRequest token) {
		AuthenticationResponse authenticationResponse = new AuthenticationResponse();
		
	}
}
