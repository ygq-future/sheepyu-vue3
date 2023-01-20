package top.sheepyu.module.system.service.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.module.system.dao.log.SystemApiLog;
import top.sheepyu.module.system.dao.log.SystemApiLogMapper;
import top.sheepyu.module.system.dto.ApiLogDto;

import static top.sheepyu.module.common.enums.CommonStatusEnum.FALSE;
import static top.sheepyu.module.common.enums.CommonStatusEnum.TRUE;
import static top.sheepyu.module.system.convert.log.SystemApiLogConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-01-18 11:26
 **/
@Service
@Slf4j
@Validated
public class SystemApiLogServiceImpl extends ServiceImplX<SystemApiLogMapper, SystemApiLog> implements SystemApiLogService {

    @Async
    @Override
    public void createApiLog(ApiLogDto apiLogDto) {
        SystemApiLog apiLog = CONVERT.convert(apiLogDto);
        //异常时间不等于空说明发生了异常
        apiLog.setError(apiLog.getExceptionTime() != null ? TRUE.getCode() : FALSE.getCode());
        //默认处理都是未处理的状态
        apiLog.setProcessStatus(FALSE.getCode());
        save(apiLog);
    }
}
