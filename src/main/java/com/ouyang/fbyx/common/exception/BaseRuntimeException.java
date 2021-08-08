package com.ouyang.fbyx.common.exception;

import lombok.Data;

/**
 * @description 异常基类
 * @author ouyangxingjie
 * @date 2021/6/29 21:17
 */
@Data
public class BaseRuntimeException extends RuntimeException {

	/**
	 * 序列化版本号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 错误码
	 */
	private int errCode;
	/**
	 * 错误信息
	 */
	private String errMsg;

	/**
	 * 异常基类
	 */
	public BaseRuntimeException() {
		super();
	}

	/**
	 * @param message 异常信息
	 * @param cause 异常
	 */
	public BaseRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message 异常信息
	 */
	public BaseRuntimeException(String message) {
		super(message);
	}

	/**
	 * @param cause 异常
	 */
	public BaseRuntimeException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param errCode 错误码
	 * @param errMsg 错误信息
	 */
	public BaseRuntimeException(int errCode, String errMsg) {
		super(errCode + ":" + errMsg);
		this.errCode = errCode;
		this.errMsg = errMsg;
	}
	
	/**  
	 * 获取错误码  
	 * @return errCode 错误码  
	 */
	public int getErrCode() {
		return errCode;
	}

	/**  
	 * 获取错误信息  
	 * @return errMsg 错误信息  
	 */
	public String getErrMsg() {
		return errMsg;
	}

	/**
	 * 覆写fillInStackTrace,并且去掉同步，以提高性能
	 * 业务异常本身也无需堆栈信息
	 */
	@Override
	public Throwable fillInStackTrace() {
		return this;
	}

}
