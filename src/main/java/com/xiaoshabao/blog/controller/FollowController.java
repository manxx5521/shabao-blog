package com.xiaoshabao.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiaoshabao.base.controller.BaseController;
import com.xiaoshabao.blog.component.shiro.ShiroUtil;
import com.xiaoshabao.blog.dto.AccountProfile;
import com.xiaoshabao.blog.dto.Data;
import com.xiaoshabao.blog.event.NotifyEvent;
import com.xiaoshabao.blog.lang.Consts;
import com.xiaoshabao.blog.service.FollowService;

/**
 * 关注相关
 */
@Controller
@RequestMapping("/user")
public class FollowController extends BaseController {
    @Autowired
    private FollowService followService;
    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping("/follow")
    public @ResponseBody
    Data follow(Long id) {
        Data data = Data.failure("操作失败");
        if (id != null) {
            try {
                AccountProfile up = ShiroUtil.getSubject().getProfile();

                followService.follow(up.getId(), id);

                sendNotify(up.getId(), id);

                data = Data.success("关注成功!", Data.NOOP);
            } catch (Exception e) {
                data = Data.failure(e.getMessage());
            }
        }
        return data;
    }

    @RequestMapping("/unfollow")
    public @ResponseBody Data unfollow(Long id) {
        Data data = Data.failure("操作失败");
        if (id != null) {
            try {
                AccountProfile up = ShiroUtil.getSubject().getProfile();

                followService.unfollow(up.getId(), id);

                data = Data.success("取消成功!", Data.NOOP);
            } catch (Exception e) {
                data = Data.failure(e.getMessage());
            }
        }
        return data;
    }

    @RequestMapping("/check_follow")
    public @ResponseBody Data checkFollow(Long id) {
        Data data = Data.failure("操作失败");
        if (id != null) {
            try {
                AccountProfile up = ShiroUtil.getSubject().getProfile();

                boolean check = followService.checkFollow(up.getId(), id);

                data = Data.success("操作成功!", check);
            } catch (Exception e) {
                data = Data.failure(e.getMessage());
            }
        }
        return data;
    }

    /**
     * 发送关注通知
     * @param userId
     * @param followId
     */
    private void sendNotify(long userId, long followId) {
        NotifyEvent event = new NotifyEvent("NotifyEvent");
        event.setToUserId(followId);
        event.setFromUserId(userId);
        event.setEvent(Consts.NOTIFY_EVENT_FOLLOW);
        applicationContext.publishEvent(event);
    }
}
