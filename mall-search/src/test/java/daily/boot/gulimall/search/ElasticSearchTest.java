package daily.boot.gulimall.search;

import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ElasticSearchTest {
    @Autowired
    private RestHighLevelClient elasticClient;
    
    @Test
    public void testClient() {
        System.out.println(elasticClient);
    }
}