package top.sheepyu.framework.security.core.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import top.sheepyu.framework.redis.util.RedisUtil;
import top.sheepyu.framework.security.core.LoginUser;
import top.sheepyu.framework.security.core.constants.SecurityRedisConstants;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static top.sheepyu.framework.security.core.constants.SecurityRedisConstants.ACCESS_TOKEN_TTL;

/**
 * @author ygq
 * @date 2023-01-15 14:37
 **/
public class SecurityRedisService {
    @Resource
    private RedisUtil redisUtil;

    public LoginUser getLoginUser(String prefix, String token) {
        return redisUtil.getJSONObj(prefix.concat(token), LoginUser.class);
    }

    public void setLoginUser(LoginUser loginUser) {
        String accessToken = UUID.fastUUID().toString(true);
        String refreshToken = UUID.fastUUID().toString(true);
        loginUser.setAccessToken(accessToken);
        loginUser.setRefreshToken(refreshToken);
        loginUser.setExpireTime(DateUtil.offsetMinute(new Date(), ACCESS_TOKEN_TTL.intValue()));

        redisUtil.set(SecurityRedisConstants.ACCESS_TOKEN_KEY.concat(accessToken), loginUser, ACCESS_TOKEN_TTL, TimeUnit.MINUTES);
        redisUtil.set(SecurityRedisConstants.REFRESH_TOKEN_KEY.concat(refreshToken), loginUser, SecurityRedisConstants.REFRESH_TOKEN_TTL, TimeUnit.MINUTES);
    }

    public void delLoginUser(String accessToken, String refreshToken) {
        redisUtil.del(SecurityRedisConstants.ACCESS_TOKEN_KEY.concat(accessToken));
        redisUtil.del(SecurityRedisConstants.REFRESH_TOKEN_KEY.concat(refreshToken));
    }
}
