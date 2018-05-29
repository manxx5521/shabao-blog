package test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.xiaoshabao.blog.BlogApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BlogApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@WebAppConfiguration
public class IndexTest {
	@Autowired
	@PersistenceContext
	private EntityManager entityManager;
	@PersistenceUnit EntityManagerFactory emf;
	
	@Test
	public void createIndex(){
		EntityManager em = emf.createEntityManager();
		FullTextEntityManager fullTextSession = Search.getFullTextEntityManager(em);
	    try {
	     // 只执行一次即可，后续新增的记录Hibernate将自动创建索引
	        fullTextSession.createIndexer().startAndWait();
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	}

}
