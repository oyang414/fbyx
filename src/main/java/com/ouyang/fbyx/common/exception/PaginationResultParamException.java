package com.ouyang.fbyx.common.exception;

/**
 * @description 分页参数异常
 * @author ouyangxingjie
 * @date 2021/6/29 21:19
 */
public class PaginationResultParamException extends BaseRuntimeException {

    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = 1L;

    public PaginationResultParamException() {
        super();
    }

    public PaginationResultParamException(String message){
        super(message);
    }
}
