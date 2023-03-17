package top.sheepyu.framework.web.core.filter;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import top.sheepyu.framework.web.config.WebProperties;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.common.constants.ErrorCodeConstants;
import top.sheepyu.module.common.util.ServletUtil;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ygq
 * @date 2023-01-14 15:37
 **/
@AllArgsConstructor
public class DemoFilter extends OncePerRequestFilter {
    private final WebProperties webProperties;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        AntPathMatcher matcher = new AntPathMatcher();
        boolean excluded = webProperties.getDemoExcludeUrl().stream().anyMatch(p -> matcher.match(p, request.getRequestURI()));
        String method = request.getMethod();
        //只过滤操作请求
        return !StrUtil.equalsAnyIgnoreCase(method, "POST", "PUT", "DELETE", "PATCH") || excluded;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException {
        // 直接返回 DEMO_DENY 的结果。即，请求不继续
        ServletUtil.response(response, Result.fail(ErrorCodeConstants.DEMO_DENY));
    }
}
