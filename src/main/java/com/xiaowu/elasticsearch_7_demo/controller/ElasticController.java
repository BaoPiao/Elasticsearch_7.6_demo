package com.xiaowu.elasticsearch_7_demo.controller;

import com.xiaowu.elasticsearch_7_demo.bean.MyTestBean;
import com.xiaowu.elasticsearch_7_demo.service.ElasticsearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: ElasticController
 * @Author: guowuwu
 * @Description:
 * @Date: 2021/2/23 16:56
 * @Version: 1.0
 */
@RequestMapping("/elastic")
@RestController
@Slf4j
public class ElasticController {

    @Autowired
    ElasticsearchService elasticsearchService;

    @GetMapping("/match")
    @ResponseBody
    public Page<MyTestBean> match(@RequestParam("field") String field) {
        return elasticsearchService.findByFirstCode(field);
    }

    @GetMapping("/match_high_level")
    @ResponseBody
    public Page<String> matchHighLevel(@RequestParam("field") String field) {
        return elasticsearchService.findByHighLevelClient(field);
    }

    //插入数据
    @GetMapping("/write_high_level")
    @ResponseBody
    public void writeHighLevel(@RequestParam("field") String field) throws InterruptedException {
        log.info("开始写入！");
        elasticsearchService.insertDataIntoEs(field);
    }
}
