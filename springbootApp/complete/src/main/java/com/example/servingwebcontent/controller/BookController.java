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
@RequestMapping("/books") // Đổi từ "/admin/books" thành "/books"
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
        return "books/list";
    }

    // Hiển thị form thêm sách
    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "books/add";
    }

    // Xử lý thêm sách
    @PostMapping("/add")
    public String addBook(@Valid @ModelAttribute("book") Book book, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "books/add";
        }
        bookService.addBook(book);
        return "redirect:/books";
    }

    // Hiển thị form sửa sách
    @GetMapping("/edit/{id}")
    public String showEditBookForm(@PathVariable("id") Integer id, Model model) {
        Book book = bookService.getBookById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sách với ID " + id + " không tồn tại"));
        model.addAttribute("book", book);
        return "books/edit";
    }

    // Xử lý sửa sách
    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable("id") Integer id, @Valid @ModelAttribute("book") Book book, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("book", book);
            return "books/edit";
        }
        Book updatedBook = bookService.getBookById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sách với ID " + id + " không tồn tại"));
        updatedBook.setTitle(book.getTitle());
        updatedBook.setAuthor(book.getAuthor());
        updatedBook.setAvailable(book.isAvailable());
        bookService.updateBook(id, updatedBook);
        return "redirect:/books";
    }

    // Xóa sách
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Integer id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    // Mượn sách
    @PostMapping("/borrow/{id}")
    public String borrowBook(@PathVariable("id") Integer id, Model model) {
        Book book = bookService.getBookById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sách với ID " + id + " không tồn tại"));
        if (!"available".equals(book.getStatus())) {
            throw new IllegalStateException("Sách hiện không khả dụng để mượn");
        }
        // Thêm logic để xử lý mượn sách ở đây
        return "redirect:/books";
    }
}