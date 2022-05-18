package com.jwt.server.utils.enums;

import lombok.Getter;

@Getter
public enum ApiError {
	SUCCESS("200", "succeed"),
	ERROR("400", "error"),
	FAIL("401", "fail")
	;

	private String code;
	private String name;
	ApiError(String code, String name) {
		this.code = code;
		this.name = name;
	}
}
