package top.sheepyu.module.system.controller.admin.post;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.common.common.Result;
import top.sheepyu.module.system.controller.admin.post.vo.SystemPostCreateVo;
import top.sheepyu.module.system.controller.admin.post.vo.SystemPostQueryVo;
import top.sheepyu.module.system.controller.admin.post.vo.SystemPostRespVo;
import top.sheepyu.module.system.controller.admin.post.vo.SystemPostUpdateVo;
import top.sheepyu.module.system.dao.post.SystemPost;
import top.sheepyu.module.system.service.post.SystemPostService;

import javax.annotation.Resource;
import java.util.List;

import static top.sheepyu.module.common.common.Result.success;
import static top.sheepyu.module.system.convert.post.SystemPostConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-01-14 15:58
 **/
@RestController
@RequestMapping("/system/post")
@Slf4j
@Api(tags = "管理端 - 系统职位")
public class SystemPostController {
    @Resource
    private SystemPostService systemPostService;

    @PostMapping
    @ApiOperation("添加系统职位")
    @PreAuthorize("@ss.hasPermission('system:post:create')")
    public Result<Boolean> create(@RequestBody SystemPostCreateVo createVo) {
        systemPostService.createPost(createVo);
        return success(true);
    }

    @PutMapping
    @ApiOperation("修改系统职位")
    @PreAuthorize("@ss.hasPermission('system:post:update')")
    public Result<Boolean> update(@RequestBody SystemPostUpdateVo updateVo) {
        systemPostService.updatePost(updateVo);
        return success(true);
    }

    @DeleteMapping("/{ids}")
    @ApiOperation("删除系统职位")
    @PreAuthorize("@ss.hasPermission('system:post:delete')")
    public Result<Boolean> delete(@PathVariable String ids) {
        systemPostService.deletePost(ids);
        return success(true);
    }

    @GetMapping("/page")
    @ApiOperation("获取系统职位分页")
    @PreAuthorize("@ss.hasPermission('system:post:query')")
    public Result<PageResult<SystemPostRespVo>> list(SystemPostQueryVo queryVo) {
        PageResult<SystemPost> result = systemPostService.pagePost(queryVo);
        return success(CONVERT.convertPage(result));
    }

    @GetMapping
    @ApiOperation("获取系统职位列表")
    @PreAuthorize("@ss.hasPermission('system:post:query')")
    public Result<List<SystemPostRespVo>> list() {
        return success(CONVERT.convertList(systemPostService.listPost()));
    }

    @GetMapping("/{id}")
    @ApiOperation("获取指定系统职位")
    @PreAuthorize("@ss.hasPermission('system:post:query')")
    public Result<SystemPostRespVo> findById(@PathVariable Long id) {
        SystemPost post = systemPostService.findById(id);
        return success(CONVERT.convert(post));
    }
}
