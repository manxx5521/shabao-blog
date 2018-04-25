package com.xiaoshabao.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.xiaoshabao.blog.dto.Role;

import java.util.List;

public interface RoleService {
	
	Page<Role> paging(Pageable pageable);
	
	Role get(Long id);
	
	void save(Role role);
	
	void delete(Long id);
	
	List<Role> getAll();

}
