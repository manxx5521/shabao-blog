package com.xiaoshabao.blog.dao;

import com.xiaoshabao.blog.dto.Feeds;


/**
 */
public interface FeedsDaoCustom {
    /**
     * 添加动态, 同时会分发给粉丝
     *
     * @param feeds
     * @return
     */
    int batchAdd(Feeds feeds);
}
