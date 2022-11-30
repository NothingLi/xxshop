
package top.bielai.shop.config;

import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.bielai.shop.common.XxShopException;
import top.bielai.shop.util.Result;
import top.bielai.shop.util.ResultGenerator;

import java.util.Objects;

/**
 * xx-shop 异常处理器
 *
 * @author Administrator
 */
@RestControllerAdvice
public class XxShopExceptionHandler {

    @ExceptionHandler(BindException.class)
    public Result<String> bindException(BindException e) {
        return ResultGenerator.genErrorResult(400, Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> bindException(MethodArgumentNotValidException e) {
        return ResultGenerator.genErrorResult(400, Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(XxShopException.class)
    public Result<String> handleException(XxShopException e) {
        return ResultGenerator.genErrorResult(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<String> handleException() {
        return ResultGenerator.genErrorResult(500, "请求失败，服务开小差了~");

    }
}
