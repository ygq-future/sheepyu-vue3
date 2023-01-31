package top.sheepyu.module.system.convert.dept;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.sheepyu.module.system.controller.admin.dept.vo.SystemDeptCreateVo;
import top.sheepyu.module.system.controller.admin.dept.vo.SystemDeptRespVo;
import top.sheepyu.module.system.controller.admin.dept.vo.SystemDeptUpdateVo;
import top.sheepyu.module.system.dao.dept.SystemDept;

import java.util.List;

/**
 * @author ygq
 * @date 2023-01-29 18:01
 **/
@Mapper
public interface SystemDeptConvert {
    SystemDeptConvert CONVERT = Mappers.getMapper(SystemDeptConvert.class);

    SystemDept convert(SystemDeptCreateVo createVo);

    SystemDept convert(SystemDeptUpdateVo updateVo);

    SystemDeptRespVo convert(SystemDept systemConfig);

    List<SystemDeptRespVo> convertList(List<SystemDept> list);
}
