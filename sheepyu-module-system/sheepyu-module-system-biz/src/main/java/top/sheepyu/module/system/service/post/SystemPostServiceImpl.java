package top.sheepyu.module.system.service.post;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import top.sheepyu.framework.mybatisplus.core.query.ServiceImplX;
import top.sheepyu.module.common.common.PageResult;
import top.sheepyu.module.system.controller.admin.post.vo.SystemPostCreateVo;
import top.sheepyu.module.system.controller.admin.post.vo.SystemPostQueryVo;
import top.sheepyu.module.system.controller.admin.post.vo.SystemPostUpdateVo;
import top.sheepyu.module.system.dao.post.SystemPost;
import top.sheepyu.module.system.dao.post.SystemPostMapper;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static top.sheepyu.module.system.constants.ErrorCodeConstants.POST_EXISTS;
import static top.sheepyu.module.system.constants.ErrorCodeConstants.POST_NOT_EXISTS;
import static top.sheepyu.module.system.convert.post.SystemPostConvert.CONVERT;

/**
 * @author ygq
 * @date 2023-01-29 17:56
 **/
@Service
@Slf4j
@Validated
@AllArgsConstructor
public class SystemPostServiceImpl extends ServiceImplX<SystemPostMapper, SystemPost> implements SystemPostService {
    @Override
    public void createPost(SystemPostCreateVo createVo) {
        SystemPost post = CONVERT.convert(createVo);
        checkRepeatByFieldThrow(post, POST_EXISTS, SystemPost::getName);
        save(post);
    }

    @Override
    public void updatePost(SystemPostUpdateVo updateVo) {
        SystemPost post = CONVERT.convert(updateVo);
        updateById(post);
    }

    @Transactional
    @Override
    public void deletePost(String ids) {
        batchDelete(ids, SystemPost::getId);
    }

    @Override
    public PageResult<SystemPost> pagePost(SystemPostQueryVo queryVo) {
        return page(queryVo, buildQuery()
                .likeIfPresent(SystemPost::getName, queryVo.getKeyword())
                .orderByAsc(SystemPost::getSort));
    }

    @Override
    public List<SystemPost> listPost() {
        return list(buildQuery().orderByAsc(SystemPost::getSort));
    }

    @Override
    public SystemPost findById(Long id) {
        return findByIdValidateExists(id);
    }

    @Override
    public List<String> findNamesByIds(Set<Long> postIds) {
        if (CollUtil.isEmpty(postIds)) {
            return Collections.emptyList();
        }

        return lambdaQuery().in(SystemPost::getId, postIds)
                .select(SystemPost::getName).list()
                .stream().map(SystemPost::getName)
                .collect(Collectors.toList());
    }

    private SystemPost findByIdValidateExists(Long id) {
        return findByIdValidateExists(id, POST_NOT_EXISTS);
    }

}
