package top.sheepyu.framework.file.config;

import cn.hutool.core.collection.CollUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import top.sheepyu.framework.file.core.enums.FileUploadTypeEnum;
import top.sheepyu.framework.file.core.oss.FileUpload;
import top.sheepyu.module.system.api.ConfigApi;
import top.sheepyu.module.system.enums.SystemConfigKeyEnum;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static top.sheepyu.module.common.constants.ErrorCodeConstants.FILE_UPLOAD_DONT_MATCH;
import static top.sheepyu.module.common.exception.CommonException.exception;

/**
 * @author ygq
 * @date 2023-01-25 16:58
 **/
public class FileUploadFactory implements ApplicationContextAware, SmartInitializingSingleton {
    @Resource
    private ConfigApi configApi;
    private ApplicationContext applicationContext;
    private static final Map<String, FileUpload> FILE_UPLOAD_MAP = new HashMap<>();

    @Override
    public void afterSingletonsInstantiated() {
        //在所有bean创建完成之后调用,不然如果FileUpload的实现类在本类初始化之前的话
        //getBeansOfType就会找不到
        init();
    }

    public void init() {
        Map<String, FileUpload> fileUploadMap = applicationContext.getBeansOfType(FileUpload.class);
        if (CollUtil.isNotEmpty(fileUploadMap)) {
            fileUploadMap.forEach((k, v) -> FILE_UPLOAD_MAP.put(v.getType(), v));
        }
    }

    public FileUpload get() {
        String type = configApi.get(SystemConfigKeyEnum.DEFAULT_FILE_UPLOAD);
        return get(FileUploadTypeEnum.value(type));
    }

    public FileUpload get(FileUploadTypeEnum type) {
        FileUpload fileUpload = FILE_UPLOAD_MAP.get(type.getCode());
        if (fileUpload == null) {
            throw exception(FILE_UPLOAD_DONT_MATCH);
        }
        return fileUpload;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
