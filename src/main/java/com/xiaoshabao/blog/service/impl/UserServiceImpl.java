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

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.xiaoshabao.blog.dao.RoleDao;
import com.xiaoshabao.blog.dao.UserDao;
import com.xiaoshabao.blog.dto.AccountProfile;
import com.xiaoshabao.blog.dto.AuthMenu;
import com.xiaoshabao.blog.dto.BadgesCount;
import com.xiaoshabao.blog.dto.User;
import com.xiaoshabao.blog.entity.AuthMenuPO;
import com.xiaoshabao.blog.entity.RolePO;
import com.xiaoshabao.blog.entity.UserPO;
import com.xiaoshabao.blog.lang.EntityStatus;
import com.xiaoshabao.blog.lang.MtonsException;
import com.xiaoshabao.blog.service.NotifyService;
import com.xiaoshabao.blog.service.UserService;
import com.xiaoshabao.blog.util.BeanMapUtils;

import java.util.*;

@Service
@Transactional(readOnly = true)
@CacheConfig(cacheNames = "usersCaches")
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private NotifyService notifyService;

	@Autowired
	private RoleDao roleDao;

	@Override
	@Transactional
	public AccountProfile login(String username, String password) {
		UserPO po = userDao.findByUsername(username);
		AccountProfile u = null;

		Assert.notNull(po, "账户不存在");

//		Assert.state(po.getStatus() != Const.STATUS_CLOSED, "您的账户已被封禁");

		Assert.state(StringUtils.equals(po.getPassword(), password), "密码错误");

		po.setLastLogin(Calendar.getInstance().getTime());
		userDao.save(po);
		u = BeanMapUtils.copyPassport(po);

		BadgesCount badgesCount = new BadgesCount();
		badgesCount.setNotifies(notifyService.unread4Me(u.getId()));

		u.setBadgesCount(badgesCount);
		return u;
	}

	@Override
	@Transactional
	public AccountProfile getProfileByName(String username) {
		UserPO po = userDao.findByUsername(username);
		AccountProfile u = null;

		Assert.notNull(po, "账户不存在");

//		Assert.state(po.getStatus() != Const.STATUS_CLOSED, "您的账户已被封禁");
		po.setLastLogin(Calendar.getInstance().getTime());

		u = BeanMapUtils.copyPassport(po);

		BadgesCount badgesCount = new BadgesCount();
		badgesCount.setNotifies(notifyService.unread4Me(u.getId()));

		u.setBadgesCount(badgesCount);

		return u;
	}

	@Override
	@Transactional
	public User register(User user) {
		Assert.notNull(user, "Parameter user can not be null!");

		Assert.hasLength(user.getUsername(), "用户名不能为空!");
//		Assert.hasLength(user.getEmail(), "邮箱不能为空!");
		Assert.hasLength(user.getPassword(), "密码不能为空!");

		UserPO check = userDao.findByUsername(user.getUsername());

		Assert.isNull(check, "用户名已经存在!");

		if (StringUtils.isNotBlank(user.getEmail())) {
			check = userDao.findByEmail(user.getEmail());
			Assert.isNull(check, "邮箱已经被注册!");
		}

		UserPO po = new UserPO();

		BeanUtils.copyProperties(user, po);

		Date now = Calendar.getInstance().getTime();
		po.setPassword(DigestUtils.md5Hex(user.getPassword()));
		po.setStatus(EntityStatus.ENABLED);
		po.setActiveEmail(EntityStatus.ENABLED);
		po.setCreated(now);

		userDao.save(po);

		return BeanMapUtils.copy(po, 0);
	}

	@Override
	@Transactional
	@CacheEvict(key = "#user.getId()")
	public AccountProfile update(User user) {
		Optional<UserPO> opo = userDao.findById(user.getId());
		if (opo.isPresent()) {
			UserPO po=opo.get();
			po.setName(user.getName());
			po.setSignature(user.getSignature());
			userDao.save(po);
			return BeanMapUtils.copyPassport(po);
		}
		return null;
	}

	@Override
	@Transactional
	@CacheEvict(key = "#id")
	public AccountProfile updateEmail(long id, String email) {
		Optional<UserPO> opo = userDao.findById(id);
		if (opo.isPresent()) {
			UserPO po=opo.get();
			if (email.equals(po.getEmail())) {
				throw new MtonsException("邮箱地址没做更改");
			}

			UserPO check = userDao.findByEmail(email);

			if (check != null && check.getId() != po.getId()) {
				throw new MtonsException("该邮箱地址已经被使用了");
			}
			po.setEmail(email);
			po.setActiveEmail(EntityStatus.ENABLED);

			userDao.save(po);
			return BeanMapUtils.copyPassport(po);
		}
		return null;
	}

	@Override
	@Cacheable(key = "#userId")
	public User get(long userId) {
		Optional<UserPO> opo= userDao.findById(userId);
		User ret = null;
		if (opo.isPresent()) {
			ret = BeanMapUtils.copy(opo.get(), 0);
		}
		return ret;
	}

	
	@Override
	public List<User> findHotUserByfans(){
		List<User> rets = new ArrayList<>();
		List<UserPO> list = userDao.findTop12ByOrderByFansDesc();
		for (UserPO po : list) {
			User u = BeanMapUtils.copy(po , 0);
			rets.add(u);
		}
		return rets;
	}
	
	@Override
	public User getByUsername(String username) {
		UserPO po = userDao.findByUsername(username);
		User ret = null;
		if (po != null) {
			ret = BeanMapUtils.copy(po, 0);
		}
		return ret;
	}

	@Override
	@Transactional
	@CacheEvict(key = "#id")
	public AccountProfile updateAvatar(long id, String path) {
		Optional<UserPO> opo= userDao.findById(id);
		if (opo.isPresent()) {
			UserPO po=opo.get();
			po.setAvatar(path);
			userDao.save(po);
			return BeanMapUtils.copyPassport(po);
		}
		return null;
	}

	@Override
	@Transactional
	public void updatePassword(long id, String newPassword) {
		Optional<UserPO> opo= userDao.findById(id);

		Assert.hasLength(newPassword, "密码不能为空!");

		if (opo.isPresent()) {
			UserPO po=opo.get();
			po.setPassword(DigestUtils.md5Hex(newPassword));
			userDao.save(po);
		}
	}

	@Override
	@Transactional
	public void updatePassword(long id, String oldPassword, String newPassword) {
		Optional<UserPO> opo= userDao.findById(id);

		Assert.hasLength(newPassword, "密码不能为空!");

		if (opo.isPresent()) {
			UserPO po=opo.get();
			Assert.isTrue(DigestUtils.md5Hex(oldPassword).equals(po.getPassword()), "当前密码不正确");
			po.setPassword(DigestUtils.md5Hex(newPassword));
			userDao.save(po);
		}
	}

	@Override
	@Transactional
	public void updateStatus(long id, int status) {
		Optional<UserPO> opo= userDao.findById(id);

		if (opo.isPresent()) {
			UserPO po=opo.get();
			po.setStatus(status);
			userDao.save(po);
		}
	}

	@Override
	@Transactional
	public AccountProfile updateActiveEmail(long id, int activeEmail) {
		Optional<UserPO> opo= userDao.findById(id);

		if (opo.isPresent()) {
			UserPO po=opo.get();
			po.setActiveEmail(activeEmail);
			userDao.save(po);
			return BeanMapUtils.copyPassport(po);
		}
		return null;
	}

	@Override
	@Transactional
	public void updateRole(long id, Long[] roleIds) {
		List<RolePO> rolePOs = new ArrayList<>();
		for(Long roleId:roleIds){
			Optional<RolePO> rolePO = roleDao.findById(roleId.longValue());
			rolePOs.add(rolePO.get());
		}
		Optional<UserPO> opo= userDao.findById(id);

		if (opo.isPresent()) {
			UserPO po=opo.get();
			po.setRoles(rolePOs);
			userDao.save(po);
		}
	}

	@Override
	public Page<User> paging(Pageable pageable) {
		Page<UserPO> page = userDao.findAllByOrderByIdDesc(pageable);
		List<User> rets = new ArrayList<>();

		for (UserPO po : page.getContent()) {
			User u = BeanMapUtils.copy(po , 1);
			rets.add(u);
		}

		return new PageImpl<>(rets, pageable, page.getTotalElements());
	}

	@Override
	public Map<Long, User> findMapByIds(Set<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyMap();
		}
		List<UserPO> list = userDao.findAllByIdIn(ids);
		Map<Long, User> ret = new HashMap<>();

		list.forEach(po -> {
			ret.put(po.getId(), BeanMapUtils.copy(po, 0));
		});
		return ret;
	}

	@Override
	public List<AuthMenu> getMenuList(long id) {
		List<AuthMenu> menus = new ArrayList<>();
		Optional<UserPO> opo= userDao.findById(id);
		List<RolePO> roles = opo.get().getRoles();
		for(RolePO role : roles){
			List<AuthMenuPO> menuPOs = role.getAuthMenus();
			for(AuthMenuPO menuPO : menuPOs){
				AuthMenu menu = BeanMapUtils.copy(menuPO);
				if(!menus.contains(menu)){
					menus.add(menu);
				}
			}
		}
		return menus;
	}

}
