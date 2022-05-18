package com.jwt.server.configuration.controller;

import com.jwt.server.CurrentUser;
import com.jwt.server.RequestConfig;
import com.jwt.server.configuration.auth.PrincipalDetails;
import com.jwt.server.configuration.auth.PrincipalDetailsService;
import com.jwt.server.configuration.jwt.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtController {

	private final PrincipalDetailsService principalDetailsService;
	private final JwtAuthenticationProvider jwtAuthenticationProvider;

	@PostMapping("/login")
	@RequestConfig(login = false, level = 0)
	public String login() {
		String memberId = "1234";
		PrincipalDetails userDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(memberId);
		String token = jwtAuthenticationProvider.generateTokenBy(userDetails);
		return token;
	}

	@PostMapping("/token/check")
	@RequestConfig(login = true, level = 1)
	public String check(@CurrentUser PrincipalDetails currentUser) {
		return currentUser.getUsername();
	}

}
