package top.sheepyu.module.system.controller.admin.file.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import top.sheepyu.framework.file.core.enums.FileUploadCompleteEnum;
import top.sheepyu.module.common.annotations.InEnum;
import top.sheepyu.module.common.common.PageParam;

import java.util.Date;

import static top.sheepyu.module.common.constants.CommonConstants.DATE_TIME_FORMAT;

/**
 * @author ygq
 * @date 2023-01-17 10:37
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("文件表分页查询vo")
public class SystemFileQueryVo extends PageParam {
    @ApiModelProperty("文件大小, 字节")
    private Long[] sizes;

    @ApiModelProperty("是否完成")
    @InEnum(FileUploadCompleteEnum.class)
    private Integer complete;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    private Date[] createTimes;
}
