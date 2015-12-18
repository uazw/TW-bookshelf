package com.thoughtworks.jimmy.controller;

import com.thoughtworks.jimmy.model.Book;
import com.thoughtworks.jimmy.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookShelfController {

    @Autowired
    private BookService bookService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Book>> queryBooks() {
        Iterable<Book> books = bookService.findAll();
        if (books.iterator().hasNext()) {
            return new ResponseEntity<Iterable<Book>>(books, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "book/{isbn}", method = RequestMethod.GET)
    public ResponseEntity<Book> getBook(@PathVariable String isbn) {
        Book book = bookService.findByIsbn(isbn);
        if (book != null) {
            return new ResponseEntity<Book>(book, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        }
    }

    @RequestMapping(value = "book/{isbn}", method = RequestMethod.PUT)
    public ResponseEntity<Book> edit(@PathVariable String isbn, Book book) {
        if (bookService.findByIsbn(book.getIsbn()) != null) {
            return new ResponseEntity<Book>(HttpStatus.BAD_REQUEST);
        } else {
            bookService.edit(book);
            return new ResponseEntity<Book>(bookService.findByIsbn(isbn), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "book/{isbn}", method = RequestMethod.DELETE)
    public Iterable<Book> delete(@PathVariable String isbn) {
        bookService.deleteByIsbn(isbn);
        return bookService.findAll();
    }


    @RequestMapping(value = "book", method = RequestMethod.POST)
    public ResponseEntity<Iterable<Book>> appendBook(Book book) {
        if (bookService.findByIsbn(book.getIsbn()) != null) {
            return new ResponseEntity<Iterable<Book>>(HttpStatus.CONFLICT);
        } else {
            bookService.appendBook(book);
            return new ResponseEntity<Iterable<Book>>(bookService.findAll(), HttpStatus.CREATED);
        }
    }
}
