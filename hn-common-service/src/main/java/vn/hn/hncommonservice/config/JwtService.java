package vn.hn.hncommonservice.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.hn.hncommonservice.exception.AppException;
import vn.hn.hncommonservice.exception.ErrorCode;

import java.util.Date;
import java.util.UUID;

/**
 * JWT Service - CHUẨN AUTH0 Generate và verify JWT tokens theo đúng chuẩn RFC 7519
 */
@Service
@Slf4j
public class JwtService {
	
	@NonFinal
	@Value("${jwt.signer-key}")
	private String JWT_SECRET;
	
	@NonFinal
	@Value("${jwt.issuer:hn-ecommerce}")
	private String ISSUER;
	
	@NonFinal
	@Value("${jwt.access-expiration:3600000}") // 1 hour = 3600000ms
	private long ACCESS_EXPIRATION;
	
	@NonFinal
	@Value("${jwt.refresh-expiration:2592000000}") // 30 days = 2592000000ms
	private long REFRESH_EXPIRATION;
	
	/**
	 * Generate Access Token Claims: iss, sub, iat, exp, jti, nbf, scope
	 */
	public String generateAccessToken(String username, String scope) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + ACCESS_EXPIRATION);
		
		return JWT.create()
				.withIssuer(ISSUER)                     // Required
				.withSubject(username)                   // Required
				.withIssuedAt(now)                       // Required
				.withExpiresAt(expiration)               // Required
				.withJWTId(UUID.randomUUID().toString()) // Required
				.withNotBefore(now)                      // Recommended
				.withClaim("scope", scope)               // Custom claim
				.sign(Algorithm.HMAC256(JWT_SECRET));
	}
	
	/**
	 * Generate Refresh Token Claims: iss, sub, iat, exp, jti, nbf NO scope - refresh token chỉ dùng để lấy access token mới
	 */
	public String generateRefreshToken(String username) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + REFRESH_EXPIRATION);
		
		return JWT.create()
				.withIssuer(ISSUER)
				.withSubject(username)
				.withIssuedAt(now)
				.withExpiresAt(expiration)
				.withJWTId(UUID.randomUUID().toString())
				.withNotBefore(now)
				.sign(Algorithm.HMAC256(JWT_SECRET));
	}
	
	/**
	 * Verify Token - CHUẨN AUTH0 - Verify signature - Verify issuer - Check expiration - Allow 60s clock skew
	 */
	public DecodedJWT verifyToken(String token) {
		try {
			JWTVerifier verifier = JWT.require(Algorithm.HMAC256(JWT_SECRET))
					.withIssuer(ISSUER)
					.acceptLeeway(60) // Allow 60 seconds clock skew
					.build();
			
			return verifier.verify(token);
		} catch (JWTVerificationException e) {
			log.error("Token verification failed: {}", e.getMessage());
			throw new AppException(ErrorCode.UNAUTHENTICATED);
		}
	}
}