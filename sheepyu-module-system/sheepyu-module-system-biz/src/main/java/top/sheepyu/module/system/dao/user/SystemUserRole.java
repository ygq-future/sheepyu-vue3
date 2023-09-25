package top.sheepyu.module.system.dao.user;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ygq
 * @date 2023-01-29 16:12
 **/
@Data
@Accessors(chain = true)
public class SystemUserRole {
    private Long id;
    private Long userId;
    private Long roleId;
}
