package top.sheepyu.module.system.service.codegen;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.module.system.dao.codegen.SystemCodegenColumn;
import top.sheepyu.module.system.dao.codegen.SystemCodegenColumnMapper;

import java.util.List;

/**
 * @author ygq
 * @date 2023-03-01 21:12
 **/
@Service
@Validated
@Slf4j
public class SystemCodegenColumnServiceImpl extends ServiceImplX<SystemCodegenColumnMapper, SystemCodegenColumn> implements SystemCodegenColumnService {

    @Override
    public List<SystemCodegenColumn> listByTableId(Long id) {
        return lambdaQuery().eq(SystemCodegenColumn::getTableId, id).list();
    }

    @Override
    public void removeByTableId(Long id) {
        lambdaUpdate().eq(SystemCodegenColumn::getTableId, id).remove();
    }
}
