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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.xiaoshabao.base.component.SpringContextHolder;
import com.xiaoshabao.blog.dao.PostAttributeDao;
import com.xiaoshabao.blog.dao.PostDao;
import com.xiaoshabao.blog.dto.Channel;
import com.xiaoshabao.blog.dto.Post;
import com.xiaoshabao.blog.dto.User;
import com.xiaoshabao.blog.entity.PostAttribute;
import com.xiaoshabao.blog.entity.PostPO;
import com.xiaoshabao.blog.event.FeedsEvent;
import com.xiaoshabao.blog.lang.Consts;
import com.xiaoshabao.blog.lang.EntityStatus;
import com.xiaoshabao.blog.service.ChannelService;
import com.xiaoshabao.blog.service.FavorService;
import com.xiaoshabao.blog.service.PostService;
import com.xiaoshabao.blog.service.UserEventService;
import com.xiaoshabao.blog.service.UserService;
import com.xiaoshabao.blog.util.BeanMapUtils;
import com.xiaoshabao.blog.util.PreviewTextUtils;

/**
 * @author langhsu
 *
 */
@Service
@Transactional(readOnly = true)
@CacheConfig(cacheNames = "postsCaches")
public class PostServiceImpl implements PostService {
	@Autowired
	private PostDao postDao;
	
	@Autowired
	private UserService userService;
	@Autowired
	private UserEventService userEventService;
	@Autowired
	private FavorService favorService;
	@Autowired
	private ChannelService channelService;
	@Autowired
	private PostAttributeDao postAttributeDao;
	
	@Override
	@Cacheable("temp_postsCaches")
	public Page<Post> paging(Pageable pageable, int channelId, String ord) {
		Page<PostPO> page = postDao.findAll((root, query, builder) -> {

			List<Order> orders = new ArrayList<>();

			if (Consts.order.FAVOR.equals(ord)) {
				orders.add(builder.desc(root.<Long>get("favors")));
			} else if (Consts.order.HOTTEST.equals(ord)) {
				orders.add(builder.desc(root.<Long>get("comments")));
			} else {
				orders.add(builder.desc(root.<Long>get("weight")));
			}
			orders.add(builder.desc(root.<Long>get("created")));

			Predicate predicate = builder.conjunction();

			if (channelId > Consts.ZERO) {
				predicate.getExpressions().add(
						builder.equal(root.get("channelId").as(Integer.class), channelId));
			}

			if (Consts.order.HOTTEST.equals(ord)) {
				orders.add(builder.desc(root.<Long>get("views")));
			}

//			predicate.getExpressions().add(
//					builder.equal(root.get("featured").as(Integer.class), Consts.FEATURED_DEFAULT));

			query.orderBy(orders);

			return predicate;
		}, pageable);

		return new PageImpl<>(toPosts(page.getContent()), pageable, page.getTotalElements());
	}
	
	private List<Post> toPosts(List<PostPO> posts) {
		List<Post> rets = new ArrayList<>();

		HashSet<Long> pids = new HashSet<>();
		HashSet<Long> uids = new HashSet<>();
		HashSet<Integer> groupIds = new HashSet<>();

		posts.forEach(po -> {
			pids.add(po.getId());
			uids.add(po.getAuthorId());
			groupIds.add(po.getChannelId());

			rets.add(BeanMapUtils.copy(po, 0));
		});

		// 加载用户信息
		buildUsers(rets, uids);
		buildGroups(rets, groupIds);

		return rets;
	}

	
	
	@Override
	public Page<Post> paging4Admin(Pageable pageable, long id, String title, int channelId) {
		Page<PostPO> page = postDao.findAll((root, query, builder) -> {
            query.orderBy(
					builder.desc(root.<Long>get("weight")),
					builder.desc(root.<Long>get("created"))
			);

            Predicate predicate = builder.conjunction();

			if (channelId > Consts.ZERO) {
				predicate.getExpressions().add(
						builder.equal(root.get("channelId").as(Integer.class), channelId));
			}

			if (StringUtils.isNotBlank(title)) {
				predicate.getExpressions().add(
						builder.like(root.get("title").as(String.class), "%" + title + "%"));
			}

			if (id > Consts.ZERO) {
				predicate.getExpressions().add(
						builder.equal(root.get("id").as(Integer.class), id));
			}

            return predicate;
        }, pageable);

		return new PageImpl<>(toPosts(page.getContent()), pageable, page.getTotalElements());
	}

