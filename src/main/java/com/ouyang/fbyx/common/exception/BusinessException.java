package com.ouyang.fbyx.common.exception;

import lombok.Data;

/**
 * @description 业务异常
 * @author ouyangxingjie
 * @date 2021/6/29 21:59
 */
@Data
public class BusinessException extends Exception {

    private static final long serialVersionUID = 1L;
    /**
     * 业务错误码
     */
    private final int errCode;
    /**
     * 错误信息
     */
    private final String errMsg;

    /**
     * @param errCode 错误码
     * @param errMsg 错误信息
     */
    protected BusinessException(int errCode, String errMsg) {
        super(errCode + ":" + errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
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
