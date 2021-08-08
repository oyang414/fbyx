
/**
 * @description 错误信息基本码
 * @author ouyangxingjie
 * @date 2021/6/29 23:10
 */
package com.ouyang.fbyx.constants;

/**
 * 基本错误码
 */
public enum BaseErrorCode {

	/**
	 * 系统参数错误
	 */
	PARAMS_ERROR( 400, "参数错误"),

	/**
	 * {@code 500 服务器繁忙}.
	 */
	SYSTEM_ERROR(500, "服务器繁忙"),
	
	/**
	 * {@code 404 资源不存在}.
	 */
	RESOURCE_NOT_FOUND(404, "资源不存在");

	private int errorCode;
	private String errorMsg;

	BaseErrorCode(int errorCode, String errorMsg) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public int getErrorCode() {
		return this.errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}
}
