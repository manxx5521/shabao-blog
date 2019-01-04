/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.xiaoshabao.blog.dto;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.xiaoshabao.blog.entity.PostAttribute;
import com.xiaoshabao.blog.entity.PostPO;
import com.xiaoshabao.blog.lang.Consts;

/**
 * @author langhsu
 * 
 */
public class Post extends PostPO implements Serializable {
	private static final long serialVersionUID = -1144627551517707139L;

	private String content;

	private User author;
	private Channel channel;
	
	@JSONField(serialize = false)
	private PostAttribute attribute;
	
	public String[] getTagsArray() {
		if (StringUtils.isNotBlank(super.getTags())) {
			return super.getTags().split(Consts.SEPARATOR);
		}
		return null;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public PostAttribute getAttribute() {
		return attribute;
	}

	public void setAttribute(PostAttribute attribute) {
		this.attribute = attribute;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
}
