package top.sheepyu.module.system.dao.dict;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.sheepyu.framework.mybatisplus.core.model.BaseModel;

import java.util.List;

/**
 * @author ygq
 * @date 2023-01-24 13:46
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SystemDictType extends BaseModel {
    private Long id;
    private String type;
    private String name;
    private Integer status;
    private String remark;

    @TableField(exist = false)
    private List<SystemDictData> dictDataList;
}
