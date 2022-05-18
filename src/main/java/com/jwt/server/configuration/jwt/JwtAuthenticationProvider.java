package com.jwt.server.configuration.jwt;

import com.jwt.server.configuration.auth.PrincipalDetails;
import com.jwt.server.model.security.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음.
// /login 요청해서 username, password를 전송하면
// UsernamePasswordAuthenticationFilter동작 함
@Slf4j
@Component
public class JwtAuthenticationProvider {

	@Value("${jwtExpirationDate}")
	private long jwtExpirationInMs;

	@Value("${jwtSecretCode}")
	private String jwtSecret;

	public String generateTokenBy(PrincipalDetails principalDetails) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

		// 토큰에 넣을 회원 정보
		Map<String, Object> memberInfo = new HashMap<>();
		User user = principalDetails.getUser();
		memberInfo.put("memberId", user.getUsername());

		return Jwts
				.builder()
				.setSubject(user.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(expiryDate)
				.claim("memberInfo", memberInfo)
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException ex) {
			log.error("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			log.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			log.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			log.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			log.error("JWT claims string is empty.");
		}
		return false;
	}

	public String getUserInfoJWT(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}
}
