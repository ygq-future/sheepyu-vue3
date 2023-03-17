package top.sheepyu.module.system.controller.admin.file.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author ygq
 * @date 2023-01-18 15:03
 **/
@Data
@ApiModel("文件表基本vo")
public class SystemFileBaseVo {
    @ApiModelProperty(value = "兼容s3协议唯一上传id")
    private String uploadId;

    @ApiModelProperty(value = "上传分片索引, 0开始", required = true)
    @NotNull(message = "上传索引不能为空")
    private Integer partIndex;

    @ApiModelProperty(value = "文件名", required = true)
    @NotEmpty(message = "文件名不能为空")
    private String filename;

    @ApiModelProperty(value = "文件md5")
    private String md5;

    @ApiModelProperty(value = "文件 URL", required = true)
    @NotEmpty(message = "文件 URL不能为空")
    private String url;

    @ApiModelProperty(value = "MIME类型")
    private String mimeType;

    @ApiModelProperty(value = "文件大小, 字节", required = true)
    @NotNull(message = "文件大小, 字节不能为空")
    private Long size;

    @ApiModelProperty(value = "文件上传地域, 例如: http://localhost")
    private String domain;

    @ApiModelProperty(value = "文件上传相对路径, 例如: /2022/08/xxx.jpg")
    private String path;

    @ApiModelProperty(value = "是否完成", required = true)
    @NotNull(message = "是否完成不能为空")
    private Integer complete;

    @ApiModelProperty(value = "备注")
    private String remark;

}
