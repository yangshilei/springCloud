package com.demo.elasticsearch.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "book",type = "doc",shards = 5,replicas = 1)
public class Book {

    @Id
    private String id;

    /**
     * 中文分词设置，前提是您的es已经安装了中文分词ik插件
     * 中文分词有两种形式：
     * ik_max_word：会将文本做最细粒度的拆分
     * ik_smart：会将文本做最粗粒度的拆分
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word",searchAnalyzer ="ik_max_word")
    private String title;

    private String author;

    private String postDate;

}
