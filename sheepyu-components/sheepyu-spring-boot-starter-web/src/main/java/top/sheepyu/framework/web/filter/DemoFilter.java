package top.sheepyu.framework.web.filter;

import cn.hutool.core.util.StrUtil;
import org.springframework.web.filter.OncePerRequestFilter;
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
public class DemoFilter extends OncePerRequestFilter {
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String method = request.getMethod();
        //只过滤操作请求
        return !StrUtil.equalsAnyIgnoreCase(method, "POST", "PUT", "DELETE", "PATCH");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException {
        // 直接返回 DEMO_DENY 的结果。即，请求不继续
        ServletUtil.response(response, Result.fail(ErrorCodeConstants.DEMO_DENY));
    }
}
