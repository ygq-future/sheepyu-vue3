package top.sheepyu.module.system.controller.admin.log.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ygq
 * @date 2023-01-23 20:09
 **/
@Data
@ApiModel("系统API日志响应vo")
public class SystemApiLogRespVo {
    @ApiModelProperty("日志id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户类型")
    private Integer userType;

    @ApiModelProperty("操作名称")
    private String name;

    @ApiModelProperty("操作类型")
    private String type;

    @ApiModelProperty("请求的方法名")
    private String requestMethod;

    @ApiModelProperty("请求的url")
    private String requestUrl;

    @ApiModelProperty("请求的参数")
    private String requestParams;

    @ApiModelProperty("访问的ip")
    private String userIp;

    @ApiModelProperty("方法执行时间")
    private Integer duration;

    @ApiModelProperty("返回码")
    private Integer resultCode;

    @ApiModelProperty("返回数据")
    private String resultData;

    @ApiModelProperty("日志状态 normal or exception")
    private Integer status;

    @ApiModelProperty("发生异常的时间")
    private LocalDateTime exceptionTime;

    @ApiModelProperty("异常的名称")
    private String exceptionName;

    @ApiModelProperty("异常的根消息")
    private String exceptionRootCauseMessage;

    @ApiModelProperty("异常栈的完整信息")
    private String exceptionStackTraceFull;

    @ApiModelProperty("异常栈的关键信息")
    private String exceptionStackTraceCrucial;

    @ApiModelProperty("处理状态")
    private Integer processStatus;

    @ApiModelProperty("处理时间")
    private LocalDateTime processTime;

    @ApiModelProperty("处理人id")
    private Long processUserId;
}
