package top.sheepyu.module.system.service.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.framework.security.util.SecurityFrameworkUtil;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.api.log.ApiLogDto;
import top.sheepyu.module.system.controller.admin.log.api.SystemApiLogQueryVo;
import top.sheepyu.module.system.dao.log.SystemApiLog;
import top.sheepyu.module.system.dao.log.SystemApiLogMapper;

import java.util.Date;
import java.util.Objects;

import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.*;
import static top.sheepyu.module.system.convert.log.SystemApiLogConvert.CONVERT;
import static top.sheepyu.module.system.enums.log.ApiLogProcessEnum.PROCESSED;
import static top.sheepyu.module.system.enums.log.ApiLogProcessEnum.UNPROCESSED;
import static top.sheepyu.module.system.enums.log.ApiLogStatusEnum.EXCEPTION;
import static top.sheepyu.module.system.enums.log.ApiLogStatusEnum.NORMAL;

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
        apiLog.setStatus(apiLog.getExceptionTime() != null ? EXCEPTION.getCode() : NORMAL.getCode());
        //默认处理都是未处理的状态
        apiLog.setProcessStatus(UNPROCESSED.getCode());
        save(apiLog);
    }

    @Override
    public PageResult<SystemApiLog> pageApiLog(SystemApiLogQueryVo queryVo) {
        return page(queryVo, buildQuery()
                .eqIfPresent(SystemApiLog::getType, queryVo.getType())
                .betweenIfPresent(SystemApiLog::getDuration, queryVo.getDurations())
                .betweenIfPresent(SystemApiLog::getCreateTime, queryVo.getCreateTimes())
                .eqIfPresent(SystemApiLog::getStatus, queryVo.getStatus())
                .eqIfPresent(SystemApiLog::getProcessStatus, queryVo.getProcessStatus())
                .likeIfPresent(SystemApiLog::getName, queryVo.getKeyword())
                .orderByDesc(SystemApiLog::getCreateTime));
    }

    @Override
    public void process(Long id) {
        SystemApiLog apiLog = findByIdValidateExists(id);
        //已经处理过的不用处理
        if (Objects.equals(apiLog.getProcessStatus(), PROCESSED.getCode())) {
            throw exception(ALREADY_HANDLE);
        }

        //成功的api不需要处理
        if (Objects.equals(apiLog.getStatus(), NORMAL.getCode())) {
            throw exception(SUCCESS_API_DONT_HANDLE);
        }

        apiLog.setProcessStatus(PROCESSED.getCode()).setProcessTime(new Date());
        apiLog.setProcessUserId(SecurityFrameworkUtil.getLoginUserId());
        updateById(apiLog);
    }

    private SystemApiLog findByIdValidateExists(Long id) {
        return findByIdValidateExists(id, API_LOG_NOT_EXISTS);
    }

}
