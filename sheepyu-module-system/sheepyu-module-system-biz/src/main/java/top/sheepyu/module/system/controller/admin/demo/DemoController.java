package top.sheepyu.module.system.controller.admin.demo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
