package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.model.BorrowRecord;
import com.example.servingwebcontent.model.Book;
import com.example.servingwebcontent.model.User;
import com.example.servingwebcontent.service.BookService;
import com.example.servingwebcontent.service.UserService;
import com.example.servingwebcontent.service.BorrowRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/borrows")
public class BorrowRecordController {

    @Autowired
    private BorrowRecordService borrowRecordService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    // Hiển thị danh sách phiếu mượn
    @GetMapping
    public String listBorrowRecords(Model model, @RequestParam(name = "search", required = false) String search) {
        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("borrowRecords", borrowRecordService.searchBorrowRecords(search));
        } else {
            model.addAttribute("borrowRecords", borrowRecordService.getAllBorrowRecords());
        }
        return "borrows/list";
    }

    // Hiển thị form thêm phiếu mượn
    @GetMapping("/add")
    public String showAddBorrowRecordForm(Model model) {
        model.addAttribute("borrowRecord", new BorrowRecord());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("books", bookService.getAllBooks());
        return "borrows/add";
    }

    // Xử lý thêm phiếu mượn
    @PostMapping("/add")
    public String addBorrowRecord(@Valid @ModelAttribute("borrowRecord") BorrowRecord borrowRecord, BindingResult result, Model model) {
        // Kiểm tra lỗi validation từ @Valid
        if (result.hasErrors()) {
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("books", bookService.getAllBooks());
            return "borrows/add";
        }

        // Kiểm tra logic: dueDate không được trước borrowDate
        if (borrowRecord.getDueDate() != null && borrowRecord.getBorrowDate() != null &&
            borrowRecord.getDueDate().isBefore(borrowRecord.getBorrowDate())) {
            result.rejectValue("dueDate", "error.borrowRecord", "Ngày phải trả không được trước ngày mượn");
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("books", bookService.getAllBooks());
            return "borrows/add";
        }

        try {
            borrowRecordService.addBorrowRecord(borrowRecord);
            return "redirect:/borrows";
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage()); // Lỗi khi sách không khả dụng
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("books", bookService.getAllBooks());
            return "borrows/add";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Dữ liệu không hợp lệ: " + e.getMessage());
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("books", bookService.getAllBooks());
            return "borrows/add";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi hệ thống, vui lòng thử lại sau");
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("books", bookService.getAllBooks());
            return "borrows/add";
        }
    }

    // Hiển thị form sửa phiếu mượn
    @GetMapping("/edit/{id}")
    public String showEditBorrowRecordForm(@PathVariable("id") Integer id, Model model) {
        BorrowRecord borrowRecord = borrowRecordService.getBorrowRecordById(id)
                .orElseThrow(() -> new IllegalArgumentException("Phiếu mượn với ID " + id + " không tồn tại"));
        model.addAttribute("borrowRecord", borrowRecord);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("books", bookService.getAllBooks());
        return "borrows/edit";
    }

    // Xử lý sửa phiếu mượn
    @PostMapping("/edit/{id}")
    public String updateBorrowRecord(@PathVariable("id") Integer id, @Valid @ModelAttribute("borrowRecord") BorrowRecord borrowRecord, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("books", bookService.getAllBooks());
            return "borrows/edit";
        }
        borrowRecordService.updateBorrowRecord(id, borrowRecord);
        return "redirect:/borrows";
    }

    // Xóa phiếu mượn
    @GetMapping("/delete/{id}")
    public String deleteBorrowRecord(@PathVariable("id") Integer id) {
        borrowRecordService.deleteBorrowRecord(id);
        return "redirect:/borrows";
    }
}