package com.xiaowu.elasticsearch_7_demo.dao;

import com.xiaowu.elasticsearch_7_demo.bean.MyTestBean;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

/**
 * @ClassName: ElasticMyTestRepository
 * @Author: guowuwu
 * @Description:
 * @Date: 2021/2/23 16:00
 * @Version: 1.0
 */
public interface ElasticMyTestRepository extends Repository<MyTestBean, Long> {

    // field1是字段名称
    Page<MyTestBean> findByField1(String field1, Pageable pageable);
}
