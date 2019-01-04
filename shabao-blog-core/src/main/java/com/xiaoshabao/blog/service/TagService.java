package com.xiaoshabao.blog.service;

import java.util.List;

import com.xiaoshabao.blog.entity.TagPO;

public interface TagService {
	
	/**
	 * top 查询 Tag
	 * @param maxResults
	 */
	List<TagPO> topTags(int maxResults);

}
