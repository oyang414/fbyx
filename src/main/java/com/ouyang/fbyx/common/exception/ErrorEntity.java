package com.ouyang.fbyx.common.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @description
 * @return controller异常返回实体类
 * @author ouyangxingjie
 * @date 2021/6/29 21:25
 */
@Data
@AllArgsConstructor
public class ErrorEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//业务异常code
	private int code;
	//业务异常信息
	@JsonProperty("err_msg")
	private String errMsg;


	
}

