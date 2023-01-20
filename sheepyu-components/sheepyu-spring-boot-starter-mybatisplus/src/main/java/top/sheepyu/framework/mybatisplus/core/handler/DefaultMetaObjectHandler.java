package top.sheepyu.framework.mybatisplus.core.handler;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import top.sheepyu.framework.mybatisplus.core.model.BaseModel;
import top.sheepyu.framework.web.util.WebFrameworkUtil;

import java.util.Date;

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
            baseModel.setCreateTime(new Date());
        }

        if (baseModel.getUpdateTime() == null) {
            baseModel.setUpdateTime(new Date());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        BaseModel baseModel = (BaseModel) metaObject.getOriginalObject();

        String username = WebFrameworkUtil.getLoginUserUsername();
        if (StrUtil.isNotBlank(username)) {
            baseModel.setUpdater(username);
        }
        baseModel.setUpdateTime(new Date());
    }
}
