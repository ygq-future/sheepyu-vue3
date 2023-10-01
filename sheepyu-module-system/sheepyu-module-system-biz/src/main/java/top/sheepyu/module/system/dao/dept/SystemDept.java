package top.sheepyu.module.system.dao.dept;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.sheepyu.framework.mybatisplus.core.model.BaseModel;

import java.util.List;
import java.util.Set;

/**
 * @author ygq
 * @date 2023-01-29 16:12
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SystemDept extends BaseModel {
    private Long id;
    private String name;
    private Long parentId;
    private Integer sort;
    @TableField(exist = false)
    private Set<Long> leaderUserIds;
    @TableField(exist = false)
    private String leaderNicknames;
    @TableField(exist = false)
    private Set<Long> targetDeptIds;
    @TableField(exist = false)
    private Boolean disabled;
    private Integer type;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String phone;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String email;

    @TableField(exist = false)
    private List<SystemDept> children;
}
