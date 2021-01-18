package com.demo.elasticsearch.service;

import com.demo.elasticsearch.dto.Result;
import com.demo.elasticsearch.elasticsearch.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Optional<Book> findById(String id);

    Book save(Book blog);

    void delete(Book blog);

    Optional<Book> findOne(String id);

    List<Book> findAll();

    // 根据作者字段查询
    Page<Book> findByAuthor(String author, PageRequest pageRequest);

    // 根据书的主题中某个字段查询
    Result findByTitle(String author);
}
