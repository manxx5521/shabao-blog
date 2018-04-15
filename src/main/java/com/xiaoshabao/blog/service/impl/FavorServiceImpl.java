package com.xiaoshabao.blog.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.xiaoshabao.blog.dao.FavorDao;
import com.xiaoshabao.blog.dto.Favor;
import com.xiaoshabao.blog.dto.Post;
import com.xiaoshabao.blog.entity.FavorPO;
import com.xiaoshabao.blog.service.FavorService;
import com.xiaoshabao.blog.service.PostService;
import com.xiaoshabao.blog.util.BeanMapUtils;

/**
 * @author langhsu on 2015/8/31.
 */
@Service
public class FavorServiceImpl implements FavorService {
    @Autowired
    private FavorDao favorDao;
    @Autowired
    private PostService postService;

    @Override
    @Transactional
    public void add(long userId, long postId) {
        FavorPO po = favorDao.findByOwnIdAndPostId(userId, postId);

        Assert.isNull(po, "已经喜欢过此文章了");

        // 如果没有喜欢过, 则添加记录
        po = new FavorPO();
        po.setOwnId(userId);
        po.setPostId(postId);
        po.setCreated(new Date());

        favorDao.save(po);
    }

    @Override
    @Transactional
    public void delete(long userId, long postId) {
        FavorPO po = favorDao.findByOwnIdAndPostId(userId, postId);
        Assert.notNull(po, "还没有喜欢过此文章");
        favorDao.delete(po);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Favor> pagingByOwnId(Pageable pageable, long ownId) {
        Page<FavorPO> page = favorDao.findAllByOwnIdOrderByCreatedDesc(pageable, ownId);

        List<Favor> rets = new ArrayList<>();
        Set<Long> postIds = new HashSet<>();
        for (FavorPO po : page.getContent()) {
            rets.add(BeanMapUtils.copy(po));
            postIds.add(po.getPostId());
        }

        if (postIds.size() > 0) {
            Map<Long, Post> posts = postService.findMapByIds(postIds);

            for (Favor t : rets) {
                Post p = posts.get(t.getPostId());
                t.setPost(p);
            }
        }
        return new PageImpl<>(rets, pageable, page.getTotalElements());
    }
}
