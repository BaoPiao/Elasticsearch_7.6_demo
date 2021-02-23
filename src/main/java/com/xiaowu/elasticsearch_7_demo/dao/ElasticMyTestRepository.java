package com.xiaowu.elasticsearch_7_demo.dao;

import com.xiaowu.elasticsearch_7_demo.bean.MyTestBean;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
/**
 * @ClassName: ElasticMyTestRepository
 * @Author: guowuwu
 * @Description:
 * @Date: 2021/2/23 16:00
 * @Version: 1.0
 */
public interface ElasticMyTestRepository extends  ElasticsearchRepository<MyTestBean, Long> {

    @Query("{\n" +
            "  \"match\": {\n" +
            "    \"field1\": \"?\"\n" +
            "  }\n" +
            "  }"
            )
    Page<MyTestBean> findByFirstCode(String firstCode, Pageable pageable);
}
