package top.sheepyu.module.system.dao.joblog;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.sheepyu.framework.mybatisplus.core.model.BaseModel;

import java.time.LocalDateTime;

/**
 * @author ygq
 * @date 2023-01-16 17:20
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SystemJobLog extends BaseModel {
    private Long id;
    private Long jobId;
    private String handlerName;
    private String handlerParam;
    private Integer retryCount;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Integer duration;
    private Integer status;
    private String result;
}
