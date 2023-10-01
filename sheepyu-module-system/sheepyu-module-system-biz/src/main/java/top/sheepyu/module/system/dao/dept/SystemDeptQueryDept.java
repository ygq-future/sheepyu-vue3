package top.sheepyu.module.system.dao.dept;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ygq
 * @date 2023-01-29 16:12
 **/
@Data
@Accessors(chain = true)
public class SystemDeptQueryDept {
    private Long id;
    private Long sourceDeptId;
    private Long targetDeptId;
}
