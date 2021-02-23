package com.xiaowu.elasticsearch_7_demo.service;

import com.xiaowu.elasticsearch_7_demo.bean.MyTestBean;
import org.springframework.data.domain.Page;

/**
 * @ClassName: ElasticsearchService
 * @Author: guowuwu
 * @Description:
 * @Date: 2021/2/23 16:07
 * @Version: 1.0
 */
public interface  ElasticsearchService {

    Page<MyTestBean> findByFirstCode(String firstCode);
}