	@Override
	@Cacheable
	public Page<Post> pagingByAuthorId(Pageable pageable, long userId) {
		Page<PostPO> page = postDao.findAllByAuthorIdOrderByCreatedDesc(pageable, userId);
		return new PageImpl<>(toPosts(page.getContent()), pageable, page.getTotalElements());
	}

	@Override
	@Cacheable
	public List<Post> findAllFeatured() {
		List<PostPO> list = postDao.findTop5ByFeaturedGreaterThanOrderByCreatedDesc(Consts.FEATURED_DEFAULT);
		return toPosts(list);
	}

	@Override
	public Page<Post> search(Pageable pageable, String q) throws Exception {
		Page<Post> page = postDao.search(pageable, q);

		HashSet<Long> ids = new HashSet<>();
		HashSet<Long> uids = new HashSet<>();

		for (Post po : page.getContent()) {
			ids.add(po.getId());
			uids.add(po.getAuthorId());
		}

		// 加载用户信息
		buildUsers(page.getContent(), uids);

		return page;
	}
	
	@Override
	public Page<Post> searchByTag(Pageable pageable, String tag) {
		Page<Post> page = postDao.searchByTag(pageable, tag);

		HashSet<Long> ids = new HashSet<>();
		HashSet<Long> uids = new HashSet<>();

		for (Post po : page.getContent()) {
			ids.add(po.getId());
			uids.add(po.getAuthorId());
		}

		// 加载用户信息
		buildUsers(page.getContent(), uids);
		return page;
	}
	
	@Override
	@Cacheable
	public List<Post> findLatests(int maxResults, long ignoreUserId) {
		List<PostPO> list = postDao.findTop10ByOrderByCreatedDesc();
		List<Post> rets = new ArrayList<>();

		list.forEach(po -> rets.add(BeanMapUtils.copy(po, 0)));

		return rets;
	}
	
	@Override
	@Cacheable
	public List<Post> findHots(int maxResults, long ignoreUserId) {
		List<PostPO> list = postDao.findTop10ByOrderByViewsDesc();
		List<Post> rets = new ArrayList<>();

		list.forEach(po -> rets.add(BeanMapUtils.copy(po, 0)));
		return rets;
	}
	
	@Override
	public Map<Long, Post> findMapByIds(Set<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyMap();
		}

		List<PostPO> list = postDao.findAllByIdIn(ids);
		Map<Long, Post> rets = new HashMap<>();

		HashSet<Long> uids = new HashSet<>();

		list.forEach(po -> {
			rets.put(po.getId(), BeanMapUtils.copy(po, 0));
			uids.add(po.getAuthorId());
		});
		
		// 加载用户信息
		buildUsers(rets.values(), uids);

