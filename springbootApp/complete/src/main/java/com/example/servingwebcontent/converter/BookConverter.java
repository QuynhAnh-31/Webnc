package com.example.servingwebcontent.converter;

import com.example.servingwebcontent.model.Book;
import com.example.servingwebcontent.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BookConverter implements Converter<String, Book> {
    @Autowired
    private BookService bookService;

    @Override
    public Book convert(String id) {
        if (id == null || id.isEmpty()) return null;
        try {
            Integer bookId = Integer.valueOf(id);
            return bookService.getBookById(bookId).orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}