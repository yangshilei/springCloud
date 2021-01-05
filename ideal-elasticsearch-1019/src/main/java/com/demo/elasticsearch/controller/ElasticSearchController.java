package com.demo.elasticsearch.controller;

import com.demo.elasticsearch.dto.Result;
import com.demo.elasticsearch.elasticsearch.Book;
import com.demo.elasticsearch.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Api(tags = "ES测试模块")
@RestController
@Slf4j
@RequestMapping("/book")
public class ElasticSearchController {

    @ApiOperation(value = "es测试接口", notes = "es测试接口")
    @PostMapping(value = "/test" , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Result test(){
        log.info("进入测试接口！");
        return Result.ok("模块成功启动");
    }

    @Autowired
    private BookService bookService;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @ApiOperation(value = "创建索引",notes = "创建索引")
    @PostMapping("/create")
    public Result createIndex(){
        log.info("创建索引");
        boolean index = elasticsearchTemplate.createIndex(Book.class);
        return Result.ok(index);
    }

    @PostMapping("/save")
    public Result Save(@RequestBody Book book) {
        log.info("插入数据的入参==={}",book.toString());
        Book save = bookService.save(book);
        return Result.ok("新增数据成功");
    }

    @PostMapping("/id")
    public Result getBookById(@RequestBody Book request) {
        log.info("根据id查询入参=={}",request.toString());
        Optional<Book> opt = bookService.findById(request.getId());
        Book book = opt.get();
        log.info("根据id查询的结果==={}",book.toString());
        return Result.ok(book);
    }


}
