package com.xiaowu.elasticsearch_7_demo.service.impl;

import com.xiaowu.elasticsearch_7_demo.bean.MyTestBean;
import com.xiaowu.elasticsearch_7_demo.dao.ElasticMyTestRepository;
import com.xiaowu.elasticsearch_7_demo.service.ElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @ClassName: ElasticsearchServiceImpl
 * @Author: guowuwu
 * @Description:
 * @Date: 2021/2/23 16:08
 * @Version: 1.0
 */
@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {
    @Autowired
    ElasticMyTestRepository elasticMyTestRepository;

    @Override
    public Page<MyTestBean> findByFirstCode(String firstCode) {
        Pageable pageable =  PageRequest.of(0,10);
//        MatchQueryBuilder field1 = QueryBuilders.matchQuery("field1", firstCode);
        Page<MyTestBean> byFirstCode = elasticMyTestRepository.findByField1(firstCode , pageable);
        return byFirstCode;
    }
}
