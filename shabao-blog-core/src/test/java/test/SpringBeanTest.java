package test;


import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import com.xiaoshabao.base.component.SpringContextHolder;
import com.xiaoshabao.base.service.SysConfigService;
import com.xiaoshabao.blog.BlogCoreApplication;

@SpringBootTest(classes = BlogCoreApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@WebAppConfiguration
public class SpringBeanTest {

	@Test
	public void testAddProject() {
		try {
			SpringContextHolder.getBean("sysConfigServiceImpl", SysConfigService.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Not yet implemented");
		}

	}

}
