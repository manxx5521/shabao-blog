package com.xiaoshabao.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import com.xiaoshabao.base.BaseApplication;

@EnableCaching
@SpringBootApplication
public class BlogCoreApplication extends BaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogCoreApplication.class, args);
	}

}
