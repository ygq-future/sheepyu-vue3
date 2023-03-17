package top.sheepyu.module.system.controller.admin.config.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.sheepyu.module.common.common.PageParam;

/**
* @author ygq
* @date 2023-01-17 10:37
**/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("配置分页查询vo")
public class SystemConfigQueryVo extends PageParam  {
}
