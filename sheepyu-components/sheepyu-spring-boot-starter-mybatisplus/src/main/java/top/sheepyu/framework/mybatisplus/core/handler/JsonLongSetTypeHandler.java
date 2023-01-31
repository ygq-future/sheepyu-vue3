package top.sheepyu.framework.mybatisplus.core.handler;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;

import java.util.Set;

/**
 * @author ygq
 * @date 2023-01-30 18:24
 **/
public class JsonLongSetTypeHandler extends AbstractJsonTypeHandler<Object> {
    private static final TypeReference<Set<Long>> TYPE_REFERENCE = new TypeReference<Set<Long>>() {
    };

    @Override
    protected Object parse(String json) {
        return JSONUtil.toBean(json, TYPE_REFERENCE, false);
    }

    @Override
    protected String toJson(Object obj) {
        return JSONUtil.toJsonStr(obj);
    }
}
