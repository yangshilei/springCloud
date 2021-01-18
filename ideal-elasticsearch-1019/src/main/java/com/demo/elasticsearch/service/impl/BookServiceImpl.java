package com.demo.elasticsearch.service.impl;

import com.demo.elasticsearch.dto.Result;
import com.demo.elasticsearch.elasticsearch.Book;
import com.demo.elasticsearch.elasticsearch.BookRepository;
import com.demo.elasticsearch.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void delete(Book book) {
        bookRepository.delete(book);
    }

    @Override
    public Optional<Book> findOne(String id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> findAll() {
        return (List<Book>)bookRepository.findAll();
    }

    /**
     *  根据作者精确查询
     * @param author
     * @param pageRequest
     * @return
     */
    @Override
    public Page<Book> findByAuthor(String author, PageRequest pageRequest) {
        return bookRepository.findByAuthor(author,pageRequest);
    }

    /**
     *  根据书的主题模糊查询
     * @param title
     * @return
     */
    @Override
    public Result findByTitle(String title) {
        log.info("");
        String json = "{\n" +
                "        \"match\" : {\n" +
                "            \"title\" : " + "\"" + title + "\"\n"+
                "        }\n" +
                "    }";
        log.info("hql语句格式==={}",json);

        // 创建es查询请求
        StringQuery query = new StringQuery(json);
        query.addIndices("book"); // 添加index
        query.addTypes("doc"); // 添加type

        List<Book> books = elasticsearchTemplate.queryForList(query, Book.class);

        return Result.ok(books);
    }


}
