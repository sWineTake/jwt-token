package com.jwt.server.configuration.filter;

import com.jwt.server.configuration.auth.PrincipalDetailsService;
import com.jwt.server.configuration.jwt.JwtAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class SecurityFilter implements Filter {
	@Autowired
	private JwtAuthenticationProvider tokenProvider;

	@Autowired
	private PrincipalDetailsService principalDetailsService;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) servletRequest;
		HttpServletResponse res = (HttpServletResponse) servletResponse;

		String jwt = getJwtFromRequest(req);
		if (StringUtils.isNotBlank(jwt) && tokenProvider.validateToken(jwt)) {
			String userId = tokenProvider.getUserInfoJWT(jwt);
			UserDetails userDetails = principalDetailsService.loadUserByUsername(userId);

			if (userDetails != null) {
				UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else {
				SecurityContextHolder.getContext().setAuthentication(null);
			}
		}

		if (filterChain != null && req != null && res != null) {
			filterChain.doFilter(req, res);
		}
	}

	public String getJwtFromRequest(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (StringUtils.isNotBlank(token) && token.startsWith("Bearer ")) {
			token = token.replace("Bearer ", "");
		}
		return token;
	}

}
