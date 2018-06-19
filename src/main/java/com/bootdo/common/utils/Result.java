package com.bootdo.common.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Result extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	public Result() {
		put("code", 0);
		put("msg", "操作成功");
		put("time", new Date());
		put("data", null);
	}

	public static Result error() {
		return error(1, "操作失败");
	}

	public static Result error(String msg) {
		return error(500, msg);
	}

	public static Result error(int code, String msg) {
		Result result = new Result();
		result.put("code", code);
		result.put("msg", msg);
		result.put("time", new Date());
		result.put("data", null);
		return result;
	}

	public static Result ok(String msg) {
		Result result = new Result();
		result.put("code", 0);
		result.put("msg", msg);
		result.put("time", new Date());
		result.put("data", null);
		return result;
	}

	public static Result ok(Map<String, Object> map) {
		Result result = new Result();
		result.putAll(map);
		return result;
	}

	public static Result ok() {
		return new Result();
	}

	@Override
	public Result put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
