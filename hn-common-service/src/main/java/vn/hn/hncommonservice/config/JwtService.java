//package vn.hn.hncommonservice.config;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.JWTVerifier;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import lombok.Value;
//import lombok.experimental.NonFinal;
//import vn.hn.hncommonservice.exception.AppException;
//import vn.hn.hncommonservice.exception.ErrorCode;
//
//public class JwtService {
//	@NonFinal
//	@Value("${jwt.signer-key}")
//	private String JWT_SECRET;
//
//	private static final String ISSUER = "hn-ecommerce";
//
//	public DecodedJWT verifyToken(String token) {
//		try {
//			JWTVerifier verifier = JWT.require(Algorithm.HMAC256(JWT_SECRET))
//					.withIssuer(ISSUER)
//					.build();
//			return verifier.verify(token);
//		} catch (JWTVerificationException e) {
//			log.error("Token verification failed: {}", e.getMessage());
//			throw new AppException(ErrorCode.UNAUTHENTICATED);
//		}
//	}
//}
