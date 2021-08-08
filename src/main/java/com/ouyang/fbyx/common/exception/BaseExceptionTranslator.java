package com.ouyang.fbyx.common.exception;
import com.ouyang.fbyx.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;


/**
 * @description 基础异常信息捕获类，只进行通用异常的处理，通用异常只包括 4XX 和 5XX HTTP status codes 异常
 * @author ouyangxingjie
 * @date 2021/6/29 21:51
 */
public class BaseExceptionTranslator {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * @param ex
     * @description 500 最终异常,当所有异常都不满足时，会被该方法捕获
     * @return com.ouyang.fbyx.common.exception.ErrorEntity
     * @author ouyangxingjie
     * @date 2021/6/29 21:52
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorEntity baseException(Exception ex) {
        logger.error("baseException", ex);
        return new ErrorEntity(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name());
    }

    /**
     * @param ex
     * @description 业务类异常
     * @return com.ouyang.fbyx.common.exception.ErrorEntity
     * @author ouyangxingjie
     * @date 2021/6/29 21:47
     */
    @ExceptionHandler({BusinessException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorEntity businessException(BusinessException ex) {
        return this.handleErrMsg(ex);
    }

    /**
     * @param ex
     * @description 404 未找到异常
     * @return com.ouyang.fbyx.common.exception.ErrorEntity
     * @author ouyangxingjie
     * @date 2021/6/29 21:52
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorEntity notFoundException(NotFoundException ex) {
        return handleErrMsg(ex);
    }

    /**
     * @param ex
     * @description 409 资源冲突
     * @return com.ouyang.fbyx.common.exception.ErrorEntity
     * @author ouyangxingjie
     * @date 2021/6/29 21:52
     */
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorEntity conflictException(ConflictException ex) {
        return handleErrMsg(ex);
    }

    /**
     * @param ex
     * @description body 方法参数不合法异常
     * @return com.ouyang.fbyx.common.exception.ErrorEntity
     * @author ouyangxingjie
     * @date 2021/6/29 21:47
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorEntity processMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return new ErrorEntity(HttpStatus.BAD_REQUEST.value(), ex.getMessage());

    }

    /**
     * @param ex requestBody参数校验异常
     * @description
     * @return com.ouyang.fbyx.common.exception.ErrorEntity
     * @author ouyangxingjie
     * @date 2021/6/29 21:52
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorEntity processMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage= new StringBuilder();
        Set<String> errorMessageSet = new HashSet<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            logger.error("MethodArgumentNotValidException = error field:{}, error message:{}", error.getField(), error.getDefaultMessage());
            if(errorMessageSet.add(error.getDefaultMessage())){
                if(errorMessage.length() > 0){
                    errorMessage.append("\n");
                }
                errorMessage.append(formatErrorField(error.getField())).append(":").append(error.getDefaultMessage()).append(" ");
            }
        }
        return new ErrorEntity(HttpStatus.BAD_REQUEST.value(), errorMessage.toString());

    }

    /**
     * @param ex
     * @description 参数校验异常
     * @return com.ouyang.fbyx.common.exception.ErrorEntity
     * @author ouyangxingjie
     * @date 2021/6/29 21:53
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorEntity processConstraintViolationException(ConstraintViolationException ex) {
        StringBuilder errorMessage = new StringBuilder();
        Set<String> errorMessageSet = new HashSet<>();
        for (ConstraintViolation violation : ex.getConstraintViolations()) {
            logger.error("ConstraintViolationException:{},{}", violation.getInvalidValue(), violation.getMessage());
            if(errorMessageSet.add(violation.getMessage())){
                if(errorMessage.length() > 0){
                    errorMessage.append("\n");
                }
                errorMessage.append(formatErrorField(String.valueOf(violation.getInvalidValue()))).append(":").append(violation.getMessage()).append(" ");
            }
        }
        return new ErrorEntity(HttpStatus.BAD_REQUEST.value(), errorMessage.toString());
    }

   /**
    * @param ex
    * @description 参数不合法异常
    * @return com.ouyang.fbyx.common.exception.ErrorEntity
    * @author ouyangxingjie
    * @date 2021/6/29 21:54
    */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorEntity processConstraintViolationException(IllegalArgumentException ex) {
        return new ErrorEntity(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    /**
     * @param ex
     * @description http消息不可读异常
     * @return com.ouyang.fbyx.common.exception.ErrorEntity
     * @author ouyangxingjie
     * @date 2021/6/29 21:54
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorEntity processHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        logger.error("HttpMessageNotReadableException", ex);
        return new ErrorEntity(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
    }


    /**
     * @param fieldStr
     * @description 只保留错误字段，去除数组定位
     * @return java.lang.String
     * @author ouyangxingjie
     * @date 2021/6/29 21:55
     */
    private String formatErrorField(String fieldStr){
        if(!StringUtil.hasLength(fieldStr)){
            return "";
        }else{
            Integer index = fieldStr.lastIndexOf('.');
            return fieldStr.substring(index > 0 ? index + 1 : 0);
        }
    }

    /**
     * @param ex
     * @description 组装错误信息
     * @return com.ouyang.fbyx.common.exception.ErrorEntity
     * @author ouyangxingjie
     * @date 2021/6/29 21:57
     */
    private ErrorEntity handleErrMsg(BusinessException ex) {
        logger.error(ex.getErrMsg(), ex);
        return new ErrorEntity(ex.getErrCode(), ex.getErrMsg());
    }

}
