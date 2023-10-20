package top.sheepyu.framework.security.core.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import top.sheepyu.framework.redis.util.RedisUtil;
import top.sheepyu.framework.security.config.LoginUser;
import top.sheepyu.framework.security.core.constants.SecurityRedisConstants;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static top.sheepyu.framework.security.core.constants.SecurityRedisConstants.*;

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

        redisUtil.set(ACCESS_TOKEN_KEY.concat(accessToken), loginUser, ACCESS_TOKEN_TTL, TimeUnit.MINUTES);
        redisUtil.set(REFRESH_TOKEN_KEY.concat(refreshToken), loginUser, SecurityRedisConstants.REFRESH_TOKEN_TTL, TimeUnit.MINUTES);
    }

    public void delLoginUser(String accessToken, String refreshToken) {
        delAccessToken(accessToken);
        delRefreshToken(refreshToken);
    }

    public void delAccessToken(String accessToken) {
        if (StrUtil.isNotBlank(accessToken)) {
            redisUtil.del(ACCESS_TOKEN_KEY.concat(accessToken));
        }
    }

    public void delRefreshToken(String refreshToken) {
        if (StrUtil.isNotBlank(refreshToken)) {
            redisUtil.del(REFRESH_TOKEN_KEY.concat(refreshToken));
        }
    }

    public void offlineUser(Long userId) {
        //根据前缀获取所有的accessTokenKey和refreshTokenKey
        Set<String> accessTokenKeys = redisUtil.getKeysByPrefix(ACCESS_TOKEN_KEY);
        Set<String> refreshTokenKeys = redisUtil.getKeysByPrefix(REFRESH_TOKEN_KEY);
        //批量获取用户
        List<LoginUser> loginUsers = redisUtil.getByKeys(CollUtil.unionAll(accessTokenKeys, refreshTokenKeys), LoginUser.class);
        //筛选出用户的AccessToken和RefreshToken
        List<String> removeKeys = new ArrayList<>();
        for (LoginUser loginUser : loginUsers) {
            if (Objects.equals(loginUser.getId(), userId)) {
                removeKeys.add(ACCESS_TOKEN_KEY.concat(loginUser.getAccessToken()));
                removeKeys.add(REFRESH_TOKEN_KEY.concat(loginUser.getRefreshToken()));
            }
        }
        //清除token
        if (CollUtil.isNotEmpty(removeKeys)) {
            redisUtil.del(removeKeys);
        }
    }
}
