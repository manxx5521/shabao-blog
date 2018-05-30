package com.xiaoshabao.blog;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import com.xiaoshabao.base.BaseApplication;

//缓存对象必须实现Serializable
@EnableCaching
@SpringBootApplication
public class BlogApplication extends BaseApplication {

	@Value("${http.port}")
	private Integer httpPort;
	@Value("${server.port}")
	private Integer sslPort;

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

	/**
	 * 初始化配置tomcat
	 */
	@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
			//此处的注释放开可以配合下边的注释，完成重定向，将http转向https
			/*@Override
			protected void postProcessContext(Context context) {
				SecurityConstraint securityConstraint = new SecurityConstraint();
				securityConstraint.setUserConstraint("CONFIDENTIAL");
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/*");
				securityConstraint.addCollection(collection);
				context.addConstraint(securityConstraint);
			}
			*/
		};

		// 添加http
		tomcat.addAdditionalTomcatConnectors(createStandardConnector());
		return tomcat;
	}

	// 配置http
	private Connector createStandardConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setPort(httpPort);
		//此处的注释放开可以配合上边的注释，完成重定向，将http转向https
//		connector.setScheme("http");
//		connector.setSecure(false);
//		connector.setRedirectPort(sslPort);
		return connector;
	}

}
