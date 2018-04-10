package com.xiaoshabao.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;

import com.xiaoshabao.base.BaseApplication;

//缓存对象必须实现Serializable
@EnableCaching
public class BlogApplication extends BaseApplication{
	
	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}
}
