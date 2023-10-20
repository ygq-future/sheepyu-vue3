package top.sheepyu.module.system.dao.permission.role;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.sheepyu.framework.mybatisplus.core.model.BaseModel;

/**
 * @author ygq
 * @date 2023-01-29 16:12
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SystemRole extends BaseModel {
    private Long id;
    private Long deptId;
    @TableField(exist = false)
    private String deptName;
    private String name;
    private String code;
    private Integer sort;
    private String remark;
}
