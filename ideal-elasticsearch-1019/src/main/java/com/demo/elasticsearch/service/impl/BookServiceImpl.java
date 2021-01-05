package com.demo.elasticsearch.service.impl;

import com.demo.elasticsearch.elasticsearch.Book;
import com.demo.elasticsearch.elasticsearch.BookRepository;
import com.demo.elasticsearch.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

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

    @Override
    public Page<Book> findByAuthor(String author, PageRequest pageRequest) {
        return bookRepository.findByAuthor(author,pageRequest);
    }
}
