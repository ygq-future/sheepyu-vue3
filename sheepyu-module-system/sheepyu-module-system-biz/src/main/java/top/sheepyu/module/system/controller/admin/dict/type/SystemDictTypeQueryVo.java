package top.sheepyu.module.system.controller.admin.dict.type;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.sheepyu.module.common.annotations.InEnum;
import top.sheepyu.module.common.common.PageParam;
import top.sheepyu.module.common.enums.status.VisibleStatusEnum;

/**
 * @author ygq
 * @date 2023-01-24 15:27
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("字典类型查询vo")
public class SystemDictTypeQueryVo extends PageParam {
    @InEnum(VisibleStatusEnum.class)
    private Integer visible;
}
