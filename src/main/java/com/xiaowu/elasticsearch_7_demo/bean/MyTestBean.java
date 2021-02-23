package com.xiaowu.elasticsearch_7_demo.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
/**
 * @ClassName: MyTestBean
 * @Author: guowuwu
 * @Description:
 * @Date: 2021/2/23 15:53
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Document(indexName = "my-test", shards = 1, replicas = 0)
public class MyTestBean {

    @Id
    private String id;


    @Field(type = FieldType.Text)
    private String field1;
}
