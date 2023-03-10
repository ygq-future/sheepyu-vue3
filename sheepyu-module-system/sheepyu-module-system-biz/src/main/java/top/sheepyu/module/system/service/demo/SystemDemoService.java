package top.sheepyu.module.system.service.demo;

import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.system.controller.admin.demo.vo.SystemDemoCreateVo;
import top.sheepyu.module.system.controller.admin.demo.vo.SystemDemoUpdateVo;
import top.sheepyu.module.system.controller.admin.demo.vo.SystemDemoQueryVo;
import top.sheepyu.module.system.controller.admin.demo.vo.SystemDemoExcelVo;
import top.sheepyu.module.system.dao.demo.SystemDemo;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

/**
* @author ygq
* @date 2023-01-18 14:40
**/
public interface SystemDemoService extends IServiceX<SystemDemo> {
    void create(@Valid SystemDemoCreateVo createVo);

    void update(@Valid SystemDemoUpdateVo updateVo);

    void delete(@NotEmpty(message = "ids不能为空") String ids);

    SystemDemo findById(@NotNull(message = "id不能为空") Long id);

    List<SystemDemo> list(@Valid SystemDemoQueryVo queryVo);

    PageResult<SystemDemo> page(@Valid SystemDemoQueryVo queryVo);

    void batchImport(List<SystemDemoExcelVo> result);
}
