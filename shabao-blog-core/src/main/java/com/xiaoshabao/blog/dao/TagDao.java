package com.xiaoshabao.blog.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xiaoshabao.blog.entity.TagPO;

public interface TagDao  extends JpaRepository<TagPO, Long> {
	
	@Query("from TagPO order by featured,hots")
	List<TagPO> findTop8ByOrderByhots();
}