package com.example.servingwebcontent.service;

import com.example.servingwebcontent.model.Book;
import com.example.servingwebcontent.model.BorrowRecord;
import com.example.servingwebcontent.model.User;
import com.example.servingwebcontent.repository.BorrowRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowRecordService {

    private static final int MAX_EXTEND_COUNT = 2; // Giới hạn số lần gia hạn

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    // Lấy tất cả phiếu mượn
    public List<BorrowRecord> getAllBorrowRecords() {
        List<BorrowRecord> records = borrowRecordRepository.findAll();
        updateStatusForRecords(records);
        return records;
    }

    // Tìm kiếm phiếu mượn theo tên người dùng hoặc tiêu đề sách
    public List<BorrowRecord> searchBorrowRecords(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBorrowRecords();
        }
        List<BorrowRecord> records = borrowRecordRepository.findByUserNameOrBookTitleContainingIgnoreCase(keyword);
        updateStatusForRecords(records);
        return records;
    }

    // Lấy phiếu mượn theo ID
    public Optional<BorrowRecord> getBorrowRecordById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID phiếu mượn không được để trống");
        }
        Optional<BorrowRecord> record = borrowRecordRepository.findById(id);
        record.ifPresent(this::updateStatus);
        return record;
    }

    // Thêm phiếu mượn
    public void addBorrowRecord(BorrowRecord borrowRecord) {
        if (borrowRecord.getUser() == null || borrowRecord.getUser().getId() == null) {
            throw new IllegalArgumentException("Người dùng không được để trống");
        }
        if (borrowRecord.getBook() == null || borrowRecord.getBook().getId() == null) {
            throw new IllegalArgumentException("Sách không được để trống");
        }
        if (borrowRecord.getBorrowDate() == null || borrowRecord.getDueDate() == null) {
            throw new IllegalArgumentException("Ngày mượn và ngày đến hạn không được để trống");
        }
        if (borrowRecord.getDueDate().isBefore(borrowRecord.getBorrowDate())) {
            throw new IllegalArgumentException("Ngày đến hạn không được trước ngày mượn");
        }
        User user = userService.getUserById(borrowRecord.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));
        Book book = bookService.getBookById(borrowRecord.getBook().getId())
                .orElseThrow(() -> new IllegalArgumentException("Sách không tồn tại"));
        if (!"available".equals(book.getStatus())) {
            throw new IllegalStateException("Sách hiện không khả dụng để mượn");
        }
        borrowRecord.setUser(user);
        borrowRecord.setBook(book);
        book.setStatus("borrowed");
        bookService.updateBook(book.getId(), book);
        // Không cần setStatus vì @PrePersist trong BorrowRecord đã đặt BORROWED
        borrowRecordRepository.save(borrowRecord);
    }

    // Cập nhật phiếu mượn
    public void updateBorrowRecord(Integer id, BorrowRecord updatedRecord) {
        if (id == null || updatedRecord == null) {
            throw new IllegalArgumentException("ID hoặc thông tin phiếu mượn không được để trống");
        }
        BorrowRecord record = borrowRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Phiếu mượn với ID " + id + " không tồn tại"));
        if (updatedRecord.getBorrowDate() != null) {
            record.setBorrowDate(updatedRecord.getBorrowDate());
        }
        if (updatedRecord.getDueDate() != null) {
            record.setDueDate(updatedRecord.getDueDate());
        }
        if (updatedRecord.getReturnDate() != null) {
            record.setReturnDate(updatedRecord.getReturnDate());
        }
        updateStatus(record);
        if (record.getStatus() == BorrowRecord.Status.RETURNED) {
            Book book = bookService.getBookById(record.getBook().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Sách không tồn tại"));
            book.setStatus("available");
            bookService.updateBook(book.getId(), book);
        }
        borrowRecordRepository.save(record);
    }

    // Xóa phiếu mượn
    public void deleteBorrowRecord(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID phiếu mượn không được để trống");
        }
        BorrowRecord record = borrowRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Phiếu mượn với ID " + id + " không tồn tại"));
        if (record.getStatus() == BorrowRecord.Status.BORROWED || record.getStatus() == BorrowRecord.Status.OVERDUE) {
            Book book = bookService.getBookById(record.getBook().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Sách không tồn tại"));
            book.setStatus("available");
            bookService.updateBook(book.getId(), book);
        }
        borrowRecordRepository.deleteById(id);
    }

    // Lấy danh sách phiếu mượn theo userId
    public List<BorrowRecord> getBorrowRecordsByUser(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("ID người dùng không được để trống");
        }
        List<BorrowRecord> records = borrowRecordRepository.findByUserId(userId);
        updateStatusForRecords(records);
        return records;
    }

    // Trả sách
    public void returnBook(Integer borrowRecordId, Integer userId) {
        if (borrowRecordId == null || userId == null) {
            throw new IllegalArgumentException("ID phiếu mượn hoặc người dùng không được để trống");
        }
        BorrowRecord borrowRecord = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new IllegalArgumentException("Phiếu mượn không tồn tại"));
        if (!borrowRecord.getUser().getId().equals(userId)) {
            throw new IllegalStateException("Bạn không có quyền trả sách này");
        }
        if (borrowRecord.getReturnDate() != null) {
            throw new IllegalStateException("Sách đã được trả");
        }
        Book book = borrowRecord.getBook();
        book.setStatus("available");
        bookService.updateBook(book.getId(), book);
        borrowRecord.setReturnDate(LocalDate.now());
        borrowRecord.setStatus(BorrowRecord.Status.RETURNED);
        borrowRecordRepository.save(borrowRecord);
    }

    // Gia hạn phiếu mượn
    public void extendDueDate(Integer borrowRecordId, Integer userId, Integer additionalDays) {
        if (borrowRecordId == null || userId == null || additionalDays == null || additionalDays <= 0) {
            throw new IllegalArgumentException("Thông tin gia hạn không hợp lệ");
        }
        BorrowRecord borrowRecord = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new IllegalArgumentException("Phiếu mượn không tồn tại"));
        if (!borrowRecord.getUser().getId().equals(userId)) {
            throw new IllegalStateException("Bạn không có quyền gia hạn phiếu mượn này");
        }
        if (borrowRecord.getReturnDate() != null) {
            throw new IllegalStateException("Không thể gia hạn phiếu mượn đã trả");
        }
        if (borrowRecord.getExtendCount() >= MAX_EXTEND_COUNT) {
            throw new IllegalStateException("Đã đạt số lần gia hạn tối đa (" + MAX_EXTEND_COUNT + ")");
        }
        LocalDate newDueDate = borrowRecord.getDueDate().plusDays(additionalDays);
        if (newDueDate.isBefore(LocalDate.now())) {
            throw new IllegalStateException("Ngày gia hạn không hợp lệ");
        }
        borrowRecord.setDueDate(newDueDate);
        borrowRecord.setExtendCount(borrowRecord.getExtendCount() + 1);
        updateStatus(borrowRecord);
        borrowRecordRepository.save(borrowRecord);
    }

    // Cập nhật trạng thái phiếu mượn
    private void updateStatus(BorrowRecord record) {
        if (record.getReturnDate() != null) {
            record.setStatus(BorrowRecord.Status.RETURNED);
        } else if (record.getDueDate().isBefore(LocalDate.now())) {
            record.setStatus(BorrowRecord.Status.OVERDUE);
        } else {
            record.setStatus(BorrowRecord.Status.BORROWED);
        }
    }

    // Cập nhật trạng thái cho danh sách phiếu mượn
    private void updateStatusForRecords(List<BorrowRecord> records) {
        List<BorrowRecord> recordsToSave = new ArrayList<>();
        for (BorrowRecord record : records) {
            BorrowRecord.Status oldStatus = record.getStatus();
            updateStatus(record);
            if (record.getStatus() != oldStatus) {
                recordsToSave.add(record);
            }
        }
        if (!recordsToSave.isEmpty()) {
            borrowRecordRepository.saveAll(recordsToSave);
        }
    }
}