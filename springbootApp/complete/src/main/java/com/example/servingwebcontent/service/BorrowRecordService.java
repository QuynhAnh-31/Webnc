package com.example.servingwebcontent.service;

import com.example.servingwebcontent.model.Book;
import com.example.servingwebcontent.model.BorrowRecord;
import com.example.servingwebcontent.model.User;
import com.example.servingwebcontent.repository.BorrowRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowRecordService {

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
        List<BorrowRecord> records = borrowRecordRepository.findByUserNameOrBookTitleContainingIgnoreCase(keyword);
        updateStatusForRecords(records);
        return records;
    }

    // Lấy phiếu mượn theo ID
    public Optional<BorrowRecord> getBorrowRecordById(Integer id) {
        Optional<BorrowRecord> record = borrowRecordRepository.findById(id);
        record.ifPresent(this::updateStatus);
        return record;
    }

    // Thêm phiếu mượn
    public void addBorrowRecord(BorrowRecord borrowRecord) {
        // Kiểm tra user có tồn tại không
        User user = userService.getUserById(borrowRecord.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

        // Kiểm tra sách có khả dụng không
        Book book = bookService.getBookById(borrowRecord.getBook().getId())
                .orElseThrow(() -> new IllegalArgumentException("Sách không tồn tại"));
        if (!"available".equals(book.getStatus())) {
            throw new IllegalStateException("Sách hiện không khả dụng để mượn");
        }
        // Đánh dấu sách đã được mượn
        book.setStatus("borrowed");
        bookService.saveBook(book);

        // Không cần setStatus ở đây nữa vì đã có @PrePersist
        borrowRecordRepository.save(borrowRecord);
    }

    // Cập nhật phiếu mượn (trả sách)
    public void updateBorrowRecord(Integer id, BorrowRecord updatedRecord) {
        Optional<BorrowRecord> existingRecord = borrowRecordRepository.findById(id);
        if (existingRecord.isPresent()) {
            BorrowRecord record = existingRecord.get();
            record.setReturnDate(updatedRecord.getReturnDate());
            updateStatus(record);
            if ("returned".equals(record.getStatus())) {
                Book book = bookService.getBookById(record.getBook().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Sách không tồn tại"));
                book.setStatus("available");
                bookService.updateBook(book.getId(), book);
            }
            borrowRecordRepository.save(record);
        } else {
            throw new IllegalArgumentException("Phiếu mượn với ID " + id + " không tồn tại");
        }
    }

    // Xóa phiếu mượn
    public void deleteBorrowRecord(Integer id) {
        Optional<BorrowRecord> record = borrowRecordRepository.findById(id);
        if (record.isPresent()) {
            BorrowRecord borrowRecord = record.get();
            if ("borrowed".equals(borrowRecord.getStatus()) || "overdue".equals(borrowRecord.getStatus())) {
                Book book = bookService.getBookById(borrowRecord.getBook().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Sách không tồn tại"));
                book.setStatus("available");
                bookService.updateBook(book.getId(), book);
            }
            borrowRecordRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Phiếu mượn với ID " + id + " không tồn tại");
        }
    }

    // Cập nhật trạng thái phiếu mượn
    private void updateStatus(BorrowRecord record) {
        if (record.getReturnDate() != null) {
            record.setStatus("returned");
        } else if (record.getDueDate().isBefore(LocalDate.now())) {
            record.setStatus("overdue");
        } else {
            record.setStatus("borrowed");
        }
    }

    // Cập nhật trạng thái cho danh sách phiếu mượn
    private void updateStatusForRecords(List<BorrowRecord> records) {
        for (BorrowRecord record : records) {
            updateStatus(record);
            borrowRecordRepository.save(record);
        }
    }
}