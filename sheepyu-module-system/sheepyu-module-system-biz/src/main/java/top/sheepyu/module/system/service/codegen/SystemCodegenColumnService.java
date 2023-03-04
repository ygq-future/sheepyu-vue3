package top.sheepyu.module.system.service.codegen;

import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.system.dao.codegen.SystemCodegenColumn;

import java.util.List;

/**
 * @author ygq
 * @date 2023-03-01 21:12
 **/
public interface SystemCodegenColumnService extends IServiceX<SystemCodegenColumn> {
    List<SystemCodegenColumn> listByTableId(Long id);

    void removeByTableId(Long id);
}
