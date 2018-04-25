package com.xiaoshabao.blog.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoshabao.blog.dao.AuthMenuDao;
import com.xiaoshabao.blog.dao.RoleDao;
import com.xiaoshabao.blog.dto.AuthMenu;
import com.xiaoshabao.blog.dto.Role;
import com.xiaoshabao.blog.entity.AuthMenuPO;
import com.xiaoshabao.blog.entity.RolePO;
import com.xiaoshabao.blog.service.RoleService;
import com.xiaoshabao.blog.util.BeanMapUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private AuthMenuDao authMenuDao;

	@Override
	@Transactional(readOnly = true)
	public Page<Role> paging(Pageable pageable) {
		Page<RolePO> page = roleDao.findAllByOrderByIdDesc(pageable);
		List<Role> roles = new ArrayList<>();
		page.getContent().forEach(po -> {
			roles.add(BeanMapUtils.copy(po));
		});

		return new PageImpl<>(roles, pageable, page.getTotalElements());
	}

	@Override
	@Transactional(readOnly = true)
	public Role get(Long id) {
		Optional<RolePO> opo = roleDao.findById(id);
		Role role = BeanMapUtils.copy(opo.get());
		return role;
	}

	@Override
	@Transactional
	public void save(Role role){
		RolePO rolePO = new RolePO();
		List<AuthMenu> authMenus = role.getAuthMenus();
		List<AuthMenuPO> authMenuPOs = new ArrayList<>();
		for(AuthMenu authMenu : authMenus){
			AuthMenuPO authMenuPO = authMenuDao.findById(authMenu.getId()).get();
			authMenuPOs.add(authMenuPO);
		}
		BeanUtils.copyProperties(role, rolePO);
		rolePO.setAuthMenus(authMenuPOs);
		roleDao.save(rolePO);
	}

	@Override
	@Transactional(readOnly=false)
	public void delete(Long id) {
		roleDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Role> getAll() {
		List<RolePO> list = roleDao.findAll();
		List<Role> roles = new ArrayList<>();
		list.forEach(po -> {
			roles.add(BeanMapUtils.copy(po));
		});
		return roles;
	}

}
