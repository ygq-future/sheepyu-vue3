package top.sheepyu.module.system.service.dept;

import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.system.controller.admin.dept.vo.SystemDeptCreateVo;
import top.sheepyu.module.system.controller.admin.dept.vo.SystemDeptQueryVo;
import top.sheepyu.module.system.controller.admin.dept.vo.SystemDeptUpdateVo;
import top.sheepyu.module.system.dao.dept.SystemDept;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * @author ygq
 * @date 2023-01-29 17:55
 **/
public interface SystemDeptService extends IServiceX<SystemDept> {
    Long createDept(@Valid SystemDeptCreateVo createVo);

    void updateDept(@Valid SystemDeptUpdateVo updateVo);

    void deleteDept(Long id);

    SystemDept findById(Long id);

    /**
     * 查询部门表中所有数据包含职位, 用于列表展示
     *
     * @param queryVo queryVo
     * @return List<SystemDept>
     */
    List<SystemDept> listDept(@Valid SystemDeptQueryVo queryVo);

    /**
     * 查询部门表中类型为部门的数据, 用于下拉框树形选择
     *
     * @return List<SystemDept>
     */
    List<SystemDept> tree();

    List<SystemDept> deptListToTree(List<SystemDept> list);

    List<String> findNamesByIds(Long userId, Set<Long> postIds);

    /**
     * 查询此用户部门及子部门的用户id
     *
     * @param userId userId
     * @return userId Set
     */
    Set<Long> deepQueryUserIdByUserId(Long userId);

    /**
     * 查询此用户部门及子部门的部门id
     *
     * @param userId userId
     * @return deptId Set
     */
    Set<Long> deepQueryDeptIdByUserId(Long userId);

    /**
     * 查询指定部门下的所有用户id
     *
     * @param deptIds 部门id集合
     * @return userId Set
     */
    Set<Long> deepQueryUserIdByDeptId(Set<Long> deptIds);

    /**
     * 查询此部门及子部门的部门id
     *
     * @param deptIds deptIds
     * @return deptId Set
     */
    Set<Long> deepQueryDeptIdByDeptId(Set<Long> deptIds);

    boolean isOwn(Long loginUserId, Long deptId);
}
