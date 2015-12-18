package com.thoughtworks.jimmy.controller;

import com.thoughtworks.jimmy.model.Book;
import com.thoughtworks.jimmy.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BookShelfController {

    @Autowired
    private BookService bookService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String queryBooks(ModelMap model) {
        model.put("books", bookService.findAll());
        return "books";
    }

    @RequestMapping(value = "book/{isbn}", method = RequestMethod.GET)
    public String getBook(@PathVariable String isbn, ModelMap model) {
        model.put("book", bookService.findByIsbn(isbn));
        return "book";
    }

    @RequestMapping(value = "book/{isbn}", method = RequestMethod.PUT)
    @ResponseBody
    public String edit(@PathVariable String isbn, Book book) {
        bookService.edit(book);
        return "success";
    }

    @RequestMapping(value = "book/delete/{isbn}", method = RequestMethod.GET)
    public String delete(@PathVariable String isbn) {
        bookService.deleteByIsbn(isbn);
        return "redirect:/";
    }


    @RequestMapping(value = "book", method = RequestMethod.POST)
    public String appendBook(Book book) {
        bookService.appendBook(book);
        return "redirect:book/" + book.getIsbn();
    }

    @RequestMapping(value = "book/new", method = RequestMethod.GET)
    public String append() {
        return "newBook";
    }



    @RequestMapping(value = "book/edit/{isbn}", method = RequestMethod.GET)
    public String edit(@PathVariable String isbn, ModelMap model) {
        model.put("book", bookService.findByIsbn(isbn));
        return "editBook";
    }


}
