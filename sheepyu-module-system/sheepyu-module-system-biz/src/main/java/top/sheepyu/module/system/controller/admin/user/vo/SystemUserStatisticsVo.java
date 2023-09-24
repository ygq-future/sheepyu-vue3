package top.sheepyu.module.system.controller.admin.user.vo;

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
public class SystemUserStatisticsVo {
    @ApiModelProperty("今日注册人数")
    private Long todayIncrement;

    @ApiModelProperty("今日较昨日注册人数增加百分比")
    private Integer todayPercent;

    @ApiModelProperty("总会员人数")
    private Long total;

    @ApiModelProperty("周注册统计")
    private List<Long> weekIncrement;

    @ApiModelProperty("周访问统计")
    private List<Long> weekAccess;

    @ApiModelProperty("最近注册的5个用户")
    private List<SystemUserRespVo> nearUserList;
}
