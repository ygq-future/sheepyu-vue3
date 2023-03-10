package top.sheepyu.module.system.service.codegen;

import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.codegen.vo.SystemCodegenQueryVo;
import top.sheepyu.module.system.dao.codegen.SystemCodegenTable;

import javax.validation.Valid;

/**
 * @author ygq
 * @date 2023-03-01 21:12
 **/
public interface SystemCodegenTableService extends IServiceX<SystemCodegenTable> {
    SystemCodegenTable findById(Long id);

    PageResult<SystemCodegenTable> page(@Valid SystemCodegenQueryVo queryVo);
}
