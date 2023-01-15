package top.sheepyu.system.controller.admin.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.sheepyu.common.common.Result;

/**
 * @author ygq
 * @date 2023-01-14 15:58
 **/
@RestController
@RequestMapping("/demo")
@Slf4j
public class DemoController {
    @GetMapping
    public Result<Boolean> demo() {
        return Result.success(true);
    }
}
