package top.sheepyu.module.system.dao.log;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.sheepyu.framework.mybatisplus.core.model.BaseModel;

import java.time.LocalDateTime;

/**
 * @author ygq
 * @date 2023-01-18 11:23
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SystemApiLog extends BaseModel {
    private Long id;
    private Long userId;
    private Integer userType;
    private String name;
    private String type;
    private String requestMethod;
    private String requestUrl;
    private String requestParams;
    private String userIp;
    private Integer duration;
    private Integer resultCode;
    private String resultData;
    private Integer error;
    private LocalDateTime exceptionTime;
    private String exceptionName;
    private String exceptionRootCaseMessage;
    private String exceptionStackTraceFull;
    private String exceptionStackTraceCrucial;
    private Integer processStatus;
    private LocalDateTime processTime;
    private Long processUserId;
}
