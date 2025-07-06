package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.model.BorrowRecord;
import com.example.servingwebcontent.service.BookService;
import com.example.servingwebcontent.service.BorrowRecordService;
import com.example.servingwebcontent.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/borrows")
public class BorrowRecordController {
    @Autowired
    private BorrowRecordService borrowRecordService;
    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String listBorrowRecords(Model model) {
        model.addAttribute("borrows", borrowRecordService.getAllBorrowRecords());
        return "borrows/list";
    }

    @GetMapping("/new")
    public String showBorrowForm(Model model) {
        model.addAttribute("borrow", new BorrowRecord());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("books", bookService.getAllBooks());
        return "borrows/form";
    }

    @PostMapping
    public String createBorrowRecord(@Valid @ModelAttribute("borrow") BorrowRecord borrowRecord, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("books", bookService.getAllBooks());
            return "borrows/form";
        }
        borrowRecordService.createBorrowRecord(borrowRecord);
        return "redirect:/borrows";
    }

    @GetMapping("/return/{id}")
    public String returnBook(@PathVariable("id") Long id) {
        borrowRecordService.returnBook(id);
        return "redirect:/borrows";
    }

    @GetMapping("/delete/{id}")
    public String deleteBorrowRecord(@PathVariable("id") Long id) {
        borrowRecordService.deleteBorrowRecord(id);
        return "redirect:/borrows";
    }
}