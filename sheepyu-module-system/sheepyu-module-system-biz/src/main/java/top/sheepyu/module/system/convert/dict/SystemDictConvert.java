package top.sheepyu.module.system.convert.dict;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.dict.data.SystemDictDataCreateVo;
import top.sheepyu.module.system.controller.admin.dict.data.SystemDictDataRespVo;
import top.sheepyu.module.system.controller.admin.dict.data.SystemDictDataUpdateVo;
import top.sheepyu.module.system.controller.admin.dict.type.SystemDictTypeCreateVo;
import top.sheepyu.module.system.controller.admin.dict.type.SystemDictTypeRespVo;
import top.sheepyu.module.system.controller.admin.dict.type.SystemDictTypeUpdateVo;
import top.sheepyu.module.system.dao.dict.SystemDictData;
import top.sheepyu.module.system.dao.dict.SystemDictType;

import java.util.List;

/**
 * @author ygq
 * @date 2023-01-24 14:23
 **/
@Mapper
public interface SystemDictConvert {
    SystemDictConvert CONVERT = Mappers.getMapper(SystemDictConvert.class);

    SystemDictData convert(SystemDictDataCreateVo createVo);

    SystemDictData convert(SystemDictDataUpdateVo updateVo);

    SystemDictDataRespVo convert(SystemDictData dictData);

    List<SystemDictDataRespVo> convertList(List<SystemDictData> dictData);

    SystemDictType convert(SystemDictTypeCreateVo createVo);

    SystemDictType convert(SystemDictTypeUpdateVo updateVo);

    SystemDictTypeRespVo convert(SystemDictType dictType);

    PageResult<SystemDictTypeRespVo> convertPage(PageResult<SystemDictType> pageResult);
}
