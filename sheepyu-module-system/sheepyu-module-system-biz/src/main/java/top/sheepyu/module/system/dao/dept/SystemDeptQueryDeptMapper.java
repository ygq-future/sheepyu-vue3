package top.sheepyu.module.system.dao.dept;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static top.sheepyu.module.common.util.CollectionUtil.convertSet;

/**
 * @author ygq
 * @date 2023-01-29 17:54
 **/
public interface SystemDeptQueryDeptMapper extends BaseMapper<SystemDeptQueryDept> {
    default Set<Long> findByTargetIds(Set<Long> targetDeptIds) {
        List<SystemDeptQueryDept> queryDeptList = selectList(new LambdaQueryWrapper<SystemDeptQueryDept>()
                .in(SystemDeptQueryDept::getTargetDeptId, targetDeptIds)
                .select(SystemDeptQueryDept::getSourceDeptId));
        return convertSet(queryDeptList, SystemDeptQueryDept::getSourceDeptId);
    }

    default Set<Long> findBySourceId(Long sourceDeptId) {
        List<SystemDeptQueryDept> queryDeptList = selectList(new LambdaQueryWrapper<SystemDeptQueryDept>()
                .eq(SystemDeptQueryDept::getSourceDeptId, sourceDeptId)
                .select(SystemDeptQueryDept::getTargetDeptId));
        return convertSet(queryDeptList, SystemDeptQueryDept::getTargetDeptId);
    }

    default void deleteBySourceId(Long sourceDeptId) {
        delete(new LambdaQueryWrapper<SystemDeptQueryDept>().in(SystemDeptQueryDept::getSourceDeptId, sourceDeptId));
    }

    default void deleteBySourceIdAndTargetIds(Long sourceDeptId, Collection<Long> targetDeptIds) {
        delete(new LambdaQueryWrapper<SystemDeptQueryDept>()
                .eq(SystemDeptQueryDept::getSourceDeptId, sourceDeptId)
                .in(SystemDeptQueryDept::getTargetDeptId, targetDeptIds));
    }

    default void insertSourceIdAndTargetIds(Long sourceDeptId, Collection<Long> targetDeptIds) {
        for (Long targetDeptId : targetDeptIds) {
            insert(new SystemDeptQueryDept().setSourceDeptId(sourceDeptId).setTargetDeptId(targetDeptId));
        }
    }
}
