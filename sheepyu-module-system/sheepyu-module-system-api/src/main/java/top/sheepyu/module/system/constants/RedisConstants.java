package top.sheepyu.module.system.constants;

/**
 * @author ygq
 * @date 2023-01-19 16:06
 **/
public interface RedisConstants {
    /**
     * 图形验证码redis key前缀 和 ttl
     */
    String CAPTCHA_IMAGE_KEY = "captcha:image:";
    long CAPTCHA_IMAGE_TTL = 2;

    /**
     * 系统配置redis key前缀
     */
    String SYSTEM_CONFIG_KEY = "system:config";

    /**
     * 系统字典redis key前缀
     */
    String SYSTEM_DICT_KEY = "system:dict:";
}
