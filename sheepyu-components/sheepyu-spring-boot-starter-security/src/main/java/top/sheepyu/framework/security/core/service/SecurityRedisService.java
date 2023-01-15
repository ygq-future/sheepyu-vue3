package top.sheepyu.framework.security.core.service;

import cn.hutool.core.lang.UUID;
import top.sheepyu.framework.security.core.constants.SecurityRedisConstants;
import top.sheepyu.framework.redis.util.RedisUtil;
import top.sheepyu.framework.security.core.LoginUser;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author ygq
 * @date 2023-01-15 14:37
 **/
public class SecurityRedisService {
    @Resource
    private RedisUtil redisUtil;

    public LoginUser getLoginUser(String prefix, String token) {
        return redisUtil.get(prefix.concat(token), LoginUser.class);
    }

    public void setLoginUser(LoginUser loginUser) {
        String accessToken = UUID.fastUUID().toString(true);
        String refreshToken = UUID.fastUUID().toString(true);
        loginUser.setAccessToken(accessToken);
        loginUser.setRefreshToken(refreshToken);
        loginUser.setExpireTime(LocalDateTime.now().plusMinutes(SecurityRedisConstants.ACCESS_TOKEN_TTL));

        redisUtil.set(SecurityRedisConstants.ACCESS_TOKEN_KEY.concat(accessToken), loginUser, SecurityRedisConstants.ACCESS_TOKEN_TTL, TimeUnit.MINUTES);
        redisUtil.set(SecurityRedisConstants.REFRESH_TOKEN_KEY.concat(refreshToken), loginUser, SecurityRedisConstants.REFRESH_TOKEN_TTL, TimeUnit.MINUTES);
    }

    public void delLoginUser(String accessToken, String refreshToken) {
        redisUtil.del(SecurityRedisConstants.ACCESS_TOKEN_KEY.concat(accessToken));
        redisUtil.del(SecurityRedisConstants.REFRESH_TOKEN_KEY.concat(refreshToken));
    }
}
