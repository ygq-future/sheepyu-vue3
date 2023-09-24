package top.sheepyu.module.system.dao.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author ygq
 * @date 2023-01-16 17:24
 **/
public interface SystemUserMapper extends BaseMapper<SystemUser> {
    void removeByUsernameDeleted(@Param("username") String username);

    List<Long> countByWeek(@Param("beginWeek") Date beginWeek, @Param("endWeek") Date endWeek);
}
