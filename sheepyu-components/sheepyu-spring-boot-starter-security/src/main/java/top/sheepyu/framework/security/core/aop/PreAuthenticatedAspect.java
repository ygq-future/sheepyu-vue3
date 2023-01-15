package top.sheepyu.framework.security.core.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import top.sheepyu.framework.security.core.annotation.PreAuthenticated;
import top.sheepyu.framework.security.util.SecurityFrameworkUtil;

import static top.sheepyu.module.common.constants.ErrorCodeConstants.NOT_AUTHORIZE;
import static top.sheepyu.module.common.exception.CommonException.exception;


@Aspect
public class PreAuthenticatedAspect {
    @Before("@annotation(preAuthenticated)")
    public void before(PreAuthenticated preAuthenticated) {
        if (SecurityFrameworkUtil.getLoginUser() == null) {
            throw exception(NOT_AUTHORIZE);
        }
    }
}
