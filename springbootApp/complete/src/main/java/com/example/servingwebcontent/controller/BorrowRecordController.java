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

import java.security.Principal;
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
    public String listBorrowRecords(Model model,
                                   @RequestParam(name = "search", required = false) String search,
                                   @RequestParam(name = "status", required = false) String status) {
        List<BorrowRecord> records;
        if (search != null && !search.trim().isEmpty()) {
            records = borrowRecordService.searchBorrowRecords(search);
        } else {
            records = borrowRecordService.getAllBorrowRecords();
        }
        if (status != null && !status.isEmpty() && !"ALL".equals(status)) {
            try {
                BorrowRecord.Status filterStatus = BorrowRecord.Status.valueOf(status);
                records = records.stream().filter(r -> r.getStatus() == filterStatus).toList();
            } catch (IllegalArgumentException e) {
                // ignore invalid status
            }
        }
        model.addAttribute("borrowRecords", records);
        model.addAttribute("search", search);
        model.addAttribute("status", status);
        model.addAttribute("statusList", BorrowRecord.Status.values());
        return "borrows/list";
    }

    // Hiển thị danh sách phiếu mượn cá nhân
    @GetMapping("/my-borrows")
    public String listMyBorrowRecords(Model model, Principal principal) {
        Integer userId = 1; 
        try {
            List<BorrowRecord> borrowRecords = borrowRecordService.getBorrowRecordsByUser(userId);
            String stats = calculateStats(borrowRecords);
            model.addAttribute("borrowRecords", borrowRecords);
            model.addAttribute("stats", stats);
            model.addAttribute("maxExtendCount", 2); 
            return "borrows/my-borrows";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("borrowRecords", List.of());
            model.addAttribute("stats", "Không có phiếu mượn nào.");
            model.addAttribute("maxExtendCount", 2);
            return "borrows/my-borrows";
        }
    }

    // Hiển thị form thêm phiếu mượn
    @GetMapping("/add")
    public String showAddBorrowRecordForm(Model model) {
        BorrowRecord record = new BorrowRecord();
        record.setStatus(BorrowRecord.Status.BORROWED);
        model.addAttribute("borrowRecord", record);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("statusList", BorrowRecord.Status.values());
        return "borrows/add";
    }

    // Xử lý thêm phiếu mượn
    @PostMapping("/add")
    public String addBorrowRecord(@Valid @ModelAttribute("borrowRecord") BorrowRecord borrowRecord, BindingResult result, Model model) {
        if (borrowRecord.getStatus() == null) {
            borrowRecord.setStatus(BorrowRecord.Status.BORROWED);
        }
        if (result.hasErrors()) {
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("books", bookService.getAllBooks());
            model.addAttribute("statusList", BorrowRecord.Status.values());
            return "borrows/add";
        }
        if (borrowRecord.getDueDate() != null && borrowRecord.getBorrowDate() != null &&
                borrowRecord.getDueDate().isBefore(borrowRecord.getBorrowDate())) {
            result.rejectValue("dueDate", "error.borrowRecord", "Ngày phải trả không được trước ngày mượn");
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("books", bookService.getAllBooks());
            model.addAttribute("statusList", BorrowRecord.Status.values());
            return "borrows/add";
        }
        try {
            borrowRecordService.addBorrowRecord(borrowRecord);
            return "redirect:/borrows";
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("books", bookService.getAllBooks());
            model.addAttribute("statusList", BorrowRecord.Status.values());
            return "borrows/add";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Dữ liệu không hợp lệ: " + e.getMessage());
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("books", bookService.getAllBooks());
            model.addAttribute("statusList", BorrowRecord.Status.values());
            return "borrows/add";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi hệ thống, vui lòng thử lại sau");
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("books", bookService.getAllBooks());
            model.addAttribute("statusList", BorrowRecord.Status.values());
            return "borrows/add";
        }
    }

    // Hiển thị form sửa phiếu mượn
    @GetMapping("/edit/{id}")
    public String showEditBorrowRecordForm(@PathVariable("id") Integer id, Model model) {
        try {
            BorrowRecord borrowRecord = borrowRecordService.getBorrowRecordById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Phiếu mượn với ID " + id + " không tồn tại"));
            model.addAttribute("borrowRecord", borrowRecord);
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("books", bookService.getAllBooks());
            model.addAttribute("statusList", BorrowRecord.Status.values());
            return "borrows/edit";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            BorrowRecord record = new BorrowRecord();
            record.setStatus(BorrowRecord.Status.BORROWED);
            model.addAttribute("borrowRecord", record);
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("books", bookService.getAllBooks());
            model.addAttribute("statusList", BorrowRecord.Status.values());
            return "borrows/edit";
        }
    }

    // Xử lý sửa phiếu mượn
    @PostMapping("/edit/{id}")
    public String updateBorrowRecord(@PathVariable("id") Integer id, @Valid @ModelAttribute("borrowRecord") BorrowRecord borrowRecord, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("books", bookService.getAllBooks());
            model.addAttribute("statusList", BorrowRecord.Status.values());
            return "borrows/edit";
        }
        if (borrowRecord.getDueDate() != null && borrowRecord.getBorrowDate() != null &&
                borrowRecord.getDueDate().isBefore(borrowRecord.getBorrowDate())) {
            result.rejectValue("dueDate", "error.borrowRecord", "Ngày phải trả không được trước ngày mượn");
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("books", bookService.getAllBooks());
            model.addAttribute("statusList", BorrowRecord.Status.values());
            return "borrows/edit";
        }
        try {
            borrowRecordService.updateBorrowRecord(id, borrowRecord);
            return "redirect:/borrows";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Dữ liệu không hợp lệ: " + e.getMessage());
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("books", bookService.getAllBooks());
            model.addAttribute("statusList", BorrowRecord.Status.values());
            return "borrows/edit";
        }
    }

    // Xử lý trả sách
    @PostMapping("/return/{id}")
    public String returnBook(@PathVariable("id") Integer id, Model model, Principal principal) {
        Integer userId = 1; 
        try {
            borrowRecordService.returnBook(id, userId);
            return "redirect:/borrows/my-borrows";
        } catch (IllegalStateException | IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            List<BorrowRecord> borrowRecords = borrowRecordService.getBorrowRecordsByUser(userId);
            model.addAttribute("borrowRecords", borrowRecords);
            model.addAttribute("stats", calculateStats(borrowRecords));
            model.addAttribute("maxExtendCount", 2);
            return "borrows/my-borrows";
        }
    }

    // Xử lý gia hạn phiếu mượn
    @PostMapping("/extend/{id}")
    public String extendDueDate(@PathVariable("id") Integer id, @RequestParam("days") Integer additionalDays, Model model, Principal principal) {
        Integer userId = 1;
        try {
            borrowRecordService.extendDueDate(id, userId, additionalDays);
            return "redirect:/borrows/my-borrows";
        } catch (IllegalStateException | IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            List<BorrowRecord> borrowRecords = borrowRecordService.getBorrowRecordsByUser(userId);
            model.addAttribute("borrowRecords", borrowRecords);
            model.addAttribute("stats", calculateStats(borrowRecords));
            model.addAttribute("maxExtendCount", 2);
            return "borrows/my-borrows";
        }
    }

    // Xóa phiếu mượn
    @GetMapping("/delete/{id}")
    public String deleteBorrowRecord(@PathVariable("id") Integer id, Model model) {
        try {
            borrowRecordService.deleteBorrowRecord(id);
            return "redirect:/borrows";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("borrowRecords", borrowRecordService.getAllBorrowRecords());
            return "borrows/list";
        }
    }

    private String calculateStats(List<BorrowRecord> borrowRecords) {
        long totalBorrowed = borrowRecords.size();
        long notReturned = borrowRecords.stream()
                .filter(r -> r.getReturnDate() == null && r.getStatus() != BorrowRecord.Status.OVERDUE)
                .count();
        long overdue = borrowRecords.stream()
                .filter(r -> r.getStatus() == BorrowRecord.Status.OVERDUE)
                .count();
        return String.format("Bạn đã mượn %d sách, còn %d chưa trả, %d đã quá hạn.", totalBorrowed, notReturned, overdue);
    }
}