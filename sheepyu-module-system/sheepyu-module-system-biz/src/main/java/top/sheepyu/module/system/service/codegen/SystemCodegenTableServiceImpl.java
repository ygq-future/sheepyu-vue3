package top.sheepyu.module.system.service.codegen;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.codegen.vo.SystemCodegenQueryVo;
import top.sheepyu.module.system.dao.codegen.SystemCodegenTable;
import top.sheepyu.module.system.dao.codegen.SystemCodegenTableMapper;

import static top.sheepyu.module.system.constants.ErrorCodeConstants.CODEGEN_NOT_EXISTS;

/**
 * @author ygq
 * @date 2023-03-01 21:13
 **/
@Service
@Validated
@Slf4j
public class SystemCodegenTableServiceImpl extends ServiceImplX<SystemCodegenTableMapper, SystemCodegenTable> implements SystemCodegenTableService {

    @Override
    public SystemCodegenTable findById(Long id) {
        return findByIdThrowIfNotExists(id);
    }

    @Override
    public PageResult<SystemCodegenTable> page(SystemCodegenQueryVo queryVo) {
        return page(queryVo, buildQuery()
                .likeIfPresent(SystemCodegenTable::getTableName, queryVo.getKeyword())
                .eqIfPresent(SystemCodegenTable::getScene, queryVo.getScene())
                .betweenIfPresent(SystemCodegenTable::getCreateTime, queryVo.getCreateTimes())
                .orderByDesc(SystemCodegenTable::getCreateTime));
    }

    private SystemCodegenTable findByIdThrowIfNotExists(Long id) {
        return findByIdThrowIfNotExists(id, CODEGEN_NOT_EXISTS);
    }
}
