package com.xiaoshabao.blog.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.xiaoshabao.blog.entity.VerifyPO;

/**
 */
public interface VerifyDao extends JpaRepository<VerifyPO, Long>, JpaSpecificationExecutor<VerifyPO> {
    VerifyPO findByUserIdAndType(long userId, int type);
    VerifyPO findByUserId(long userId);
}
