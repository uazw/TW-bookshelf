package com.thoughtworks.jimmy.service;

import com.thoughtworks.jimmy.model.Book;

public interface BookService {

    Iterable<Book> findAll();

    Book findByIsbn(String isbn);

    void appendBook(Book book);

    void edit(Book book);

    void deleteByIsbn(String isbn);
}
