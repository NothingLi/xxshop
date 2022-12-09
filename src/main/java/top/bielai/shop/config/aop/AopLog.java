package top.bielai.shop.config.aop;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.UUID;


/**
 * @author bielai
 */
@Aspect
@Component
@Slf4j
public class AopLog {


    private static final String REQUEST_ID = "requestId";

    /**
     * 设置切入点，按需要的项目结构设置
     */
    @Pointcut("execution(public * top.bielai.shop.api.*.*Api.*(..))")
    public void log() {
        //做切点用，空方法
    }

    @Before("log()")
    public void before(JoinPoint point) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
            UUID requestId = UUID.randomUUID();
            String requestPath = "net:" + request.getRequestURL() + ";java:" + point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName();
            String requestHeader = JSON.toJSONString(ServletUtil.getHeaderMap(request));
            String params = JSON.toJSONString(ServletUtil.getParams(request));
            Object[] args = point.getArgs();
            StringBuilder body = new StringBuilder();
            for (Object arg : args) {
                if (!(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse) && !(arg instanceof MultipartFile)) {
                    body.append(JSON.toJSONString(arg));
                }
            }
            String requestBody = body.toString();
            log.info("收到请求:{},路径:{},请求头:{},参数:{},{},请求人:{}", requestId, requestPath, requestHeader, params, requestBody, request.getHeader("token"));
            request.setAttribute(REQUEST_ID, requestId.toString());
        } catch (Exception e) {
            log.error("记录请求日志异常:{}", e.getLocalizedMessage());
        }
    }


    @AfterReturning(value = "log()", returning = "rvt")
    private void afterReturning(Object rvt) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
            String responseBody = JSON.toJSONString(ObjectUtil.isEmpty(rvt) ? "" : rvt);
            log.info("请求id:{} 处理完成,结果:{},请求人:{}", request.getAttribute(REQUEST_ID), responseBody, request.getHeader("token"));
        } catch (Exception e) {
            log.error("记录响应日志异常:{}", e.getLocalizedMessage());
        }
    }

    @AfterThrowing(value = "log()", throwing = "e")
    public void afterThrowing(Exception e) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
            String exceptionMessage = e.getLocalizedMessage().substring(0, Math.min(e.getLocalizedMessage().length(), 512));
            StringBuilder stackMsg = new StringBuilder(exceptionMessage + "异常发生在");
            StackTraceElement[] stackTrace = e.getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                stackMsg.append(stackTraceElement.toString()).append(System.lineSeparator());
            }
            log.info("请求id:{} 处理出现异常,问题:{},请求人:{}", request.getAttribute(REQUEST_ID), stackMsg, request.getHeader("token"));
        } catch (Exception ex) {
            log.error("记录错误日志异常:{}", ex.getLocalizedMessage());
        }
    }
}