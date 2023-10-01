package top.sheepyu.framework.web.core.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.common.constants.ErrorCodeConstants;
import top.sheepyu.module.common.exception.CommonException;
import top.sheepyu.module.common.util.ExceptionUtil;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.sql.SQLException;

import static top.sheepyu.module.common.constants.ErrorCodeConstants.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 处理 SpringMVC 请求参数缺失
     * 例如说，接口上设置了 @RequestParam("xx") 参数，结果并未传递 xx 参数
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public Result<Object> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException ex) {
        return Result.fail(INVALID_PARAMS.getCode(), String.format("请求参数缺失:%s", ex.getParameterName()));
    }

    /**
     * 处理 SpringMVC 请求参数类型错误
     * 例如说，接口上设置了 @RequestParam("xx") 参数为 Integer，结果传递 xx 参数类型为 String
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Object> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException ex) {
        return Result.fail(INVALID_PARAMS.getCode(), String.format("请求参数类型错误:%s", ex.getMessage()));
    }

    /**
     * 处理 SpringMVC 参数校验不正确
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Object> methodArgumentNotValidExceptionExceptionHandler(MethodArgumentNotValidException ex) {
        FieldError error = ex.getFieldError();
        String msg = error == null ? "" : error.getDefaultMessage();
        return Result.fail(INVALID_PARAMS.getCode(), String.format("请求参数不正确:%s", msg));
    }

    /**
     * 处理 SpringMVC 参数绑定不正确，本质上也是通过 Validator 校验
     */
    @ExceptionHandler(BindException.class)
    public Result<Object> bindExceptionHandler(BindException ex) {
        FieldError error = ex.getFieldError();
        String msg = error == null ? "" : error.getDefaultMessage();
        return Result.fail(INVALID_PARAMS.getCode(), String.format("请求参数不正确:%s", msg));
    }

    /**
     * 处理 Validator 校验不通过产生的异常
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result<Object> constraintViolationExceptionHandler(ConstraintViolationException ex) {
        ConstraintViolation<?> constraintViolation = ex.getConstraintViolations().iterator().next();
        return Result.fail(INVALID_PARAMS.getCode(), String.format("请求参数不正确:%s", constraintViolation.getMessage()));
    }

    /**
     * 处理自定义校验异常
     */
    @ExceptionHandler(value = ValidationException.class)
    public Result<Object> validationExceptionHandler(ValidationException ex) {
        return Result.fail(INVALID_PARAMS.getCode(), String.format("请求参数不正确:%s", ex.getMessage()));
    }

    /**
     * 处理 SpringMVC 请求方法不正确
     * 例如说，A 接口的方法为 GET 方式，结果请求方法为 POST 方式，导致不匹配
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Object> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException ex) {
        return Result.fail(REQUEST_METHOD_NOT_SUPPORT.getCode(), String.format("请求方法不正确:%s", ex.getMessage()));
    }

    @ExceptionHandler(SQLException.class)
    public Result<?> sqlExceptionHandler(SQLException ex) {
        return Result.fail(SQL_ERROR.getCode(), ex.getMessage());
    }

    /**
     * 处理 Spring Security 权限不足的异常
     * 来源是，使用 @PreAuthorize 注解，AOP 进行权限拦截
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public Result<Object> accessDeniedExceptionHandler() {
        return Result.fail(ErrorCodeConstants.NOT_PERMISSION);
    }

    /**
     * 处理业务异常 CommonException
     * 例如说，商品库存不足，用户手机号已存在。
     */
    @ExceptionHandler(value = CommonException.class)
    public Result<Object> serviceExceptionHandler(CommonException ex) {
        return Result.fail(ex.getCode(), ex.getMessage());
    }

    /**
     * 处理系统异常，兜底处理所有的一切
     */
    @ExceptionHandler(value = Exception.class)
    public Result<Object> defaultExceptionHandler(Exception ex) {
        String message = ExceptionUtil.getMessage(ex);
        log.error("\n{}", message);
        return Result.fail(UNKNOWN_ERROR);
    }
}
