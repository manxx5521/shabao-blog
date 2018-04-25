package com.xiaoshabao.blog.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.xiaoshabao.blog.entity.AuthMenuPO;

import java.util.List;

public interface AuthMenuDao extends JpaRepository<AuthMenuPO, Long>, JpaSpecificationExecutor<AuthMenuPO> {
    List<AuthMenuPO> findAllByParentIdOrderBySortAsc(Long parentId);
    List<AuthMenuPO> findAll();
}
