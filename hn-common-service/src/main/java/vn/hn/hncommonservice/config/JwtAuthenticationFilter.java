package vn.hn.hncommonservice.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.hn.hncommonservice.exception.AppException;
import vn.hn.hncommonservice.exception.ErrorCode;
import vn.hn.hncommonservice.service.IsTokenInvalidated;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private final JwtService jwtService;
	
	private final IsTokenInvalidated isTokenInvalidated;
	
	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain
	                               ) throws ServletException, IOException {
		
		try {
			String token = extractTokenFromRequest(request);
			
			if (token != null) {
				DecodedJWT decodedJWT = jwtService.verifyToken(token);
				
				String jti = decodedJWT.getId();
				if (isTokenInvalidated.isTokenInvalidated(jti)) {
					log.error("Token with JTI {} has been invalidated", jti);
					throw new AppException(ErrorCode.TOKEN_ALREADY_INVALIDATED);
				}
				
				String username = decodedJWT.getSubject();
				String scope = decodedJWT.getClaim("scope").asString();
				
				List<SimpleGrantedAuthority> authorities = parseAuthorities(scope);
				
				UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(
								username,
								null,
								authorities
						);
				
				authentication.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(request)
				                         );
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
				log.debug("Set Authentication for user: {} with authorities: {}",
						username, authorities);
			}
			
		} catch (AppException e) {
			log.error("Authentication error: {}", e.getMessage());
			SecurityContextHolder.clearContext();
			request.setAttribute("authException", e);
		} catch (Exception e) {
			log.error("Unexpected error in JWT filter", e);
			SecurityContextHolder.clearContext();
		}
		
		filterChain.doFilter(request, response);
	}
	
	private String extractTokenFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		
		return null;
	}
	
	private List<SimpleGrantedAuthority> parseAuthorities(String scope) {
		if (scope == null || scope.isEmpty()) {
			return List.of();
		}
		
		return Arrays.stream(scope.split(" "))
				.filter(s -> !s.isEmpty())
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}
}