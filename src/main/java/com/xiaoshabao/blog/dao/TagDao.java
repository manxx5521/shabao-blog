package com.xiaoshabao.blog.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xiaoshabao.blog.entity.TagPO;

public interface TagDao  extends JpaRepository<TagPO, Long> {
	
	List<TagPO> findTop8ByOrderByhots();
}