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
        String username = getLoginUserUsername();
        Date now = new Date();

        if (hasProperty(metaObject, CREATOR)) {
            Object creator = metaObject.getValue(CREATOR);
            if (creator == null && StrUtil.isNotBlank(username)) {
                metaObject.setValue(CREATOR, username);
            }
        }

        if (hasProperty(metaObject, CREATE_TIME)) {
            Object createTime = metaObject.getValue(CREATE_TIME);
            if (createTime == null) {
                metaObject.setValue(CREATE_TIME, now);
            }
        }

        if (hasProperty(metaObject, UPDATE_TIME)) {
            Object updateTime = metaObject.getValue(UPDATE_TIME);
            if (updateTime == null) {
                metaObject.setValue(UPDATE_TIME, now);
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (hasProperty(metaObject, UPDATER)) {
            metaObject.setValue(UPDATER, getLoginUserUsername());
        }
        if (hasProperty(metaObject, UPDATE_TIME)) {
            metaObject.setValue(UPDATE_TIME, new Date());
        }
    }

    private boolean hasProperty(MetaObject metaObject, String propertyName) {
        return metaObject.hasGetter(propertyName) && metaObject.hasSetter(propertyName);
    }
}
