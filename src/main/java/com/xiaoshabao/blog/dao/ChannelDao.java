/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.xiaoshabao.blog.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.xiaoshabao.blog.entity.ChannelPO;

import java.util.Collection;
import java.util.List;

/**
 * @author langhsu
 *
 */
public interface ChannelDao extends JpaRepository<ChannelPO, Integer>, JpaSpecificationExecutor<ChannelPO> {
	List<ChannelPO> findAllByStatus(int status);
	List<ChannelPO> findAllByIdIn(Collection<Integer> id);
	ChannelPO findByKey(String key);
}
