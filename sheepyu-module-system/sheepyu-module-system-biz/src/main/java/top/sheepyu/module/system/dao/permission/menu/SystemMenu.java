package top.sheepyu.module.system.dao.permission.menu;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.sheepyu.framework.mybatisplus.core.model.BaseModel;

import java.util.List;

/**
 * @author ygq
 * @date 2023-01-29 16:13
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SystemMenu extends BaseModel {
    private Long id;
    private String name;
    private String permission;
    private Integer type;
    private Integer sort;
    private Long parentId;
    private String path;
    private String icon;
    private String component;
    private Integer status;
    private Integer keepAlive;

    @TableField(exist = false)
    private List<SystemMenu> children;
}
