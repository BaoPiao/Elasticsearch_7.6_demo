package com.xiaowu.elasticsearch_7_demo.service.impl;

import com.xiaowu.elasticsearch_7_demo.Elasticsearch7DemoApplication;
import com.xiaowu.elasticsearch_7_demo.service.ElasticsearchService;
import org.elasticsearch.common.collect.Tuple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * @ClassName: ElasticsearchServiceImplTest
 * @Author: guowuwu
 * @Description:
 * @Date: 2021/3/24 11:23
 * @Version: 1.0
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Elasticsearch7DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan
public class ElasticsearchServiceImplTest {

    @Autowired
    ElasticsearchService elasticsearchService;


    @Test
    public void testCreateIndex() {
        elasticsearchService.findByFirstCode("");
    }

    @Test
    public void aggQuery() {
        ArrayList<Tuple<String, String>> tuples = elasticsearchService.aggQuery();
        System.out.println(tuples.toString());

    }
}