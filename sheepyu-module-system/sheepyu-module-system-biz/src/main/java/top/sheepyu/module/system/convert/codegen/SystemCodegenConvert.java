package top.sheepyu.module.system.convert.codegen;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.sheepyu.module.system.controller.admin.codegen.vo.SystemCodegenRespVo;
import top.sheepyu.module.system.controller.admin.codegen.vo.SystemCodegenUpdateVo;
import top.sheepyu.module.system.dao.codegen.SystemCodegenTable;

/**
 * @author ygq
 * @date 2023-03-04 11:11
 **/
@Mapper
public interface SystemCodegenConvert {
    SystemCodegenConvert CONVERT = Mappers.getMapper(SystemCodegenConvert.class);

    SystemCodegenRespVo convert(SystemCodegenTable table);

    SystemCodegenTable convert(SystemCodegenUpdateVo updateVo);
}
