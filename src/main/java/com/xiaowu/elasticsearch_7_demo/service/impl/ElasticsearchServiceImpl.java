package com.xiaowu.elasticsearch_7_demo.service.impl;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.xiaowu.elasticsearch_7_demo.bean.MyTestBean;
import com.xiaowu.elasticsearch_7_demo.dao.ElasticMyTestRepository;
import com.xiaowu.elasticsearch_7_demo.service.ElasticsearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.lucene.index.IndexOptions;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.forcemerge.ForceMergeRequest;
import org.elasticsearch.action.admin.indices.forcemerge.ForceMergeResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.*;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: ElasticsearchServiceImpl
 * @Author: guowuwu
 * @Description:
 * @Date: 2021/2/23 16:08
 * @Version: 1.0
 */
@Service
@Slf4j
public class ElasticsearchServiceImpl implements ElasticsearchService {
    @Autowired
    private ElasticMyTestRepository elasticMyTestRepository;

    @Autowired
    @Qualifier("MyRestHighLevelClient")
    private RestHighLevelClient restHighLevelClient;

    @Override
    public Page<MyTestBean> findByFirstCode(String firstCode) {
        Pageable pageable = PageRequest.of(0, 10);
//        MatchQueryBuilder field1 = QueryBuilders.matchQuery("field1", firstCode);
        Page<MyTestBean> byFirstCode = elasticMyTestRepository.findByField1(firstCode, pageable);
        return byFirstCode;
    }


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

    // 插入数据
    @Override
    @Async
    public Boolean insertDataIntoEs(String s) {
        IndexRequest indexRequest = new IndexRequest();
        indexRequest.index("my-test");
        HashMap<String, String> data = new HashMap<>();
        data.put("feild1", s);
        indexRequest.source(data);
        log.info("开始插入数据！");
        try {
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("插入失败！", e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    //创建模板索引
    void testCreateIndex() {
        IndexRequest indexRequest = new IndexRequest();
        indexRequest.index("data");
        indexRequest.source("");
        try {
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //判断索引是否存在
    public Boolean judgeIndexExist(String indexName) {
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        IndicesClient indices = restHighLevelClient.indices();
        try {
            return indices.exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("查询出错了！", e);
            return Boolean.FALSE;
        }
    }


    //索引创建
//PUT
//     http://127.0.0.1:9200/my-index-agg
//     {
//     "settings": {
//     "number_of_shards": 1
//     },
//     "mappings": {
//     "properties": {
//     "date23": {
//     "type": "keyword"
//     }
//     }
//     }
//     }


    //插入数据
//    POST
//     http://127.0.0.1:9200/my-index-agg/_doc
//     {
//
//     "date23":"test1"
//     }

    //聚集查询
    public ArrayList<Tuple<String, String>> aggQuery() {
        TermsAggregationBuilder date23 = AggregationBuilders.terms("date23-agg").size(10).field("date23");
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource();
        searchSourceBuilder.aggregation(date23);
        searchRequest.indices("my-index-agg").source(searchSourceBuilder);
        log.info("aggregation sql is " + searchRequest.toString());
        SearchResponse search = null;
        try {
            search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("查询失败了！");
        }
        Aggregations aggregations = search.getAggregations();
        Aggregation aggregation1 = aggregations.get("date23-agg");
        ArrayList<Tuple<String, String>> tuples = new ArrayList<>();
        if (!StringUtils.isEmpty(aggregation1)) {
            ParsedStringTerms s = (ParsedStringTerms) aggregation1;
            for (Terms.Bucket bucket : s.getBuckets()) {
                Tuple<String, String> stringStringTuple = new Tuple<>(String.valueOf(bucket.getKey()), String.valueOf(bucket.getDocCount()));
                tuples.add(stringStringTuple);
            }
        }
        return tuples;
    }

    @Scheduled(cron = "* 0/2 * * * *")
    public void schedule() {
        ForceMergeRequest forceMergeRequest = new ForceMergeRequest("mysql-enc_original_database-vehicle_info_echo");
        forceMergeRequest.flush(Boolean.TRUE);
        forceMergeRequest.maxNumSegments(1);
        IndicesClient indices = restHighLevelClient.indices();

        indices.forcemergeAsync(forceMergeRequest, RequestOptions.DEFAULT, new ActionListener<ForceMergeResponse>() {
            @Override
            public void onResponse(ForceMergeResponse forceMergeResponse) {
                for (int i = 0; i < 10; i++) {
                    log.info("合并完毕啦！");
                }
            }

            @Override
            public void onFailure(Exception e) {
                for (int i = 0; i < 10; i++) {
                    log.info("合并失败啦！");
                }
            }
        });
        System.out.println("测试");

    }

    //创建索引
    public void createIndex() {
        IndicesClient indices = restHighLevelClient.indices();
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("test-node1");
        createIndexRequest.settings("{\n" +
                "    \"index\": {\n" +
                "        \"refresh_interval\": \"60s\",\n" +
                "        \"number_of_shards\": \"10\",\n" +
                "        \"max_result_window\": \"2147483647\",\n" +
                "        \"store\": {\n" +
                "            \"preload\": [\n" +
                "                \"nvd\",\n" +
                "                \"dvd\",\n" +
                "                \"tim\",\n" +
                "                \"doc\",\n" +
                "                \"dim\"\n" +
                "            ]\n" +
                "        }\n" +
                "    }\n" +
                "}", XContentType.JSON);

        createIndexRequest.mapping("{\n" +
                "    \"_doc\": {\n" +
                "      \"properties\": {\n" +
                "        \"field\": {\n" +
                "          \"type\":\"keyword\"\n" +
                "        }\n" +
                "       \n" +
                "      }\n" +
                "    }\n" +
                "  }", XContentType.JSON);
        try {
            indices.create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
