package daily.boot.gulimall.search.service.impl;

import daily.boot.gulimall.search.entity.Account;
import daily.boot.gulimall.search.service.AccountService;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class BaseESServiceImplTest {
    
    @Autowired
    private AccountService accountService;
    
    @Test
    void saveOrUpdate() {
        Account account = new Account();
        account.setAccountNumber(1000L);
        account.setAddress("Shanghai Beijiangyan Road 227");
        account.setAge(25L);
        account.setBalance(38383L);
        account.setCity("Shanghai");
        account.setState("SH");
        account.setEmail("test2@126.com");
        account.setEmployer("Amy Wong");
        account.setFirstname("Amy");
        account.setLastname("Wong");
        account.setGender("M");
    
        accountService.saveOrUpdate(account);
    }
    
    @Test
    /**
     * 按照年龄聚合，并且这些年龄段M的平均薪资和F的平均薪资以及这个年龄段总平均薪资
     * GET /bank_account/_search
     * {
     *   "query": {
     *     "match_all": {}
     *   },
     *   "aggs": {
     *     "ageAgg": {
     *       "terms": {
     *         "field": "age",
     *         "size": 100
     *       },
     *       "aggs": {
     *         "gendAgg": {
     *           "terms": {
     *             "field": "gender.keyword",
     *             "size": 10
     *           },
     *           "aggs": {
     *             "genderBlanceAvg": {
     *               "avg": {
     *                 "field": "balance"
     *               }
     *             }
     *           }
     *         },
     *         "blanceAvg": {
     *           "avg": {
     *             "field": "balance"
     *           }
     *         }
     *       }
     *     }
     *   },
     *   "size": 0
     * }
     */
    void testSearch() {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery()).size(5);
        //根据年龄聚合
        TermsAggregationBuilder termsAggs = AggregationBuilders.terms("ageAgg").field("age").size(100);
        //基于年龄聚合继承根据性别聚合
        TermsAggregationBuilder genderAggs = AggregationBuilders.terms("genderAgg").field("gender.keyword").size(10);
        //基于年龄性别继承聚合求平均值
        genderAggs.subAggregation(AggregationBuilders.avg("genderBlanceAvg").field("balance"));
        termsAggs.subAggregation(genderAggs);
        //基于年龄平局值
        termsAggs.subAggregation(AggregationBuilders.avg("blanceAvg").field("balance"));
        builder.aggregation(termsAggs);
    
        System.out.println("检索条件：" + builder);
    
        accountService.search(builder);
        
    }
}