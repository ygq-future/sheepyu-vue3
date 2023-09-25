package top.sheepyu.module.system.service.demo;

import lombok.extern.slf4j.Slf4j;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.demo.vo.SystemDemoCreateVo;
import top.sheepyu.module.system.controller.admin.demo.vo.SystemDemoQueryVo;
import top.sheepyu.module.system.controller.admin.demo.vo.SystemDemoUpdateVo;
import top.sheepyu.module.system.controller.admin.demo.vo.SystemDemoExcelVo;
import top.sheepyu.module.system.dao.demo.SystemDemo;
import top.sheepyu.module.system.dao.demo.SystemDemoMapper;

import java.util.*;

import static top.sheepyu.module.system.constants.ErrorCodeConstants.DEMO_NOT_EXISTS;
import static top.sheepyu.module.system.convert.demo.SystemDemoConvert.CONVERT;

/**
* @author sheepyu
* @date 2023-03-10 21:40:22
**/
@Service
@Slf4j
@Validated
public class SystemDemoServiceImpl extends ServiceImplX<SystemDemoMapper, SystemDemo> implements SystemDemoService {
    @Override
    public void create(SystemDemoCreateVo createVo) {
        SystemDemo systemDemo = CONVERT.convert(createVo);
        save(systemDemo);
    }

    @Override
    public void update(SystemDemoUpdateVo updateVo) {
        SystemDemo systemDemo = CONVERT.convert(updateVo);
        updateById(systemDemo);
    }

    @Override
    public void delete(String ids) {
        batchDelete(ids, SystemDemo::getId);
    }

    @Override
    public SystemDemo findById(Long id) {
        return findByIdThrowIfNotExists(id);
    }

    @Override
    public List<SystemDemo> list(SystemDemoQueryVo queryVo) {
        return list(buildQuery()
                .and(StrUtil.isNotBlank(queryVo.getKeyword()), q -> q
                        .eq(SystemDemo::getId, queryVo.getKeyword()).or()
                        .like(SystemDemo::getName, queryVo.getKeyword()))
                .eqIfPresent(SystemDemo::getStatus, queryVo.getStatus())
                .betweenIfPresent(SystemDemo::getBeginTime, queryVo.getBeginTimes())
                .betweenIfPresent(SystemDemo::getCreateTime, queryVo.getCreateTimes())
                .eqIfPresent(SystemDemo::getAge, queryVo.getAge())
                .eqIfPresent(SystemDemo::getSex, queryVo.getSex())
                .orderByDesc(SystemDemo::getId)           );
    }

    @Override
    public PageResult<SystemDemo> page(SystemDemoQueryVo queryVo) {
        return page(queryVo, buildQuery()
                .and(StrUtil.isNotBlank(queryVo.getKeyword()), q -> q
                        .eq(SystemDemo::getId, queryVo.getKeyword()).or()
                        .like(SystemDemo::getName, queryVo.getKeyword()))
                .eqIfPresent(SystemDemo::getStatus, queryVo.getStatus())
                .betweenIfPresent(SystemDemo::getBeginTime, queryVo.getBeginTimes())
                .betweenIfPresent(SystemDemo::getCreateTime, queryVo.getCreateTimes())
                .eqIfPresent(SystemDemo::getAge, queryVo.getAge())
                .eqIfPresent(SystemDemo::getSex, queryVo.getSex())
                .orderByDesc(SystemDemo::getId)           );
    }

    @Override
    public void batchImport(List<SystemDemoExcelVo> result) {
        if (CollUtil.isEmpty(result)) {
            return;
        }
        List<SystemDemo> list = CONVERT.convertExcel2(result);
        saveBatch(list);
    }

    private SystemDemo findByIdThrowIfNotExists(Long id) {
        return findByIdThrowIfNotExists(id, DEMO_NOT_EXISTS);
    }
}
