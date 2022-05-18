package com.jwt.server.configuration.auth;

import com.jwt.server.model.security.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("PrincipalDetailsService.loadUserByUsername()");
		// 받은 회원 아이디를 가지고 DB에서 유저 정보 조회 후 매핑
		User userEntity = new User();
		userEntity.setUsername(username);
		userEntity.setId(4387);
		userEntity.setPassword("");
		userEntity.setRoles("ROLE1");

		return new PrincipalDetails(userEntity);
	}
}
