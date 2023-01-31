package top.sheepyu.module.system.controller.admin.post.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.sheepyu.module.common.common.PageParam;

/**
 * @author ygq
 * @date 2023-01-30 16:01
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统职位查询vo")
public class SystemPostQueryVo extends PageParam {
}
