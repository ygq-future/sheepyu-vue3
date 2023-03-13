package top.sheepyu.module.system.dao.dict;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author ygq
 * @date 2023-01-24 13:47
 **/
public interface SystemDictTypeMapper extends BaseMapper<SystemDictType> {
    boolean deleteRealById(@Param("id") Long id);
}
