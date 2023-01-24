package top.sheepyu.module.system.dao.log;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.sheepyu.framework.mybatisplus.core.model.BaseModel;

/**
 * @author ygq
 * @date 2023-01-20 16:52
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SystemAccessLog extends BaseModel {
    private Long id;
    private Long userId;
    private Integer userType;
    private Integer loginType;
    private Integer loginResult;
    private String username;
    private String nickname;
    private String userIp;
    private String userAgent;
}
