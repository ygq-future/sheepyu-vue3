package top.sheepyu.module.system.convert.demo;

import top.sheepyu.module.common.common.PageResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.sheepyu.module.system.controller.admin.demo.vo.SystemDemoCreateVo;
import top.sheepyu.module.system.controller.admin.demo.vo.SystemDemoRespVo;
import top.sheepyu.module.system.controller.admin.demo.vo.SystemDemoUpdateVo;
import top.sheepyu.module.system.controller.admin.demo.vo.SystemDemoExcelVo;
import top.sheepyu.module.system.dao.demo.SystemDemo;

import java.util.List;

/**
* @author sheepyu
* @date 2023-03-10 21:40:22
**/
@Mapper
public interface SystemDemoConvert {
    SystemDemoConvert CONVERT = Mappers.getMapper(SystemDemoConvert.class);

    SystemDemo convert(SystemDemoCreateVo createVo);

    SystemDemo convert(SystemDemoUpdateVo updateVo);

    SystemDemoRespVo convert(SystemDemo systemDemo);

    PageResult<SystemDemoRespVo> convertPage(PageResult<SystemDemo> pageResult);

    List<SystemDemoExcelVo> convert2Excel(List<SystemDemo> list);

    List<SystemDemo> convertExcel2(List<SystemDemoExcelVo> list);
}
