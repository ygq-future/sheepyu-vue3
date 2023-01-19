package top.sheepyu.module.system.dao.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.sheepyu.framework.mybatisplus.core.model.BaseModel;

import java.time.LocalDateTime;

/**
 * @author ygq
 * @date 2023-01-18 14:36
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SystemUser extends BaseModel {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String mobile;
    private String avatar;
    private Integer status;
    private Integer type;
    private String remark;
    private String loginIp;
    private LocalDateTime loginTime;
}
