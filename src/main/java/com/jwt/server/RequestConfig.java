package com.jwt.server;

import java.lang.annotation.*;

/**
 * RequestConfig
 * @author stylehosting
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestConfig {

	/**
	 * 로그인 체크
	 * @return
	 */
	boolean login() default true;

	/**
	 * 메뉴 레벨
	 * @return
	 */
	int level() default 0;
}
