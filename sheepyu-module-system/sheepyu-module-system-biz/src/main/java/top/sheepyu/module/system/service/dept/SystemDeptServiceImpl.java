package top.sheepyu.module.system.service.dept;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.module.system.controller.admin.dept.vo.SystemDeptCreateVo;
import top.sheepyu.module.system.controller.admin.dept.vo.SystemDeptQueryVo;
import top.sheepyu.module.system.controller.admin.dept.vo.SystemDeptUpdateVo;
import top.sheepyu.module.system.dao.dept.SystemDept;
import top.sheepyu.module.system.dao.dept.SystemDeptMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.DEPT_HAS_CHILDREN;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.DEPT_NOT_EXISTS;
import static top.sheepyu.module.system.convert.dept.SystemDeptConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-01-29 17:55
 **/
@Service
@Slf4j
@Validated
@AllArgsConstructor
public class SystemDeptServiceImpl extends ServiceImplX<SystemDeptMapper, SystemDept> implements SystemDeptService {
    @Override
    public void createDept(SystemDeptCreateVo createVo) {
        SystemDept dept = CONVERT.convert(createVo);
        save(dept);
    }

    @Override
    public void updateDept(SystemDeptUpdateVo updateVo) {
        SystemDept dept = CONVERT.convert(updateVo);
        updateById(dept);
    }

    @Transactional
    @Override
    public void deleteDept(Long id) {
        if (existsChildren(id)) {
            throw exception(DEPT_HAS_CHILDREN);
        }
        removeById(id);
    }

    @Override
    public SystemDept findById(Long id) {
        return findByIdValidateExists(id);
    }

    @Override
    public List<SystemDept> listDept(SystemDeptQueryVo queryVo) {
        String keyword = queryVo.getKeyword();
        List<SystemDept> list = list(buildQuery()
                .eqIfPresent(SystemDept::getName, keyword)
                .likeIfPresent(SystemDept::getEmail, keyword)
                .likeIfPresent(SystemDept::getPhone, keyword)
                .orderByAsc(SystemDept::getSort));

        List<SystemDept> result = new ArrayList<>();
        for (SystemDept dept : list) {
            if (Objects.equals(dept.getParentId(), 0L)) {
                result.add(dept);
            }
        }

        //填充数据
        for (SystemDept dept : result) {
            dept.setChildren(fillTreeData(list, dept.getId()));
        }

        return result;
    }

    @Override
    public String findNameById(Long deptId) {
        SystemDept one = lambdaQuery().eq(SystemDept::getId, deptId)
                .select(SystemDept::getName).one();
        return one == null ? null : one.getName();
    }

    /**
     * 递归填充树形数据
     *
     * @param list 全部数据
     * @param id   查找此id所有的子部门
     * @return 返回子部门
     */
    private List<SystemDept> fillTreeData(List<SystemDept> list, Long id) {
        List<SystemDept> result = new ArrayList<>();

        for (SystemDept dept : list) {
            if (Objects.equals(dept.getParentId(), id)) {
                result.add(dept);
            }
        }

        for (SystemDept dept : result) {
            dept.setChildren(fillTreeData(list, dept.getId()));
        }

        return result.isEmpty() ? null : result;
    }

    public boolean existsChildren(Long parentId) {
        return lambdaQuery().eq(SystemDept::getParentId, parentId).exists();
    }

    private SystemDept findByIdValidateExists(Long id) {
        return findByIdValidateExists(id, DEPT_NOT_EXISTS);
    }

}
