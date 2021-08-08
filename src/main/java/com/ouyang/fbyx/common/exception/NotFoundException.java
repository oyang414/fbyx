package com.ouyang.fbyx.common.exception;

/**
 * @description 资源不存在异常
 * @author ouyangxingjie
 * @date 2021/6/29 21:59
 */

public class NotFoundException extends BusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * @param errCode 错误码
     * @param errMsg  错误信息
     */
    public NotFoundException(int errCode, String errMsg) {
        super(errCode, errMsg);
    }
}
