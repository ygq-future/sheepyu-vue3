package top.sheepyu.framework.log.core.aop;

import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import top.sheepyu.framework.log.core.ApiLogProperties;
import top.sheepyu.framework.log.core.annotations.RecordLog;
import top.sheepyu.framework.log.core.enums.OperateTypeEnum;
import top.sheepyu.framework.log.core.service.ApiLogFrameworkService;
import top.sheepyu.framework.web.util.WebFrameworkUtil;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.common.enums.UserTypeEnum;
import top.sheepyu.module.common.exception.CommonException;
import top.sheepyu.module.common.util.ExceptionUtil;
import top.sheepyu.module.common.util.ServletUtil;
import top.sheepyu.module.system.dto.ApiLogDto;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

import static top.sheepyu.framework.log.core.enums.OperateTypeEnum.*;

/**
 * @author ygq
 * @date 2023-01-17 16:16
 **/
@Aspect
@Slf4j
public class RecordLogAspect {
    @Resource
    private ApiLogFrameworkService apiLogFrameworkService;
    @Resource
    private ApiLogProperties apiLogProperties;
    private static final ThreadLocal<ApiLogDto> LOG = new ThreadLocal<>();

    @Before("@annotation(apiOperation)")
    public void before(JoinPoint joinPoint, ApiOperation apiOperation) {
        Integer userType = WebFrameworkUtil.getLoginUserType();
        if (!Objects.equals(userType, UserTypeEnum.ADMIN.getValue())) {
            return;
        }

        ApiLogDto apiLog = new ApiLogDto().setUserId(WebFrameworkUtil.getLoginUserId()).setUserType(userType);
        apiLog.setName(apiOperation.value()).setStartTime(System.currentTimeMillis());
        setRequestAbout(apiLog, joinPoint);
        LOG.set(apiLog);
    }

    @AfterReturning(value = "@annotation(apiOperation)", returning = "returnValue")
    public void afterReturning(ApiOperation apiOperation, Object returnValue) {
        try {
            ApiLogDto apiLog = LOG.get();
            //如果是get方法且没有RecordLog注解就不记录
            if (apiLog.isClose()) {
                return;
            }

            //只记录返回结果为Result的
            if (Result.class.isAssignableFrom(returnValue.getClass())) {
                Result<?> result = (Result<?>) returnValue;
                apiLog.setResultCode(result.getCode()).setResultData(JSONUtil.toJsonStr(result.getData()));
            }

            setDuration(apiLog);
            apiLogFrameworkService.createApiLog(apiLog);
        } finally {
            LOG.remove();
        }
    }

    @AfterThrowing(value = "@annotation(apiOperation)", throwing = "th")
    public void afterThrowing(ApiOperation apiOperation, Throwable th) {
        try {
            //如果是自定义异常也不记录
            if (th.getClass().isAssignableFrom(CommonException.class)) {
                return;
            }

            ApiLogDto apiLog = LOG.get();
            apiLog.setExceptionTime(LocalDateTime.now());
            apiLog.setExceptionName(th.getClass().getName());
            apiLog.setExceptionRootCaseMessage(ExceptionUtil.getRootCauseMessage(th));
            apiLog.setExceptionStackTraceFull(ExceptionUtil.getMessage(th));

            List<String> crucialInfo = new ArrayList<>();
            for (StackTraceElement traceElement : th.getStackTrace()) {
                String className = traceElement.getClassName();
                String fileName = traceElement.getFileName();
                String methodName = traceElement.getMethodName();
                int line = traceElement.getLineNumber();
                if (className.startsWith("top.sheepyu") && !fileName.contains("generated")) {
                    String info = String.format("class: %s, filename: %s, method: %s, line: %s", className, fileName, methodName, line);
                    crucialInfo.add(info);
                }
            }

            apiLog.setExceptionStackTraceCrucial(String.join("\n", crucialInfo));
            setDuration(apiLog);
            apiLogFrameworkService.createApiLog(apiLog);
        } finally {
            LOG.remove();
        }
    }

    private void setDuration(ApiLogDto apiLog) {
        long duration = System.currentTimeMillis() - apiLog.getStartTime();
        apiLog.setDuration((int) duration);
    }

    private void setRequestAbout(ApiLogDto apiLog, JoinPoint joinPoint) {
        HttpServletRequest request = WebFrameworkUtil.getRequest();
        String requestURI = request.getRequestURI();
        filterExcludes(requestURI, apiLog);

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RequestMapping requestMapping = AnnotationUtils.getAnnotation(method, RequestMapping.class);
        RequestMethod[] methods = requestMapping == null ? new RequestMethod[1] : requestMapping.method();
        RequestMethod requestMethod = ArrayUtil.firstNonNull(methods);

        OperateTypeEnum operateType = null;
        switch (requestMethod) {
            case GET:
                operateType = GET;
                //如果是get方法, 且没有RecordLog注解就不记录日志
                RecordLog recordLog = AnnotationUtils.getAnnotation(method, RecordLog.class);
                apiLog.setClose(recordLog == null);
                break;
            case POST:
                operateType = CREATE;
                break;
            case PUT:
            case PATCH:
                operateType = UPDATE;
                break;
            case DELETE:
                operateType = DELETE;
        }

        apiLog.setType(operateType == null ? OTHER.getDesc() : operateType.getDesc());
        apiLog.setRequestUrl(requestURI);
        apiLog.setRequestMethod(request.getMethod());
        apiLog.setRequestParams(obtainMethodArgs(joinPoint));
        apiLog.setUserIp(ServletUtil.getClientIp(request));
    }

    private void filterExcludes(String requestURI, ApiLogDto apiLogDto) {
        AntPathMatcher matcher = new AntPathMatcher();
        for (String excludeUri : apiLogProperties.getExcludes()) {
            if (matcher.match(excludeUri, requestURI)) {
                apiLogDto.setClose(true);
                return;
            }
        }
    }

    private static String obtainMethodArgs(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] argNames = methodSignature.getParameterNames();
        Object[] argValues = joinPoint.getArgs();
        // 拼接参数
        Map<String, Object> args = Maps.newHashMapWithExpectedSize(argValues.length);
        for (int i = 0; i < argNames.length; i++) {
            String argName = argNames[i];
            Object argValue = argValues[i];
            // 被忽略时，标记为 ignore 字符串，避免和 null 混在一起
            args.put(argName, !isIgnoreArgs(argValue) ? argValue : "[ignore]");
        }
        return JSONUtil.toJsonStr(args);
    }

    private static boolean isIgnoreArgs(Object object) {
        Class<?> clazz = object.getClass();
        // 处理数组的情况
        if (clazz.isArray()) {
            return IntStream.range(0, Array.getLength(object)).anyMatch(i -> isIgnoreArgs(Array.get(object, i)));
        }
        // 递归，处理数组、Collection、Map 的情况
        if (Collection.class.isAssignableFrom(clazz)) {
            return ((Collection<?>) object).stream().anyMatch(RecordLogAspect::isIgnoreArgs);
        }
        if (Map.class.isAssignableFrom(clazz)) {
            return isIgnoreArgs(((Map<?, ?>) object).values());
        }
        // obj
        return object instanceof MultipartFile
                || object instanceof HttpServletRequest
                || object instanceof HttpServletResponse
                || object instanceof BindingResult;
    }
}
