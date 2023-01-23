package top.sheepyu.module.system.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

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
    private LocalDateTime exceptionTime;
    private String exceptionName;
    private String exceptionRootCauseMessage;
    private String exceptionStackTraceFull;
    private String exceptionStackTraceCrucial;
    private Long startTime;
    private boolean close;
}