		return rets;
	}

	@Override
	@Transactional
	@CacheEvict(allEntries = true)
	public long post(Post post) {
		PostPO po = new PostPO();

		BeanUtils.copyProperties(post, po);

		po.setCreated(new Date());
		po.setStatus(EntityStatus.ENABLED);

		// 处理摘要
		if (StringUtils.isBlank(post.getSummary())) {
			po.setSummary(trimSummary(post.getContent()));
		} else {
			po.setSummary(post.getSummary());
		}

		postDao.save(po);

		PostAttribute attr = new PostAttribute();
		attr.setContent(post.getContent());
		attr.setId(po.getId());
		submitAttr(attr);
		
		// 更新文章统计
		userEventService.identityPost(po.getAuthorId(), po.getId(), true);

		FeedsEvent event = new FeedsEvent("feedsEvent");
		event.setPostId(po.getId());
		event.setAuthorId(post.getAuthorId());
		SpringContextHolder.publishEvent(event);

		return po.getId();
	}
	
	@Override
	@Cacheable(key = "'view_' + #id")
	public Post get(long id) {
		Optional<PostPO> opo = postDao.findById(id);
		Post d = null;
		if (opo.isPresent()) {
			PostPO po=opo.get();
			d = BeanMapUtils.copy(po, 1);

			d.setAuthor(userService.get(d.getAuthorId()));

			d.setChannel(channelService.getById(d.getChannelId()));

			Optional<PostAttribute> attr = postAttributeDao.findById(po.getId());
			if (attr.isPresent()) {
				d.setContent(attr.get().getContent());
			}
		}
		return d;
	}

	/**
	 * 更新文章方法
	 * @param p
	 */
	@Override
	@Transactional
	@CacheEvict(allEntries = true)
	public void update(Post p){
		Optional<PostPO> opo = postDao.findById(p.getId());
		if (opo.isPresent()) {
			PostPO po=opo.get();
			po.setTitle(p.getTitle());//标题
			po.setChannelId(p.getChannelId());

			// 处理摘要
			if (StringUtils.isBlank(p.getSummary())) {
				po.setSummary(trimSummary(p.getContent()));
			} else {
				po.setSummary(p.getSummary());
			}

			po.setTags(p.getTags());//标签

			// 保存扩展
			PostAttribute attr = new PostAttribute();
			attr.setContent(p.getContent());
			attr.setId(po.getId());
			submitAttr(attr);
		}
	}

	@Override
	@Transactional
	@CacheEvict(allEntries = true)
	public void updateFeatured(long id, int featured) {
		Optional<PostPO> opo = postDao.findById(id);

		if (opo.isPresent()) {
			PostPO po=opo.get();
			int status = Consts.FEATURED_ACTIVE == featured ? Consts.FEATURED_ACTIVE: Consts.FEATURED_DEFAULT;
			po.setFeatured(status);
			postDao.save(po);
		}
	}

	@Override
	@Transactional
	@CacheEvict(allEntries = true)
	public void updateWeight(long id, int weight) {
		Optional<PostPO> opo = postDao.findById(id);

		if (opo.isPresent()) {
			PostPO po=opo.get();
			int max = weight;
			if (Consts.FEATURED_ACTIVE == weight) {
				max = postDao.maxWeight() + 1;
			}
			po.setWeight(max);
			postDao.save(po);
		}
	}

	@Override
	@Transactional
	@CacheEvict(allEntries = true)
	public void delete(long id) {
		Optional<PostPO> opo = postDao.findById(id);
		if (opo.isPresent()) {
			postDao.delete(opo.get());
		}
	}
	
	@Override
	@Transactional
	@CacheEvict(allEntries = true)
	public void delete(long id, long authorId) {
		Optional<PostPO> opo = postDao.findById(id);
		if (opo.isPresent()) {
			// 判断文章是否属于当前登录用户
			Assert.isTrue(opo.get().getAuthorId() == authorId, "认证失败");

			delete(id);
		}
	}

	@Override
	@Transactional
	@CacheEvict(allEntries = true)
	public void delete(Collection<Long> ids) {
		ids.forEach(this::delete);
	}

	@Override
	@Transactional
	public void identityViews(long id) {
		Optional<PostPO> opo = postDao.findById(id);
		if (opo.isPresent()) {
			PostPO po=opo.get();
			po.setViews(po.getViews() + Consts.IDENTITY_STEP);
			postDao.save(po);
		}
	}

	@Override
	@Transactional
	public void identityComments(long id) {
		Optional<PostPO> opo = postDao.findById(id);
		if (opo.isPresent()) {
			PostPO po=opo.get();
			po.setComments(po.getComments() + Consts.IDENTITY_STEP);
			postDao.save(po);
		}
	}

	@Override
	@Transactional
	@CacheEvict(key = "'view_' + #postId")
	public void favor(long userId, long postId) {
		Optional<PostPO> opo = postDao.findById(postId);
		PostPO po=opo.get();
		
		Assert.notNull(po, "文章不存在");

		favorService.add(userId, postId);

		po.setFavors(po.getFavors() + Consts.IDENTITY_STEP);
	}

	@Override
	@Transactional
	@CacheEvict(key = "'view_' + #postId")
	public void unfavor(long userId, long postId) {
		Optional<PostPO> opo = postDao.findById(postId);
		PostPO po=opo.get();

		Assert.notNull(po, "文章不存在");

		favorService.delete(userId, postId);

		po.setFavors(po.getFavors() - Consts.IDENTITY_STEP);
	}
	
	@Override
	@Transactional
	public void resetIndexs() {
		postDao.resetIndexs();
	}

	/**
	 * 截取文章内容
	 * @param text
	 * @return
	 */
	private String trimSummary(String text){
		return PreviewTextUtils.getText(text, 126);
	}

	

	private void buildUsers(Collection<Post> posts, Set<Long> uids) {
		Map<Long, User> userMap = userService.findMapByIds(uids);

		posts.forEach(p -> p.setAuthor(userMap.get(p.getAuthorId())));
	}

	private void buildGroups(Collection<Post> posts, Set<Integer> groupIds) {
		Map<Integer, Channel> map = channelService.findMapByIds(groupIds);
		posts.forEach(p -> p.setChannel(map.get(p.getChannelId())));
	}

	private void submitAttr(PostAttribute attr) {
		postAttributeDao.save(attr);
	}

}
