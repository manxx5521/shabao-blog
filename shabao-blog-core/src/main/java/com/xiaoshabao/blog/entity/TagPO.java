package com.xiaoshabao.blog.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 */
@Entity
@Table(name = "mto_tags")
public class TagPO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", unique = true, length = 32)
	private String name;
	/** 是否推荐 */
	private Integer featured;
	/** 文章数 */
	private Integer posts;
	/** 热度 */
	private Integer hots;
	/** 是否锁定 */
	private Integer locked;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getFeatured() {
		return featured;
	}
	public void setFeatured(Integer featured) {
		this.featured = featured;
	}
	public Integer getPosts() {
		return posts;
	}
	public void setPosts(Integer posts) {
		this.posts = posts;
	}
	public Integer getHots() {
		return hots;
	}
	public void setHots(Integer hots) {
		this.hots = hots;
	}
	public Integer getLocked() {
		return locked;
	}
	public void setLocked(Integer locked) {
		this.locked = locked;
	}

}
