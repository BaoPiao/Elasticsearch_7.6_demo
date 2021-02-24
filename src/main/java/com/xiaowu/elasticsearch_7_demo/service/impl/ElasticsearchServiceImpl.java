package com.xiaowu.elasticsearch_7_demo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaowu.elasticsearch_7_demo.bean.MyTestBean;
import com.xiaowu.elasticsearch_7_demo.dao.ElasticMyTestRepository;
import com.xiaowu.elasticsearch_7_demo.service.ElasticsearchService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

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
        Pageable pageable = PageRequest.of(0, 10);
//        MatchQueryBuilder field1 = QueryBuilders.matchQuery("field1", firstCode);
        Page<MyTestBean> byFirstCode = elasticMyTestRepository.findByField1(firstCode, pageable);
        return byFirstCode;
    }

    @Autowired
    @Qualifier("MyRestHighLevelClient")
    RestHighLevelClient restHighLevelClient;

    @Override
    public Page<String> findByHighLevelClient(String string) {
        MatchQueryBuilder field1 = QueryBuilders.matchQuery("field1", string);

        SearchSourceBuilder query = new SearchSourceBuilder().query(field1);
        SearchResponse test = null;
//        SearchRequest searchRequest1 = new SearchRequest().source(query).indices("test").indicesOptions(IndicesOptions.lenientExpandOpen());
        try {

            test = restHighLevelClient.search(new SearchRequest().source(query).indices("my-test").indicesOptions(IndicesOptions.lenientExpandOpen()), RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SearchHits hits = test.getHits();

        ArrayList<String> arrayList = new ArrayList<>();
        for (SearchHit hit : hits.getHits()) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            for (Map.Entry<String, Object> stringObjectEntry : sourceAsMap.entrySet()) {
                Object value = stringObjectEntry.getValue();
                arrayList.add(String.valueOf(value));
            }
        }
        PageImpl<String> strings = new PageImpl<String>(arrayList, PageRequest.of(0, 10), Integer.valueOf(String.valueOf(hits.getTotalHits().value)));
        return strings;
    }


}
