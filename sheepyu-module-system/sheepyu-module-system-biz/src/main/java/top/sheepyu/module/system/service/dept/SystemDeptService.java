package top.sheepyu.module.system.service.dept;

import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.system.controller.admin.dept.vo.SystemDeptCreateVo;
import top.sheepyu.module.system.controller.admin.dept.vo.SystemDeptQueryVo;
import top.sheepyu.module.system.controller.admin.dept.vo.SystemDeptUpdateVo;
import top.sheepyu.module.system.dao.dept.SystemDept;

import javax.validation.Valid;
import java.util.List;

/**
 * @author ygq
 * @date 2023-01-29 17:55
 **/
public interface SystemDeptService extends IServiceX<SystemDept> {
    void createDept(@Valid SystemDeptCreateVo createVo);

    void updateDept(@Valid SystemDeptUpdateVo updateVo);

    void deleteDept(Long id);

    SystemDept findById(Long id);

    List<SystemDept> listDept(@Valid SystemDeptQueryVo queryVo);

    String findNameById(Long deptId);
}
