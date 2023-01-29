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
    boolean existsByType(String type);

    SystemDictData createDictData(SystemDictDataCreateVo createVo);

    SystemDictData updateDictData(@Valid SystemDictDataUpdateVo updateVo);

    String batchDeleteDictData(String ids);

    /**
     * 查询字典列表数据
     *
     * @param type 类型id
     * @param enable 是否查询状态为开启的字典数据
     * @return 返回字典集合数据
     */
    List<SystemDictData> listDictData(String type, boolean enable);

    SystemDictData findDictData(Long id);
}
