package top.sheepyu.module.system.service.log;

import com.baomidou.mybatisplus.extension.service.IService;
import top.sheepyu.module.system.dao.log.SystemApiLog;
import top.sheepyu.module.system.dto.ApiLogDto;

import javax.validation.Valid;

/**
 * @author ygq
 * @date 2023-01-16 17:24
 **/
public interface SystemApiLogService extends IService<SystemApiLog> {
    void createApiLog(@Valid ApiLogDto apiLogDto);
}
