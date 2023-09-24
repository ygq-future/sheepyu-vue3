package top.sheepyu.module.system.controller.admin.file.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ygq
 * @date 2023-03-19 15:01
 **/
@ApiModel("系统用户统计vo")
@Data
public class SystemFileStatisticsVo {
    @ApiModelProperty("今日上传量")
    private Long todayIncrement;

    @ApiModelProperty("今日较昨日上传量增加百分比")
    private Integer todayPercent;

    @ApiModelProperty("总上传量")
    private Long total;

    /**
     * 第一层list代表一周每一天, 第二层代表每天的 图片:0(下标), 文档: 1, 影音: 2, 压缩包: 3 的数量
     */
    @ApiModelProperty("周上传统计")
    private List<List<Long>> weekIncrement;
}
