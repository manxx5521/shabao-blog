package com.xiaoshabao.blog.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiaoshabao.blog.dao.TagDao;
import com.xiaoshabao.blog.entity.TagPO;
import com.xiaoshabao.blog.service.TagService;

@Service("tagServiceImpl")
public class TagServiceImpl implements TagService {
	
	@Autowired
	private TagDao tagDao;

	@Override
	public List<TagPO> topTags(int maxResults) {
		return  tagDao.findTop8ByOrderByhots();
	}

}
