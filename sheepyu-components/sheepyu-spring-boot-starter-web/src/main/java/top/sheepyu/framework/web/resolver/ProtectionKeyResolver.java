package top.sheepyu.framework.web.resolver;

import org.aspectj.lang.JoinPoint;

/**
 * @author ygq
 * @date 2023-01-20 15:30
 **/
public interface ProtectionKeyResolver {
    String resolver(JoinPoint joinPoint);
}
