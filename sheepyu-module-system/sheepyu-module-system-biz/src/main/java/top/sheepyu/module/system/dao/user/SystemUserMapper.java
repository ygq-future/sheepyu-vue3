package top.sheepyu.module.system.dao.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author ygq
 * @date 2023-01-16 17:24
 **/
public interface SystemUserMapper extends BaseMapper<SystemUser> {
    void removeByUsernameDeleted(@Param("username") String username);
}
