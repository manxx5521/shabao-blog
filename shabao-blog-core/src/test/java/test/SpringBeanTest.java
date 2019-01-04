package test;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.xiaoshabao.base.component.SpringContextHolder;
import com.xiaoshabao.base.service.SysConfigService;
import com.xiaoshabao.blog.BlogCoreApplication;

@RunWith(SpringJUnit4ClassRunner.class)
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
