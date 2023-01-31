package top.sheepyu.module.system.api.log;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author ygq
 * @date 2023-01-17 16:19
 **/
@Data
@Accessors(chain = true)
public class ApiLogDto {
    private Long userId;
    private Integer userType;
    private String name;
    private Integer type;
    private String requestMethod;
    private String requestUrl;
    private String requestParams;
    private String userIp;
    private Integer duration;
    private Integer resultCode;
    private String resultData;
    private Date exceptionTime;
    private String exceptionName;
    private String exceptionRootCauseMessage;
    private String exceptionStackTraceFull;
    private String exceptionStackTraceCrucial;
}
