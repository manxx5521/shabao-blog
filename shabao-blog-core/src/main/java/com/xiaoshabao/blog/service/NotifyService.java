package com.xiaoshabao.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.xiaoshabao.blog.dto.Notify;

/**
 * @author langhsu on 2015/8/31.
 */
public interface NotifyService {

    Page<Notify> findByOwnId(Pageable pageable, long ownId);

    /**
     * 发送通知
     * @param notify
     */
    void send(Notify notify);

    /**
     * 未读消息数量
     * @param ownId
     * @return
     */
    int unread4Me(long ownId);

    /**
     * 标记为已读
     * @param ownId
     */
    void readed4Me(long ownId);

}
