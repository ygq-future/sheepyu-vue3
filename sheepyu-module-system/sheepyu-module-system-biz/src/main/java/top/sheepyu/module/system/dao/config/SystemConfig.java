package top.sheepyu.module.system.dao.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.sheepyu.framework.mybatisplus.core.model.BaseModel;

/**
 * @author ygq
 * @date 2023-01-21 14:21
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SystemConfig extends BaseModel {
    private Long id;
    private String name;
    private String configKey;
    private String configValue;
    private String remark;
}
