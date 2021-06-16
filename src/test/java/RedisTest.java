import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import tw.hyin.mySpringBoot.MyRestApiApplication;
import tw.hyin.mySpringBoot.config.RedisConfig;
import tw.hyin.mySpringBoot.dao.QuestionnaireQuestionDao;
import tw.hyin.mySpringBoot.entity.QuestionnaireQuestion;
import tw.hyin.mySpringBoot.service.QuestionnaireService;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author rita6 on 2021.
 */
//using an embedded Redis server
@Import({ RedisConfig.class, QuestionnaireService.class})
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyRestApiApplication.class)
@EnableCaching
@ImportAutoConfiguration(classes = {
        CacheAutoConfiguration.class,
        RedisAutoConfiguration.class
})
public class RedisTest {

    @MockBean
    private QuestionnaireQuestionDao questionDao;

    @Autowired
    private QuestionnaireService qService;

    @Test
    public void RedisCachingTest() {
        List<QuestionnaireQuestion> anItem = questionDao.getQuestions("1");

        List<QuestionnaireQuestion> itemOrigin = qService.getQuestions("1");
        List<QuestionnaireQuestion> itemCache = qService.getQuestions("1");

        assertEquals(itemOrigin, anItem);
        assertEquals(itemCache, anItem);
    }
}
