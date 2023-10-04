package top.sheepyu.module.system.controller.admin.permission.menu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.sheepyu.module.common.annotations.InEnum;
import top.sheepyu.module.common.enums.CommonStatusEnum;

/**
 * @author ygq
 * @date 2023-01-30 15:28
 **/
@Data
@ApiModel("系统菜单查询vo")
public class SystemMenuQueryVo {
    @ApiModelProperty("根据名字模糊匹配")
    private String keyword;

    @ApiModelProperty("菜单状态")
    @InEnum(CommonStatusEnum.class)
    private Integer status;

    @ApiModelProperty("父级菜单id")
    private Long parentId;
}
