package com.xiaoshabao.blog.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.xiaoshabao.blog.entity.PostPO;

import java.util.Collection;
import java.util.List;

/**
 */
public interface PostDao extends JpaRepository<PostPO, Long>, JpaSpecificationExecutor<PostPO>, PostDaoCustom {
	/**
	 * 查询指定用户
	 * @param pageable
	 * @param authorId
	 * @return
	 */
	Page<PostPO> findAllByAuthorIdOrderByCreatedDesc(Pageable pageable, long authorId);

	// findLatests
	List<PostPO> findTop10ByOrderByCreatedDesc();

	// findHots
	List<PostPO> findTop10ByOrderByViewsDesc();

	List<PostPO> findAllByIdIn(Collection<Long> id);

	List<PostPO> findTop5ByFeaturedGreaterThanOrderByCreatedDesc(int featured);

	@Query("select coalesce(max(weight), 0) from PostPO")
	int maxWeight();
	
}
