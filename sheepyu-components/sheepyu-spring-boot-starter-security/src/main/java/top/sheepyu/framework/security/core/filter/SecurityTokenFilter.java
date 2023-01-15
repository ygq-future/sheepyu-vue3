package top.sheepyu.framework.security.core.filter;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;
import top.sheepyu.framework.security.core.LoginUser;
import top.sheepyu.framework.security.core.SecurityProperties;
import top.sheepyu.framework.security.core.constants.SecurityRedisConstants;
import top.sheepyu.framework.security.core.service.SecurityRedisService;
import top.sheepyu.framework.security.util.SecurityFrameworkUtil;
import top.sheepyu.framework.web.util.WebFrameworkUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author ygq
 * @date 2022-12-03 16:55
 **/
@AllArgsConstructor
public class SecurityTokenFilter extends OncePerRequestFilter {
    private SecurityProperties securityProperties;
    private SecurityRedisService securityRedisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(securityProperties.getTokenHeader());
        if (StrUtil.isNotBlank(token)) {
            Integer userType = WebFrameworkUtil.getLoginUserType(request);
            LoginUser loginUser = securityRedisService.getLoginUser(SecurityRedisConstants.ACCESS_TOKEN_KEY, token);

            if (loginUser == null) {
                loginUser = buildMockUser(token, userType);
            }

            if (loginUser != null) {
                SecurityFrameworkUtil.setLoginUser(loginUser, request);
            }
        }
        filterChain.doFilter(request, response);
    }

    private LoginUser buildMockUser(String token, Integer userType) {
        if (!securityProperties.isMockEnable() || !token.startsWith(securityProperties.getMockSecret())) {
            return null;
        }
        return new LoginUser().setId(1L).setUserType(userType);
    }
}
