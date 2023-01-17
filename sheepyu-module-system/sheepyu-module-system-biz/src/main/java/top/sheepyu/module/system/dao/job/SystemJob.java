package top.sheepyu.module.system.dao.job;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.sheepyu.framework.mybatisplus.core.model.BaseModel;

/**
 * @author ygq
 * @date 2023-01-16 17:44
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SystemJob extends BaseModel {
    private Long id;
    private String name;
    private Integer status;
    private String handlerName;
    private String handlerParam;
    private String cron;
    private Integer retryCount;
    private Integer retryInterval;
}
