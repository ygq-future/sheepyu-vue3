package top.sheepyu.framework.web.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.sheepyu.framework.web.config.WebProperties;
import top.sheepyu.module.common.enums.UserTypeEnum;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * 专属于 web 包的工具类
 *
 * @author ygq
 */
public class WebFrameworkUtil {
    private static final String REQUEST_ATTRIBUTE_LOGIN_USER_ID = "login_user_id";
    private static final String REQUEST_ATTRIBUTE_LOGIN_USERNAME = "login_user_name";
    private static final String REQUEST_ATTRIBUTE_LOGIN_USER_TYPE = "login_user_type";
    private static WebProperties properties;

    public static void setProperties(WebProperties webProperties) {
        properties = webProperties;
    }

    public static void setLoginUserId(ServletRequest request, Long userId) {
        request.setAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_ID, userId);
    }

    public static void setLoginUserUsername(ServletRequest request, String username) {
        request.setAttribute(REQUEST_ATTRIBUTE_LOGIN_USERNAME, username);
    }

    public static void setLoginUserType(ServletRequest request, Integer userType) {
        request.setAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_TYPE, userType);
    }

    public static Long getLoginUserId(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return (Long) request.getAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_ID);
    }

    public static String getLoginUserUsername(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        //使用String.valueOf防止NPE
        Object username = request.getAttribute(REQUEST_ATTRIBUTE_LOGIN_USERNAME);
        return username == null ? null : username.toString();
    }

    public static Integer getLoginUserType(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        // 1. 优先，从 Attribute 中获取
        Integer userType = (Integer) request.getAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_TYPE);
        if (userType != null) {
            return userType;
        }
        // 2. 其次，基于 URL 前缀的约定
        if (request.getRequestURI().startsWith(properties.getAdmin().getPrefix())) {
            return UserTypeEnum.ADMIN.getCode();
        }
        if (request.getRequestURI().startsWith(properties.getApp().getPrefix())) {
            return UserTypeEnum.MEMBER.getCode();
        }
        return null;
    }

    public static String getLoginUserUsername() {
        HttpServletRequest request = getRequest();
        return getLoginUserUsername(request);
    }

    public static Long getLoginUserId() {
        HttpServletRequest request = getRequest();
        return getLoginUserId(request);
    }

    public static Integer getLoginUserType() {
        HttpServletRequest request = getRequest();
        return getLoginUserType(request);
    }

    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return null;
        }
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        return servletRequestAttributes.getRequest();
    }
}
