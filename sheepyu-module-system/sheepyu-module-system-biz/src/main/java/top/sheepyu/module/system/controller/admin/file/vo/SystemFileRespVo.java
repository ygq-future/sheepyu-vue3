package top.sheepyu.module.system.controller.admin.file.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ygq
 * @date 2023-01-27 11:35
 **/
@Data
@ApiModel("系统文件响应vo")
public class SystemFileRespVo {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("文件名")
    private String filename;

    @ApiModelProperty("md5")
    private String md5;

    @ApiModelProperty("url")
    private String url;

    @ApiModelProperty("mimeType")
    private String mimeType;

    @ApiModelProperty("size")
    private Integer size;

    @ApiModelProperty("地域")
    private String domain;

    @ApiModelProperty("相对路径")
    private String path;

    @ApiModelProperty("是否完成")
    private Integer complete;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("如果没有完成, 最后一块分片的坐标")
    private Integer index;
}
