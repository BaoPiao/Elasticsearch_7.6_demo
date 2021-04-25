package com.xiaowu.elasticsearch_7_demo.service;

import com.xiaowu.elasticsearch_7_demo.bean.MyTestBean;
import org.elasticsearch.common.collect.Tuple;
import org.springframework.data.domain.Page;

import java.util.ArrayList;

/**
 * @ClassName: ElasticsearchService
 * @Author: guowuwu
 * @Description:
 * @Date: 2021/2/23 16:07
 * @Version: 1.0
 */
public interface  ElasticsearchService {

    Page<MyTestBean> findByFirstCode(String firstCode);


    Page<String> findByHighLevelClient(String string);

    //插入数据
    Boolean insertDataIntoEs(String s);

    //聚集查询
     ArrayList<Tuple<String, String>> aggQuery();

     //判断索引是否存在
    Boolean judgeIndexExist(String indexName);


    //强制合并
    void schedule();
}
