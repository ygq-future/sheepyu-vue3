package top.sheepyu.module.system.dao.dept;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.sheepyu.framework.mybatisplus.core.model.BaseModel;

import java.util.List;

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
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long leaderUserId;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String leaderNickname;
    private Integer type;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String phone;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String email;

    @TableField(exist = false)
    private List<SystemDept> children;
}
