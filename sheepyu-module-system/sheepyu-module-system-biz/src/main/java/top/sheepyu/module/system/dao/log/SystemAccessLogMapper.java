package top.sheepyu.module.system.dao.log;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author ygq
 * @date 2023-01-20 16:55
 **/
public interface SystemAccessLogMapper extends BaseMapper<SystemAccessLog> {
    List<Long> countByWeek(@Param("beginWeek") Date beginWeek, @Param("endWeek") Date endWeek);
}
