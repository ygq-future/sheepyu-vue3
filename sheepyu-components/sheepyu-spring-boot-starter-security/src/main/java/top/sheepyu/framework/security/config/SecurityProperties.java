package top.sheepyu.framework.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ygq
 * @date 2022-12-03 17:25
 **/
@ConfigurationProperties(prefix = "sheepyu.security")
@Data
@Validated
public class SecurityProperties {
    /**
     * HTTP 请求时，访问令牌的请求 Header
     */
    @NotEmpty(message = "Token Header 不能为空")
    private String tokenHeader = "Authorization";

    @NotEmpty(message = "RefreshToken Header 不能为空")
    private String refreshTokenHeader = "RefreshToken";

    /**
     * mock 模式的开关
     */
    @NotNull(message = "mock 模式的开关不能为空")
    private boolean mockEnable;
    /**
     * mock 模式的密钥
     * 一定要配置密钥，保证安全性
     */
    private String mockSecret = "test";
    private List<String> permitUrls = new ArrayList<>();
    private List<String> authenticateUrls = new ArrayList<>();

    /**
     * 系统是以什么样的模式, 考虑到有些系统是以用户为中心的模式, 大部分功能都要登陆后才能访问, 所以肯定更要大面积用到
     * PreAuthenticated注解, 有的系统用户功能并不是特别重要, 所以又不需要用很多PreAuthenticated
     * 所以我把系统分为两个模式, 用户中心模式, 非用户中心模式, 如果是用户心中模式那么默认app-api下所有请求都需要
     * 认证, 可使用Permit注解和permitAllUrls排除少量不需要认证的接口, 如果是非用户中心模式, 那么app-api下的
     * 所有请求默认都会放行, 可以使用PreAuthenticated注解和authenticateUrls排除少量需要认证的接口,
     * 默认值为false, 也就是非用户中心模式
     */
    private boolean userCenterMode;
}
