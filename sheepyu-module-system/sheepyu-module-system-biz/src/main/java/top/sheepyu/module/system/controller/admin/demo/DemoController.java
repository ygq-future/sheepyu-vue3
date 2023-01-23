package top.sheepyu.module.system.controller.admin.demo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.sheepyu.framework.web.annotations.FlowLimit;
import top.sheepyu.framework.web.annotations.Idempotent;
import top.sheepyu.module.common.common.PageParam;
import top.sheepyu.module.common.common.Result;

/**
 * @author ygq
 * @date 2023-01-14 15:58
 **/
@RestController
@RequestMapping("/system/demo")
@Slf4j
@Api(tags = "测试")
public class DemoController {
    @GetMapping
    @ApiOperation("测试")
    public Result<Boolean> demo() {
        return Result.success(true);
    }

    @GetMapping("/flow-limit")
    @ApiOperation("测试限流")
    @FlowLimit
    public Result<Boolean> demoOfFlowLimit() {
        return Result.success(true);
    }

    @GetMapping("/idempotent")
    @ApiOperation("测试幂等")
    @Idempotent
    public Result<Boolean> demoOfIdempotent(PageParam param) {
        log.info("param: {}", param);
        return Result.success(true);
    }
}
