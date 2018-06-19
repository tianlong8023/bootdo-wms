package com.bootdo.common.exception;

import com.bootdo.common.utils.GenUtils;
import org.apache.commons.configuration.Configuration;

/**
 * 自定义异常
 * 
 */
public class BDException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String msg;
	private int code = 500;

	public BDException(ExceptionTypeEnum typeEnum, int code) {
		super(String.valueOf(code));
		Configuration conf = GenUtils.getExceptionConfig(typeEnum.getName());
		String msg = String.valueOf(conf.getProperty(String.valueOf(code)));
		this.msg = msg;
		this.code = code;
	}

	public BDException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public BDException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}

	public BDException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}

	public BDException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
