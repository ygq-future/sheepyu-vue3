package top.sheepyu.framework.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.sheepyu.framework.web.util.WebFrameworkUtil;
import top.sheepyu.module.common.util.ServletUtil;

/**
 * @author ygq
 * @date 2023-01-20 16:23
 **/
@Getter
@AllArgsConstructor
public enum KeyPrefixTypeEnum {
    NONE(-1, "没有前缀"),
    USER(0, "基于用户ID"),
    IP(1, "基于客户端IP"),
    ;
    private final int code;
    private final String desc;

    public String getPrefix() {
        switch (this) {
            case NONE:
                return "";
            case USER:
                return WebFrameworkUtil.getLoginUserId().toString();
            case IP:
                return ServletUtil.getClientIp(WebFrameworkUtil.getRequest());
        }
        return null;
    }
}
