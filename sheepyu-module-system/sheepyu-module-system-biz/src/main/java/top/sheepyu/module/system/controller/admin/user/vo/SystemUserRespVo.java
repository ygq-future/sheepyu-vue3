package top.sheepyu.module.system.controller.admin.user.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ygq
 * @date 2023-01-18 17:06
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统用户修改vo")
public class SystemUserRespVo extends SystemUserBaseVo {
    private Long id;
    private String loginIp;
    private String loginTime;
}
