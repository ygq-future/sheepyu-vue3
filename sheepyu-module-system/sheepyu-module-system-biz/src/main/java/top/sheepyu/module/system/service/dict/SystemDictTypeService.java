package top.sheepyu.module.system.service.dict;

import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.dict.type.SystemDictTypeCreateVo;
import top.sheepyu.module.system.controller.admin.dict.type.SystemDictTypeQueryVo;
import top.sheepyu.module.system.controller.admin.dict.type.SystemDictTypeUpdateVo;
import top.sheepyu.module.system.dao.dict.SystemDictType;

import javax.validation.Valid;
import java.util.List;

/**
 * @author ygq
 * @date 2023-01-24 13:49
 **/
public interface SystemDictTypeService extends IServiceX<SystemDictType> {
    void createDictType(@Valid SystemDictTypeCreateVo createVo);

    String updateDictType(@Valid SystemDictTypeUpdateVo updateVo);

    PageResult<SystemDictType> pageDictType(@Valid SystemDictTypeQueryVo queryVo);

    SystemDictType findDictType(Long id);

    boolean existsByType(String type);

    List<String> typeList();

    List<SystemDictType> listDictType();
}
