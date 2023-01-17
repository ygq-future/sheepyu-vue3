package top.sheepyu.framework.mybatisplus.core.handler;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import top.sheepyu.framework.mybatisplus.core.model.BaseModel;
import top.sheepyu.framework.web.util.WebFrameworkUtil;

import java.time.LocalDateTime;

/**
 * @author ygq
 * @date 2023-01-15 11:30
 **/
public class DefaultMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        BaseModel baseModel = (BaseModel) metaObject.getOriginalObject();

        String username = WebFrameworkUtil.getLoginUserUsername();
        if (baseModel.getCreator() == null && StrUtil.isNotBlank(username)) {
            baseModel.setCreator(username);
        }

        if (baseModel.getCreateTime() == null) {
            baseModel.setCreateTime(LocalDateTime.now());
        }

        if (baseModel.getUpdateTime() == null) {
            baseModel.setUpdateTime(LocalDateTime.now());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        BaseModel baseModel = (BaseModel) metaObject.getOriginalObject();

        String username = WebFrameworkUtil.getLoginUserUsername();
        if (StrUtil.isNotBlank(username)) {
            baseModel.setUpdater(username);
        }
        baseModel.setUpdateTime(LocalDateTime.now());
    }
}
