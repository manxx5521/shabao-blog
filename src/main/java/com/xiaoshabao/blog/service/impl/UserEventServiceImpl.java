/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.xiaoshabao.blog.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoshabao.blog.dao.UserDao;
import com.xiaoshabao.blog.entity.UserPO;
import com.xiaoshabao.blog.service.UserEventService;

/**
 * @author langhsu on 2015/8/6.
 */
@Service
public class UserEventServiceImpl implements UserEventService {
    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public void identityPost(Long userId, long postId, boolean identity) {
    	Optional<UserPO> opo = userDao.findById(userId);

        if (opo.isPresent()) {
        	UserPO po=opo.get();
            po.setPosts(po.getPosts() + ((identity) ? 1 : -1));
            userDao.save(po);
        }
    }

    @Override
    @Transactional
    public void identityComment(Long userId, long commentId, boolean identity) {
        Optional<UserPO> opo = userDao.findById(userId);

        if (opo.isPresent()) {
        	UserPO po=opo.get();
            po.setComments(po.getComments() + ((identity) ? 1 : -1));
            userDao.save(po);
        }
    }

    @Override
    @Transactional
    public void identityFollow(Long userId, long followId, boolean identity) {
        Optional<UserPO> opo = userDao.findById(userId);

        if (opo.isPresent()) {
        	UserPO po=opo.get();
            po.setFollows(po.getFollows() + ((identity) ? 1 : -1));
            userDao.save(po);
        }
    }

    @Override
    @Transactional
    public void identityFans(Long userId, long fansId, boolean identity) {
        Optional<UserPO> opo = userDao.findById(userId);

        if (opo.isPresent()) {
        	UserPO po=opo.get();
            po.setFans(po.getFans() + ((identity) ? 1 : -1));
            userDao.save(po);
        }
    }

    @Override
    @Transactional
    public void identityFavors(Long userId, boolean identity, int targetType, long targetId) {
    	Optional<UserPO> opo = userDao.findById(userId);
        if (opo.isPresent()) {
        	UserPO po=opo.get();
            po.setFavors(po.getFavors() + ((identity) ? 1 : -1));
            userDao.save(po);
        }
    }
}
