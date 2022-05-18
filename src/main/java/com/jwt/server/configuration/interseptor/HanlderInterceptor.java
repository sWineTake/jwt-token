package com.jwt.server.configuration.interseptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwt.server.RequestConfig;
import com.jwt.server.configuration.auth.PrincipalDetails;
import com.jwt.server.configuration.filter.SecurityFilter;
import com.jwt.server.utils.ResponseData;
import com.jwt.server.utils.enums.ApiError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HanlderInterceptor implements AsyncHandlerInterceptor {

	@Autowired
	SecurityFilter securityFilter;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HandlerMethod handleMethod = null;
		if (handler instanceof HandlerMethod) {
			handleMethod = (HandlerMethod) handler;
		}

		if (!ObjectUtils.isEmpty(handleMethod)) {
			RequestConfig requestConfig = handleMethod.getMethodAnnotation(RequestConfig.class);
			PrincipalDetails jwtUser = getPrincipal();

			if (!ObjectUtils.isEmpty(requestConfig)) {
				if (requestConfig.login()) {
					if (ObjectUtils.isEmpty(jwtUser)) return redirect(response, "로그인이 필요합니다.");
					// ========= 프로세스 로직 START =========
				}

				if (requestConfig.level() > 0) {
					if (ObjectUtils.isEmpty(jwtUser)) return redirect(response, "메뉴 권한이 필요합니다.");
					// ========= 프로세스 로직 START =========
				}
			}
			else {
				// error 처리 정의 여부 필요
			}
		}
		else {
			// error 처리 정의 여부 필요
		}
		return AsyncHandlerInterceptor.super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		AsyncHandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	/**
	 * 리다이렉트 처리
	 */
	private boolean redirect(HttpServletResponse response, String msg) throws Exception{
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		ObjectMapper mapper = new ObjectMapper();
		ResponseData data = ResponseData.builder()
								.error(true)
								.apiError(ApiError.ERROR)
								.message(msg)
								.build();
		response.getWriter().write(mapper.writeValueAsString(data));
		return false;
	}

	/**
	 * JWT 토큰으로 스프링 시큐리티에서 유저 정보를 바인딩한 값으로 유저 정보 조회
	 * @return
	 */
	private PrincipalDetails getPrincipal() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal.equals("anonymousUser")) {
			return null;
		}
		return (PrincipalDetails) principal;
	}
}
