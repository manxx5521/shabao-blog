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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoshabao.blog.dao.ChannelDao;
import com.xiaoshabao.blog.dto.Channel;
import com.xiaoshabao.blog.entity.ChannelPO;
import com.xiaoshabao.blog.lang.Consts;
import com.xiaoshabao.blog.service.ChannelService;
import com.xiaoshabao.blog.util.BeanMapUtils;

/**
 * @author langhsu
 *
 */
@Service
@Transactional(readOnly = true)
public class ChannelServiceImpl implements ChannelService {
	@Autowired
	private ChannelDao channelDao;

	@Override
	public List<Channel> findAll(int status) {
		List<ChannelPO> list;
		if (status > Consts.IGNORE) {
			list = channelDao.findAllByStatus(status);
		} else {
			list = channelDao.findAll();
		}
		List<Channel> rets = new ArrayList<>();
		list.forEach(po -> rets.add(BeanMapUtils.copy(po)));
		return rets;
	}

	@Override
	public Map<Integer, Channel> findMapByIds(Collection<Integer> ids) {
		List<ChannelPO> list = channelDao.findAllByIdIn(ids);
		Map<Integer, Channel> rets = new HashMap<>();
		list.forEach(po -> rets.put(po.getId(), BeanMapUtils.copy(po)));
		return rets;
	}

	@Override
	@Cacheable(value = "groupsCaches", key = "'g_' + #id")
	public Channel getById(int id) {
		return BeanMapUtils.copy(channelDao.findById(id).get());
	}

	@Override
	@Cacheable(value = "groupsCaches", key = "'g_' + #key")
	public Channel getByKey(String key) {
		return BeanMapUtils.copy(channelDao.findByKey(key));
	}

	@Override
	@Transactional
	public void update(Channel channel) {
		Optional<ChannelPO> opo = channelDao.findById(channel.getId());
		ChannelPO po=null;
		if (opo.isPresent()) {
			po=opo.get();
			BeanUtils.copyProperties(channel, po);
		} else {
			po = new ChannelPO();
			BeanUtils.copyProperties(channel, po);
		}
		channelDao.save(po);
	}

	@Override
	@Transactional
	public void delete(int id) {
		channelDao.deleteById(id);
	}

}
