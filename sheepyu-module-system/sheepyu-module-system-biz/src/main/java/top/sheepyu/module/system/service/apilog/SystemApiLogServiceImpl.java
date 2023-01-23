package top.sheepyu.module.system.service.apilog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.framework.security.util.SecurityFrameworkUtil;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.apilog.vo.SystemApiLogQueryVo;
import top.sheepyu.module.system.dao.apilog.SystemApiLog;
import top.sheepyu.module.system.dao.apilog.SystemApiLogMapper;
import top.sheepyu.module.system.dto.ApiLogDto;

import java.time.LocalDateTime;
import java.util.Objects;

import static top.sheepyu.module.common.enums.CommonStatusEnum.FALSE;
import static top.sheepyu.module.common.enums.CommonStatusEnum.TRUE;
import static top.sheepyu.module.common.exception.CommonException.exception;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.*;
import static top.sheepyu.module.system.convert.apilog.SystemApiLogConvert.CONVERT;

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

    @Override
    public PageResult<SystemApiLog> pageApiLog(SystemApiLogQueryVo queryVo) {
        return page(queryVo, buildQuery()
                .eqIfPresent(SystemApiLog::getType, queryVo.getType())
                .betweenIfPresent(SystemApiLog::getDuration, queryVo.getDurations())
                .eqIfPresent(SystemApiLog::getError, queryVo.getError())
                .eqIfPresent(SystemApiLog::getProcessStatus, queryVo.getProcessStatus())
                .likeIfPresent(SystemApiLog::getName, queryVo.getKeyword()));
    }

    @Override
    public void process(Long id) {
        SystemApiLog apiLog = findByIdValidateExists(id);
        //已经处理过的不用处理
        if (Objects.equals(apiLog.getProcessStatus(), TRUE.getCode())) {
            throw exception(ALREADY_HANDLE);
        }

        //成功的api不需要处理
        if (Objects.equals(apiLog.getError(), FALSE.getCode())) {
            throw exception(SUCCESS_API_DONT_HANDLE);
        }

        apiLog.setProcessStatus(TRUE.getCode()).setProcessTime(LocalDateTime.now());;
        apiLog.setProcessUserId(SecurityFrameworkUtil.getLoginUserId());
        updateById(apiLog);
    }

    private SystemApiLog findByIdValidateExists(Long id) {
        return findByIdValidateExists(id, API_LOG_NOT_EXISTS);
    }

}
