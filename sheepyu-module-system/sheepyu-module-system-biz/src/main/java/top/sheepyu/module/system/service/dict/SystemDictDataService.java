package top.sheepyu.module.system.service.dict;

import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.system.controller.admin.dict.data.SystemDictDataCreateVo;
import top.sheepyu.module.system.controller.admin.dict.data.SystemDictDataUpdateVo;
import top.sheepyu.module.system.dao.dict.SystemDictData;

import javax.validation.Valid;
import java.util.List;

/**
 * @author ygq
 * @date 2023-01-24 13:48
 **/
public interface SystemDictDataService extends IServiceX<SystemDictData> {
    boolean existsByTypeId(Long id);

    SystemDictData createDictData(SystemDictDataCreateVo createVo);

    SystemDictData updateDictData(@Valid SystemDictDataUpdateVo updateVo);

    Long batchDeleteDictData(String ids);

    /**
     * 查询字典列表数据
     *
     * @param typeId 类型id
     * @param open   是否查询状态为开启的字典数据
     * @return 返回字典集合数据
     */
    List<SystemDictData> listDictData(Long typeId, boolean open);

    SystemDictData findDictData(Long id);
}
