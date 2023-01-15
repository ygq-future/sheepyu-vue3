package top.sheepyu.mybatisplus.core.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import top.sheepyu.mybatisplus.core.model.BaseModel;
import top.sheepyu.web.util.WebFrameworkUtil;

import java.time.LocalDateTime;

/**
 * @author ygq
 * @date 2023-01-15 11:30
 **/
public class DefaultMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        BaseModel baseModel = (BaseModel) metaObject.getOriginalObject();

        Long loginUserId = WebFrameworkUtil.getLoginUserId();
        if (baseModel.getCreator() == null && loginUserId != null) {
            baseModel.setCreator(loginUserId.toString());
        }

        if (baseModel.getUpdater() == null && loginUserId != null) {
            baseModel.setUpdater(loginUserId.toString());
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

        Long loginUserId = WebFrameworkUtil.getLoginUserId();
        if (baseModel.getUpdater() == null && loginUserId != null) {
            baseModel.setUpdater(loginUserId.toString());
        }

        if (baseModel.getUpdateTime() == null) {
            baseModel.setUpdateTime(LocalDateTime.now());
        }
    }
}
