package com.xiaoshabao.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;

import com.xiaoshabao.base.BaseApplication;

//缓存对象必须实现Serializable
@EnableCaching
public class BlogApplication extends BaseApplication{
	/*@Bean
	public CacheManagerCustomizer<ConcurrentMapCacheManager> cacheManagerCustomizer() {
		return new CacheManagerCustomizer<ConcurrentMapCacheManager>() {
			@Override
			public void customize(ConcurrentMapCacheManager cacheManager) {
				cacheManager.setAllowNullValues(false);
			}
		};
	}*/
	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}
}
