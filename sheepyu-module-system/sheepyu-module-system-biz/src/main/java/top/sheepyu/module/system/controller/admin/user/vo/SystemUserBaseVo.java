package top.sheepyu.module.system.controller.admin.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.sheepyu.module.common.annotations.InEnum;
import top.sheepyu.module.common.enums.CommonStatusEnum;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author ygq
 * @date 2023-01-18 15:03
 **/
@Data
@ApiModel("系统用户基本vo")
public class SystemUserBaseVo {
    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("电话号码")
    private String mobile;

    @ApiModelProperty("头像url")
    private String avatar;

    @ApiModelProperty("状态")
    @NotNull(message = "状态不能为空")
    @InEnum(CommonStatusEnum.class)
    private Integer status;

    @ApiModelProperty("所属部门-职位")
    private Set<Long> deptIds;

    @ApiModelProperty("备注")
    private String remark;
}
