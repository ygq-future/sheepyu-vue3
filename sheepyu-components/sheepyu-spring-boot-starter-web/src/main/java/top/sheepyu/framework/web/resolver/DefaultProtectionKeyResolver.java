package top.sheepyu.framework.web.resolver;

import cn.hutool.crypto.SecureUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import top.sheepyu.framework.web.util.WebFrameworkUtil;
import top.sheepyu.module.common.constants.ErrorCodeConstants;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

import static top.sheepyu.module.common.exception.CommonException.exception;

/**
 * @author ygq
 * @date 2023-01-20 15:29
 **/
public class DefaultProtectionKeyResolver implements ProtectionKeyResolver {
    @Override
    public String resolver(JoinPoint jp) {
        HttpServletRequest request = WebFrameworkUtil.getRequest();
        Method method = ((MethodSignature) (jp.getSignature())).getMethod();
        RequestMapping requestMapping = AnnotationUtils.getAnnotation(method, RequestMapping.class);
        if (requestMapping == null) {
            throw exception(ErrorCodeConstants.FLOW_LIMIT_ON_METHOD);
        }

        //拼接请求方式名
        StringBuilder sb = new StringBuilder();
        for (RequestMethod requestMethod : requestMapping.method()) {
            sb.append(requestMethod.name());
        }
        //请求uri
        String requestURI = request.getRequestURI();
        //方法名
        String name = method.getName();
        //方法参数
        Object[] args = jp.getArgs();
        //拼接成字符串
        String unique = requestURI + sb + name + Arrays.toString(args);
        return SecureUtil.md5(unique);
    }
}
