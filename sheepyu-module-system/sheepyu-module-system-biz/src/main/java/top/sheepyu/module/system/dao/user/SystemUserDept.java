package top.sheepyu.module.system.dao.user;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ygq
 * @date 2023-01-29 16:12
 **/
@Data
@Accessors(chain = true)
public class SystemUserDept {
    private Long id;
    private Long userId;
    private Long deptId;
    @TableField(exist = false)
    private Long parentDeptId;
}
