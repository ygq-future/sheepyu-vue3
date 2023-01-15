package top.sheepyu.framework.security.core.constants;

/**
 * @author ygq
 * @date 2023-01-15 14:46
 **/
public interface SecurityRedisConstants {
    String ACCESS_TOKEN_KEY = "user:token:";
    Long ACCESS_TOKEN_TTL = 30L;
    String REFRESH_TOKEN_KEY = "user:refreshToken:";
    Long REFRESH_TOKEN_TTL = 24L * 60;
}
