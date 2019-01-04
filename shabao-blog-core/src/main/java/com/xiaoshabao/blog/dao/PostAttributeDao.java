package com.xiaoshabao.blog.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.xiaoshabao.blog.entity.PostAttribute;

/**
 * Created by langhsu on 2017/9/27.
 */
public interface PostAttributeDao extends JpaRepository<PostAttribute, Long>, JpaSpecificationExecutor<PostAttribute> {

}
