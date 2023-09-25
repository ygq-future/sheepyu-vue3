package top.sheepyu.module.system.service.dept;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.framework.security.util.SecurityFrameworkUtil;
import top.sheepyu.module.system.controller.admin.dept.vo.SystemDeptCreateVo;
import top.sheepyu.module.system.controller.admin.dept.vo.SystemDeptQueryVo;
import top.sheepyu.module.system.controller.admin.dept.vo.SystemDeptUpdateVo;
import top.sheepyu.module.system.dao.dept.SystemDept;
import top.sheepyu.module.system.dao.dept.SystemDeptMapper;
import top.sheepyu.module.system.dao.user.SystemUserDeptMapper;

import javax.annotation.Resource;
import java.util.*;

import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.common.util.CollectionUtil.convertList;
import static top.sheepyu.module.common.util.CollectionUtil.convertSet;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.*;
import static top.sheepyu.module.system.convert.dept.SystemDeptConvert.CONVERT;
import static top.sheepyu.module.system.enums.dept.DeptTypeEnum.DEPT;
import static top.sheepyu.module.system.enums.dept.DeptTypeEnum.POST;

/**
 * @author ygq
 * @date 2023-01-29 17:55
 **/
@Service
@Slf4j
@Validated
@AllArgsConstructor
public class SystemDeptServiceImpl extends ServiceImplX<SystemDeptMapper, SystemDept> implements SystemDeptService {
    @Resource
    private SystemUserDeptMapper systemUserDeptMapper;

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
        //操作人不能删除自己所在部门
        if (isLeaderUser(id, SecurityFrameworkUtil.getLoginUserId())) {
            throw exception(DONT_REMOVE_OWN_DEPT);
        }
        if (existsChildren(id)) {
            throw exception(DEPT_HAS_CHILDREN);
        }
        if (systemUserDeptMapper.existsUser(id)) {
            throw exception(DEPT_HAS_USER);
        }
        removeById(id);
    }

    private boolean isLeaderUser(Long id, Long loginUserId) {
        return lambdaQuery()
                .eq(SystemDept::getId, id)
                .eq(SystemDept::getLeaderUserId, loginUserId)
                .exists();
    }

    @Override
    public SystemDept findById(Long id) {
        return findByIdThrowIfNotExists(id);
    }

    @Override
    public List<SystemDept> listDept(SystemDeptQueryVo queryVo) {
        String keyword = queryVo.getKeyword();
        //没有超管就只能查询自己管理部门之下的
        Set<Long> queryDeptIds = null;
        if (!SecurityFrameworkUtil.isSuperAdmin()) {
            queryDeptIds = deepQueryDeptIdByUserId(SecurityFrameworkUtil.getLoginUserId());
        }
        List<SystemDept> list = list(buildQuery()
                .inIfPresent(SystemDept::getId, queryDeptIds)
                .and(StrUtil.isNotBlank(keyword), e -> e
                        .like(SystemDept::getEmail, keyword).or()
                        .like(SystemDept::getName, keyword).or()
                        .like(SystemDept::getPhone, keyword)).or()
                .eq(StrUtil.isNotBlank(keyword), SystemDept::getType, POST.getCode())
                .orderByAsc(SystemDept::getSort));
        return deptListToTree(list);
    }

    @Override
    public List<SystemDept> tree() {
        Set<Long> queryDeptIds = deepQueryDeptIdByUserId(SecurityFrameworkUtil.getLoginUserId());
        List<SystemDept> list = list(buildQuery()
                .inIfPresent(SystemDept::getId, queryDeptIds)
                .eqIfPresent(SystemDept::getType, DEPT.getCode())
                .orderByAsc(SystemDept::getSort));
        List<SystemDept> treeData = deptListToTree(list);
        if (SecurityFrameworkUtil.isSuperAdmin()) {
            return Collections.singletonList(
                    new SystemDept().setId(0L).setName("顶级部门").setChildren(treeData)
            );
        }
        return treeData;
    }

    private List<SystemDept> deptListToTree(List<SystemDept> list) {
        //返回结果
        List<SystemDept> result = new ArrayList<>();
        //把所以的id抽取出来
        Set<Long> deptIds = convertSet(list, SystemDept::getId);
        //循环递归封装数据
        for (SystemDept dept : list) {
            //筛选类型为部门且在当前返回数据中没有上级的部门, 以此为顶级来封装树形数据
            if (Objects.equals(dept.getType(), DEPT.getCode()) && !deptIds.contains(dept.getParentId())) {
                //递归封装数据
                dept.setChildren(fillTreeData(list, dept.getId()));
                result.add(dept);
            }
        }
        return result;
    }

    @Override
    public List<String> findNamesByIds(Long userId, Set<Long> postIds) {
        List<SystemDept> list = list(buildQuery()
                .inIfPresent(SystemDept::getId, postIds)
                .eq(SystemDept::getType, POST.getCode()).or()
                .eq(SystemDept::getLeaderUserId, userId)
                .select(SystemDept::getName, SystemDept::getType, SystemDept::getParentId));
        return convertList(list, e -> {
            if (Objects.equals(e.getType(), DEPT.getCode())) return e.getName() + "管理员";
            return getById(e.getParentId()).getName() + "-" + e.getName();
        });
    }

    @Override
    public Set<Long> listDeptIdByUserId(Long userId) {
        List<SystemDept> list = list(buildQuery()
                .eq(SystemDept::getType, DEPT.getCode())
                .eq(SystemDept::getLeaderUserId, userId)
                .select(SystemDept::getId));
        return convertSet(list, SystemDept::getId);
    }

    @Override
    public Set<Long> deepQueryUserIdByUserId(Long userId) {
        Set<Long> userDeptIds = listDeptIdByUserId(userId);
        Set<Long> userIds = deepQueryUserIdByDeptId(userDeptIds);
        userIds.add(userId);
        return userIds;
    }

    @Override
    public Set<Long> deepQueryDeptIdByUserId(Long userId) {
        Set<Long> userDeptIds = listDeptIdByUserId(userId);
        return deepQueryDeptIdByDeptId(userDeptIds);
    }

    @Override
    public Set<Long> deepQueryUserIdByDeptId(Set<Long> deptIds) {
        Set<Long> deptIdList = deepQueryDeptIdByDeptId(deptIds);
        return systemUserDeptMapper.findUserIdByDeptIds(deptIdList);
    }

    @Override
    public Set<Long> deepQueryDeptIdByDeptId(Set<Long> deptIds) {
        Set<Long> res = new HashSet<>(deptIds);
        List<SystemDept> deptList = list();
        recursiveFlatDeptId(res, deptIds, deptList);
        return res;
    }

    @Override
    public boolean isOwn(Long loginUserId, Long deptId) {
        return lambdaQuery()
                .eq(SystemDept::getLeaderUserId, loginUserId)
                .eq(SystemDept::getId, deptId)
                .exists();
    }

    private void recursiveFlatDeptId(Set<Long> res, Set<Long> deptIds, List<SystemDept> deptList) {
        Set<Long> childrenDept = new HashSet<>();
        for (SystemDept dept : deptList) {
            if (deptIds.contains(dept.getParentId())) {
                childrenDept.add(dept.getId());
                res.add(dept.getId());
            }
        }
        if (CollUtil.isNotEmpty(childrenDept)) recursiveFlatDeptId(res, childrenDept, deptList);
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
                dept.setChildren(fillTreeData(list, dept.getId()));
                result.add(dept);
            }
        }

        return result.isEmpty() ? null : result;
    }

    public boolean existsChildren(Long parentId) {
        return lambdaQuery().eq(SystemDept::getParentId, parentId).exists();
    }

    private SystemDept findByIdThrowIfNotExists(Long id) {
        return findByIdThrowIfNotExists(id, DEPT_NOT_EXISTS);
    }
}
