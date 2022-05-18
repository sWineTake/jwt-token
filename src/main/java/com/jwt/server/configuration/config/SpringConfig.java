package com.jwt.server.configuration.config;

import com.jwt.server.configuration.interseptor.HanlderInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringConfig implements WebMvcConfigurer  {

	/**
	 * 인터셉터 등록 및 관리
	 * */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 기본 인터셉터에 제외될 URL
		//List<String> excludePathPatterns = Arrays.asList( );

		registry.addInterceptor(getHanlderIntercepotor())
				.addPathPatterns("/**");
				// .excludePathPatterns(excludePathPatterns); // 인터셉터 예외시킬URL이 있을경우
	}

	@Bean
	public HanlderInterceptor getHanlderIntercepotor() {
		return new HanlderInterceptor();
	}

	/**
	 * Cors 크로스 도메인 체크를 위한 도메인 화이트 리스트
	 * @param registry
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // 호출 URL QueryString 경로
				.allowedOrigins("*") // 호출할 서버 URL 오리진 경로 ,로 여러개 입력가능
				// .allowedHeaders("*") // 어떤 헤더들을 허용할 것인지
				.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
				.allowCredentials(false) // 쿠키 요청을 허용한다(다른 도메인 서버에 인증하는 경우에만 사용해야하며, true 설정시 보안상 이슈가 발생할 수 있다)
				.maxAge(3600);
	}
}
