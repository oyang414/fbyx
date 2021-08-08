/*
 * @Copyright 2018 www.co-mall.com/ Inc. All rights reserved.
 * 注意：本内容仅限于北京科码先锋互联网技术股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.ouyang.fbyx.controller;

import com.ouyang.fbyx.common.exception.BaseExceptionTranslator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;


/**
 * @description 全局异常处理
 * @author ouyangxingjie
 * @date 2021/6/29 22:07
 */
@ControllerAdvice(basePackages="com.ouyang.fbyx.controller")
@Slf4j
public class GlobalExceptionTranslator extends BaseExceptionTranslator {



}
