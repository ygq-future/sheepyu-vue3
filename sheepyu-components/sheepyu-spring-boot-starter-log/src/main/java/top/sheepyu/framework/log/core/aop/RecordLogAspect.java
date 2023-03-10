package top.sheepyu.framework.log.core.aop;

import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import top.sheepyu.framework.log.config.ApiLogProperties;
import top.sheepyu.framework.log.core.annotations.RecordLog;
import top.sheepyu.framework.log.core.service.ApiLogFrameworkService;
import top.sheepyu.framework.web.util.WebFrameworkUtil;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.common.exception.CommonException;
import top.sheepyu.module.common.util.ExceptionUtil;
import top.sheepyu.module.common.util.ServletUtil;
import top.sheepyu.module.system.api.log.ApiLogDto;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.IntStream;

import static top.sheepyu.framework.log.core.enums.OperateTypeEnum.*;
import static top.sheepyu.module.common.enums.UserTypeEnum.ADMIN;

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

    @Around("@annotation(apiOperation)")
    public Object around(ProceedingJoinPoint pj, ApiOperation apiOperation) throws Throwable {
        RecordLog recordLog = getMethodAnnotation(pj, RecordLog.class);
        return processAround(pj, recordLog, apiOperation);
    }

    @Around("!@annotation(io.swagger.annotations.ApiOperation) && @annotation(recordLog)")
    // ???????????????????????? @RecordLog ???????????????
    public Object around(ProceedingJoinPoint joinPoint, RecordLog recordLog) throws Throwable {
        return processAround(joinPoint, recordLog, null);
    }

    private Object processAround(ProceedingJoinPoint pj, RecordLog recordLog, ApiOperation apiOperation) throws Throwable {
        if (!Objects.equals(WebFrameworkUtil.getLoginUserType(), ADMIN.getCode())) {
            return pj.proceed();
        }
        //?????????????????????
        if (isExcludes()) {
            return pj.proceed();
        }

        Object proceed;
        long startTime = System.currentTimeMillis();
        try {
            proceed = pj.proceed();
            //????????????
            log(pj, recordLog, apiOperation, startTime, proceed, null);
        } catch (Throwable e) {
            //????????????
            log(pj, recordLog, apiOperation, startTime, null, e);
            throw e;
        }
        return proceed;
    }

    private void log(ProceedingJoinPoint pj, RecordLog recordLog, ApiOperation apiOperation, long startTime, Object result, Throwable exception) {
        if (!isLogEnable(pj, recordLog, exception)) {
            return;
        }

        try {
            ApiLogDto apiLog = new ApiLogDto();
            //????????????????????????
            fillBaseFields(pj, apiLog, apiOperation, recordLog);
            // ??????????????????
            fillRequestFields(apiLog, pj, startTime, result);
            // ??????????????????
            fillExceptionFields(apiLog, exception);

            // ??????????????????
            apiLogFrameworkService.createApiLog(apiLog);
        } catch (Throwable e) {
            log.error("[log][?????????????????????????????????????????????????????? joinPoint({}) recordLog({}) apiOperation({}) result({}) exception({}) ]", pj, recordLog, apiOperation, result, exception, exception);
            throw e;
        }
    }

    private void fillExceptionFields(ApiLogDto apiLog, Throwable th) {
        if (th == null) {
            return;
        }
        //????????????????????????????????????
        if (th.getClass().isAssignableFrom(CommonException.class)) {
            return;
        }
        //?????????????????????
        if (th.toString().contains("validation")) {
            return;
        }

        apiLog.setExceptionTime(new Date());
        apiLog.setExceptionName(th.getClass().getName());
        apiLog.setExceptionRootCauseMessage(ExceptionUtil.getRootCauseMessage(th));
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
    }

    private void fillBaseFields(ProceedingJoinPoint pj, ApiLogDto apiLog, ApiOperation apiOperation, RecordLog recordLog) {
        //??????????????????
        apiLog.setUserId(WebFrameworkUtil.getLoginUserId());
        apiLog.setUserType(WebFrameworkUtil.getLoginUserType());

        //??????api??????
        if (recordLog != null) {
            apiLog.setType(recordLog.value().getCode());
        } else {
            apiLog.setType(obtainOperateType(pj));
        }
        apiLog.setName(apiOperation.value());
    }

    private Integer obtainOperateType(ProceedingJoinPoint pj) {
        RequestMethod requestMethod = obtainFirstLogRequestMethod(obtainRequestMethod(pj));
        if (requestMethod == null) {
            return GET.getCode();
        }

        switch (requestMethod) {
            case GET:
                return GET.getCode();
            case POST:
                return CREATE.getCode();
            case PUT:
            case PATCH:
                return UPDATE.getCode();
            case DELETE:
                return DELETE.getCode();
            default:
                return OTHER.getCode();
        }
    }

    private void fillRequestFields(ApiLogDto apiLog, ProceedingJoinPoint jp, long startTime, Object result) {
        // ?????? Request ??????
        HttpServletRequest request = WebFrameworkUtil.getRequest();
        if (request == null) {
            return;
        }
        //??????????????????
        apiLog.setRequestMethod(request.getMethod());
        apiLog.setRequestUrl(request.getRequestURI());
        apiLog.setUserIp(ServletUtil.getClientIp(request));
        apiLog.setRequestParams(obtainMethodArgs(jp));
        apiLog.setDuration((int) (System.currentTimeMillis() - startTime));

        //????????????????????????
        if (result != null && Result.class.isAssignableFrom(result.getClass())) {
            Result<?> r = (Result<?>) result;
            apiLog.setResultCode(r.getCode()).setResultData(JSONUtil.toJsonStr(r.getData()));
        }
    }

    private static boolean isLogEnable(ProceedingJoinPoint joinPoint, RecordLog recordLog, Throwable ex) {
        //?????????????????????
        if(ex != null) {
            return true;
        }
        // ??? @RecordLog ??????????????????
        if (recordLog != null) {
            return true;
        }
        // ?????? @RecordLog ?????????????????????????????? POST???PUT???DELETE, PATCH ?????????
        return obtainFirstLogRequestMethod(obtainRequestMethod(joinPoint)) != null;
    }

    private static RequestMethod obtainFirstLogRequestMethod(RequestMethod[] requestMethods) {
        if (ArrayUtil.isEmpty(requestMethods)) {
            return null;
        }
        return Arrays.stream(requestMethods).filter(requestMethod -> requestMethod == RequestMethod.POST
                        || requestMethod == RequestMethod.PUT
                        || requestMethod == RequestMethod.DELETE
                        || requestMethod == RequestMethod.PATCH)
                .findFirst().orElse(null);
    }

    private static RequestMethod[] obtainRequestMethod(ProceedingJoinPoint joinPoint) {
        RequestMapping requestMapping = AnnotationUtils.getAnnotation( // ?????? Spring ??????????????????????????? @RequestMapping ????????????
                ((MethodSignature) joinPoint.getSignature()).getMethod(), RequestMapping.class);
        return requestMapping != null ? requestMapping.method() : new RequestMethod[]{};
    }

    private static <T extends Annotation> T getMethodAnnotation(ProceedingJoinPoint joinPoint, Class<T> annotationClass) {
        return ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(annotationClass);
    }

    private boolean isExcludes() {
        HttpServletRequest request = WebFrameworkUtil.getRequest();
        if (request == null) {
            return false;
        }

        String requestURI = request.getRequestURI();
        AntPathMatcher matcher = new AntPathMatcher();
        for (String excludeUri : apiLogProperties.getExcludes()) {
            if (matcher.match(excludeUri, requestURI)) {
                return true;
            }
        }
        return false;
    }

    private static String obtainMethodArgs(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] argNames = methodSignature.getParameterNames();
        Object[] argValues = joinPoint.getArgs();
        // ????????????
        Map<String, Object> args = Maps.newHashMapWithExpectedSize(argValues.length);
        for (int i = 0; i < argNames.length; i++) {
            String argName = argNames[i];
            Object argValue = argValues[i];
            // ???????????????????????? ignore ????????????????????? null ????????????
            args.put(argName, isIgnoreArgs(argValue) ? "[ignore]" : argValue);
        }
        return JSONUtil.toJsonStr(args);
    }

    private static boolean isIgnoreArgs(Object object) {
        if (object == null) {
            return false;
        }

        Class<?> clazz = object.getClass();
        // ?????????????????????
        if (clazz.isArray()) {
            return IntStream.range(0, Array.getLength(object)).anyMatch(i -> isIgnoreArgs(Array.get(object, i)));
        }
        // ????????????????????????Collection???Map ?????????
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
