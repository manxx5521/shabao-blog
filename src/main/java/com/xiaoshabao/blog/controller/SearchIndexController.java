package com.xiaoshabao.blog.controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hibernate Search索引
 */
@RestController
@RequestMapping("/searchIndex")
public class SearchIndexController {
	@PersistenceUnit
	EntityManagerFactory emf;

	/**
	 * 重建索引，只需要执行一次。
	 * @return
	 */
	@GetMapping("/create")
	public String createIndex() {
		EntityManager em = emf.createEntityManager();
		FullTextEntityManager fullTextSession = Search.getFullTextEntityManager(em);
		try {
			// 只执行一次即可，后续新增的记录Hibernate将自动创建索引
			fullTextSession.createIndexer().startAndWait();
			return "索引创建成功！";
		} catch (InterruptedException e) {
			return "创建索引时，出现异常！！";
		}
	}
}
