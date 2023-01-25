package top.sheepyu.framework.security.config;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import top.sheepyu.framework.security.core.annotations.Permit;
import top.sheepyu.framework.security.core.aop.PreAuthenticatedAspect;
import top.sheepyu.framework.security.core.filter.SecurityTokenFilter;
import top.sheepyu.framework.security.core.service.SecurityFrameworkService;
import top.sheepyu.framework.security.core.service.SecurityFrameworkServiceImpl;
import top.sheepyu.framework.security.core.service.SecurityRedisService;
import top.sheepyu.framework.web.config.WebProperties;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

import static top.sheepyu.module.common.common.Result.fail;
import static top.sheepyu.module.common.constants.ErrorCodeConstants.NOT_AUTHORIZE;
import static top.sheepyu.module.common.constants.ErrorCodeConstants.NOT_PERMISSION;
import static top.sheepyu.module.common.util.ServletUtil.response;

/**
 * @author ygq
 * @date 2023-01-15 14:13
 **/
@Configuration
@EnableConfigurationProperties({SecurityProperties.class})
@EnableGlobalMethodSecurity(prePostEnabled = true)
/*
要在SecurityAutoConfiguration之前配置, 否则就会出现security配置了一个securityFilterChain
然后这个有配置了一个WebSecurityConfigurerAdapter,从而导致异常, 因为这两个不能同时存在,
而如果这个在SecurityAutoConfiguration之前,那么它检测到有WebSecurityConfigurerAdapter
就不会配置securityFilterChain了
 */
@AutoConfigureBefore(SecurityAutoConfiguration.class)
public class SheepyuSecurityAutoConfiguration extends WebSecurityConfigurerAdapter {
    @Resource
    private SecurityProperties securityProperties;
    @Resource
    private WebProperties webProperties;
    @Resource
    private ApplicationContext applicationContext;

    @Bean
    @ConditionalOnMissingBean(AuthenticationManager.class)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //基本处理
        http.formLogin().disable()
                .cors().configurationSource(this.configurationSource()).and()
                .csrf().disable()
                .rememberMe().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .headers().frameOptions().disable().and()
                .exceptionHandling()
                .authenticationEntryPoint((req, res, ex) -> response(res, fail(NOT_AUTHORIZE)))
                .accessDeniedHandler((req, res, ex) -> response(res, fail(NOT_PERMISSION)));

        //请求处理
        Multimap<HttpMethod, String> permitAllUrls = this.getPermitAllUrlsFromAnnotations();
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/file/**", "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/swagger-resources/**", "/v2/**").permitAll()
                .antMatchers(HttpMethod.GET, permitAllUrls.get(HttpMethod.GET).toArray(new String[]{})).permitAll()
                .antMatchers(HttpMethod.POST, permitAllUrls.get(HttpMethod.POST).toArray(new String[]{})).permitAll()
                .antMatchers(HttpMethod.PUT, permitAllUrls.get(HttpMethod.PUT).toArray(new String[]{})).permitAll()
                .antMatchers(HttpMethod.DELETE, permitAllUrls.get(HttpMethod.DELETE).toArray(new String[]{})).permitAll()
                .antMatchers(HttpMethod.PATCH, permitAllUrls.get(HttpMethod.PATCH).toArray(new String[]{})).permitAll()
                //不需要认证的请求
                .antMatchers(securityProperties.getPermitUrls().toArray(new String[]{})).permitAll()
                //需要认证的请求
                .antMatchers(securityProperties.getAuthenticateUrls().toArray(new String[]{})).authenticated()
                .anyRequest().authenticated();

        //如果是用户中心模式
        if (securityProperties.isUserCenterMode()) {
            http.authorizeRequests().antMatchers(buildAppApi("/**")).permitAll();
        }

        http.addFilterBefore(securityTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityTokenFilter securityTokenFilter() {
        return new SecurityTokenFilter(securityProperties, securityRedisService());
    }

    @Bean
    public PreAuthenticatedAspect preAuthenticatedAspect() {
        return new PreAuthenticatedAspect();
    }

    @Bean
    public SecurityRedisService securityRedisService() {
        return new SecurityRedisService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean("ss")
    public SecurityFrameworkService securityFrameworkService() {
        return new SecurityFrameworkServiceImpl();
    }

    private String buildAppApi(String url) {
        return webProperties.getApp().getPrefix().concat(url);
    }

    private Multimap<HttpMethod, String> getPermitAllUrlsFromAnnotations() {
        Multimap<HttpMethod, String> result = HashMultimap.create();
        // 获得接口对应的 HandlerMethod 集合
        RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping)
                applicationContext.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = requestMappingHandlerMapping.getHandlerMethods();
        // 获得有 @Permit 注解的接口
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            if (!handlerMethod.hasMethodAnnotation(Permit.class)) {
                continue;
            }
            if (entry.getKey().getPatternsCondition() == null) {
                continue;
            }
            Set<String> urls = entry.getKey().getPatternsCondition().getPatterns();
            // 根据请求方法，添加到 result 结果
            entry.getKey().getMethodsCondition().getMethods().forEach(requestMethod -> {
                switch (requestMethod) {
                    case GET:
                        result.putAll(HttpMethod.GET, urls);
                        break;
                    case POST:
                        result.putAll(HttpMethod.POST, urls);
                        break;
                    case PUT:
                        result.putAll(HttpMethod.PUT, urls);
                        break;
                    case DELETE:
                        result.putAll(HttpMethod.DELETE, urls);
                        break;
                    case PATCH:
                        result.putAll(HttpMethod.PATCH, urls);
                }
            });
        }
        return result;
    }
}
