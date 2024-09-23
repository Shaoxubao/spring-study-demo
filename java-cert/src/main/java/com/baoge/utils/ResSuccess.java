package com.baoge.utils;

public enum ResSuccess {
	SYS_200(200, "操作成功");

	private int code;
	private String message;

	private ResSuccess(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}
}
