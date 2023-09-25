package top.sheepyu.module.system.dao.permission.role;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ygq
 * @date 2023-01-29 16:13
 **/
@Data
@Accessors(chain = true)
public class SystemRoleMenu {
    private Long id;
    private Long roleId;
    private Long menuId;
}
