package top.sheepyu.module.system.controller.admin.user.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author ygq
 * @date 2023-03-14 22:20
 **/
@Data
@ApiModel("用户修改密码vo")
public class SystemUpdatePassVo {
    private String oldPass;
    private String newPass;
}
