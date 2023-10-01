package top.sheepyu.framework.mybatisplus.core.handler;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

import static top.sheepyu.framework.mybatisplus.core.constants.BaseModelFillField.*;
import static top.sheepyu.framework.web.util.WebFrameworkUtil.getLoginUserUsername;

/**
 * @author ygq
 * @date 2023-01-15 11:30
 **/
public class DefaultMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Object createTime = metaObject.getValue(CREATE_TIME);
        Object updateTime = metaObject.getValue(UPDATE_TIME);
        Object creator = metaObject.getValue(CREATOR);
        String username = getLoginUserUsername();
        Date now = new Date();

        if (creator == null && StrUtil.isNotBlank(username)) {
            metaObject.setValue(CREATOR, username);
        }
        if (createTime == null) {
            metaObject.setValue(CREATE_TIME, now);
        }
        if (updateTime == null) {
            metaObject.setValue(UPDATE_TIME, now);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue(UPDATER, getLoginUserUsername());
        metaObject.setValue(UPDATE_TIME, new Date());
    }
}
