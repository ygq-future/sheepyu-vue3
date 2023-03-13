package top.sheepyu.module.system.service.post;

import top.sheepyu.framework.mybatisplus.core.query.IServiceX;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.post.vo.SystemPostCreateVo;
import top.sheepyu.module.system.controller.admin.post.vo.SystemPostQueryVo;
import top.sheepyu.module.system.controller.admin.post.vo.SystemPostUpdateVo;
import top.sheepyu.module.system.dao.post.SystemPost;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * @author ygq
 * @date 2023-01-29 17:55
 **/
public interface SystemPostService extends IServiceX<SystemPost> {
    void createPost(@Valid SystemPostCreateVo createVo);

    void updatePost(@Valid SystemPostUpdateVo updateVo);

    void deletePost(String ids);

    PageResult<SystemPost> pagePost(SystemPostQueryVo queryVo);

    List<SystemPost> listPost();

    SystemPost findById(Long id);

    List<String> findNamesByIds(Set<Long> postIds);
}
