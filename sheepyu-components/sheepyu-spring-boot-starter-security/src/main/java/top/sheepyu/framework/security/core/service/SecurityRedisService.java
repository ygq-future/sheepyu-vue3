package top.sheepyu.framework.security.core.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import top.sheepyu.framework.redis.util.RedisUtil;
import top.sheepyu.framework.security.config.LoginUser;
import top.sheepyu.framework.security.core.constants.SecurityRedisConstants;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static top.sheepyu.framework.security.core.constants.SecurityRedisConstants.ACCESS_TOKEN_TTL;
import static top.sheepyu.framework.security.core.constants.SecurityRedisConstants.REFRESH_TOKEN_TTL;

/**
 * @author ygq
 * @date 2023-01-15 14:37
 **/
public class SecurityRedisService {
    @Resource
    private RedisUtil redisUtil;

    /**
     * 根据refreshToken或者accessToken获取当前登录用户
     *
     * @param prefix SecurityRedisConstants
     * @param token  token
     * @return LoginUser
     */
    public LoginUser getLoginUser(String prefix, String token) {
        return redisUtil.getObj(prefix.concat(token), LoginUser.class);
    }

    public void setLoginUser(LoginUser loginUser) {
        String accessToken = UUID.fastUUID().toString(true);
        String refreshToken = UUID.fastUUID().toString(true);
        loginUser.setAccessToken(accessToken);
        loginUser.setRefreshToken(refreshToken);
        loginUser.setExpireTime(DateUtil.offsetMinute(new Date(), ACCESS_TOKEN_TTL.intValue()));
        loginUser.setRefreshExpireTime(DateUtil.offsetMinute(new Date(), REFRESH_TOKEN_TTL.intValue()));

        redisUtil.set(SecurityRedisConstants.ACCESS_TOKEN_KEY.concat(accessToken), loginUser, ACCESS_TOKEN_TTL, TimeUnit.MINUTES);
        redisUtil.set(SecurityRedisConstants.REFRESH_TOKEN_KEY.concat(refreshToken), loginUser, SecurityRedisConstants.REFRESH_TOKEN_TTL, TimeUnit.MINUTES);
    }

    public void delLoginUser(String accessToken, String refreshToken) {
        delAccessToken(accessToken);
        delRefreshToken(refreshToken);
    }

    public void delAccessToken(String accessToken) {
        if (StrUtil.isNotBlank(accessToken)) {
            redisUtil.del(SecurityRedisConstants.ACCESS_TOKEN_KEY.concat(accessToken));
        }
    }

    public void delRefreshToken(String refreshToken) {
        if (StrUtil.isNotBlank(refreshToken)) {
            redisUtil.del(SecurityRedisConstants.REFRESH_TOKEN_KEY.concat(refreshToken));
        }
    }


}
