package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.model.Book;
import com.example.servingwebcontent.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // Hiển thị danh sách sách
    @GetMapping
    public String listBooks(Model model, @RequestParam(name = "search", required = false) String search) {
        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("books", bookService.searchBooksByTitle(search));
        } else {
            model.addAttribute("books", bookService.getAllBooks());
        }
        return "books";
    }

    // Hiển thị form thêm sách
    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }

    // Xử lý thêm sách
    @PostMapping("/add")
    public String addBook(@Valid @ModelAttribute("book") Book book, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-book";
        }
        bookService.addBook(book);
        return "redirect:/books";
    }

    // Hiển thị form sửa sách
    @GetMapping("/edit/{id}")
    public String showEditBookForm(@PathVariable("id") Long id, Model model) {
        Book book = bookService.getBookById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));
        model.addAttribute("book", book);
        return "edit-book";
    }

    // Xử lý sửa sách
    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable("id") Long id, @Valid @ModelAttribute("book") Book book, BindingResult result) {
        if (result.hasErrors()) {
            return "edit-book";
        }
        bookService.updateBook(id, book);
        return "redirect:/books";
    }

    // Xóa sách
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}