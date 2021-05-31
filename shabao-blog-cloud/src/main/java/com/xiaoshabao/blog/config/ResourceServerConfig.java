package com.xiaoshabao.blog.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
@EnableOAuth2Sso
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	// 配置安全策略
	@Override
	public void configure(HttpSecurity http) throws Exception {
		// 禁掉csrf
		http.csrf().disable()
			.authorizeRequests()
				// 配置这些链接无需验证
				.antMatchers("/login").anonymous()
				.antMatchers("/user/**").authenticated()//访问需要 登录
				// 除以上路径都需要验证
//				.anyRequest().authenticated()
				.and()
				// 登录页面url 配置登录成功后调用的方法
			.formLogin()
				.loginPage("/login").permitAll()
				
				
		// 路径角色验证
		// .antMatchers("/admin/**").hasRole("ADMIN")
		// 排除该路径角色认证 注意顺序自上而下
		// .antMatchers("/**").hasRole("USER")
		;
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer config) {
		config.tokenServices(tokenServices());
	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey("123");
		return converter;
	}

	@Bean
	@Primary
	public DefaultTokenServices tokenServices() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		return defaultTokenServices;
	}
}
