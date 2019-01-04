package com.xiaoshabao.blog.util;

import org.springframework.beans.BeanUtils;

import com.xiaoshabao.blog.dto.AccountProfile;
import com.xiaoshabao.blog.dto.AuthMenu;
import com.xiaoshabao.blog.dto.Channel;
import com.xiaoshabao.blog.dto.Comment;
import com.xiaoshabao.blog.dto.Favor;
import com.xiaoshabao.blog.dto.Feeds;
import com.xiaoshabao.blog.dto.Notify;
import com.xiaoshabao.blog.dto.Post;
import com.xiaoshabao.blog.dto.Role;
import com.xiaoshabao.blog.dto.User;
import com.xiaoshabao.blog.entity.AuthMenuPO;
import com.xiaoshabao.blog.entity.ChannelPO;
import com.xiaoshabao.blog.entity.CommentPO;
import com.xiaoshabao.blog.entity.FavorPO;
import com.xiaoshabao.blog.entity.FeedsPO;
import com.xiaoshabao.blog.entity.NotifyPO;
import com.xiaoshabao.blog.entity.PostPO;
import com.xiaoshabao.blog.entity.RolePO;
import com.xiaoshabao.blog.entity.UserPO;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class BeanMapUtils {
	public static String[] USER_IGNORE = new String[]{"password", "extend", "roles"};

	public static String[] POST_IGNORE_LIST = new String[]{"markdown", "content"};

	public static User copy(UserPO po, int level) {
		if (po == null) {
			return null;
		}
		User ret = new User();
		BeanUtils.copyProperties(po, ret, USER_IGNORE);
		
		if (level > 0) {
			List<RolePO> rolePOs = po.getRoles();
			List<Role> roles = new ArrayList<Role>();
			for(RolePO rolePo :rolePOs){
				Role role = copy(rolePo);
				roles.add(role);
			}
			ret.setRoles(roles);
		}
		return ret;
	}

	public static AccountProfile copyPassport(UserPO po) {
		AccountProfile passport = new AccountProfile(po.getId(), po.getUsername());
		passport.setName(po.getName());
		passport.setEmail(po.getEmail());
		passport.setAvatar(po.getAvatar());
		passport.setLastLogin(po.getLastLogin());
		passport.setStatus(po.getStatus());
		passport.setActiveEmail(po.getActiveEmail());

		List<AuthMenu> menus = new ArrayList<AuthMenu>();
		List<RolePO> rolePOs = po.getRoles();
		List<Role> roles = new ArrayList<Role>();
		for(RolePO rolePo :rolePOs){
			Role role = copy(rolePo);
			roles.add(role);
		}
		for(Role role : roles){
			List<AuthMenu> authMenus = role.getAuthMenus();
			menus.addAll(authMenus);
		}
		passport.setAuthMenus(menus);
		return passport;
	}
	public static Comment copy(CommentPO po) {
		Comment ret = new Comment();
		BeanUtils.copyProperties(po, ret);
		return ret;
	}

	public static Post copy(PostPO po, int level) {
		Post d = new Post();
		if (level > 0) {
			BeanUtils.copyProperties(po, d);
		} else {
			BeanUtils.copyProperties(po, d, POST_IGNORE_LIST);
		}
		return d;
	}

	public static Channel copy(ChannelPO po) {
		Channel r = new Channel();
		BeanUtils.copyProperties(po, r);
		return r;
	}

	public static AuthMenu copy(AuthMenuPO po) {
		AuthMenu am = new AuthMenu();
		BeanUtils.copyProperties(po, am, "children");
		return am;
	}

	public static Role copy(RolePO po) {
		Role r = new Role();
		BeanUtils.copyProperties(po, r, "users", "authMenus");
		List<AuthMenu> authMenus = new ArrayList<>();
		for (AuthMenuPO authMenuPO : po.getAuthMenus()) {
			AuthMenu authMenu = new AuthMenu();
			BeanUtils.copyProperties(authMenuPO, authMenu, "roles", "children");
			authMenus.add(authMenu);
		}
		r.setAuthMenus(authMenus);
		return r;
	}
	
	public static Notify copy(NotifyPO po) {
		Notify ret = new Notify();
		BeanUtils.copyProperties(po, ret);
		return ret;
	}
	
	
	public static Feeds copy(FeedsPO po) {
		Feeds ret = new Feeds();
		BeanUtils.copyProperties(po, ret);
		return ret;
	}

	

	public static Favor copy(FavorPO po) {
		Favor ret = new Favor();
		BeanUtils.copyProperties(po, ret);
		return ret;
	}
	

}
