package com.example.servingwebcontent.service;

import com.example.servingwebcontent.exception.InvalidOperationException;
import com.example.servingwebcontent.exception.ResourceNotFoundException;
import com.example.servingwebcontent.model.Book;
import com.example.servingwebcontent.model.BorrowRecord;
import com.example.servingwebcontent.model.User;
import com.example.servingwebcontent.repository.BookRepository;
import com.example.servingwebcontent.repository.BorrowRecordRepository;
import com.example.servingwebcontent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowRecordService {
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowRecordRepository.findAll();
    }

    public BorrowRecord getBorrowRecordById(Long id) {
        return borrowRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu mượn với id: " + id));
    }

    public BorrowRecord createBorrowRecord(BorrowRecord borrowRecord) {
        @SuppressWarnings("unused")
        User user = userRepository.findById(borrowRecord.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
        Book book = bookRepository.findById(borrowRecord.getBook().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sách"));

        if (!book.isAvailable()) {
            throw new InvalidOperationException("Sách hiện không sẵn có để mượn");
        }

        book.setAvailable(false);
        borrowRecord.setBorrowDate(LocalDate.now());
        borrowRecord.setReturned(false);
        bookRepository.save(book);
        return borrowRecordRepository.save(borrowRecord);
    }

    public BorrowRecord returnBook(Long id) {
        BorrowRecord borrowRecord = getBorrowRecordById(id);
        if (borrowRecord.isReturned()) {
            throw new InvalidOperationException("Sách đã được trả");
        }

        Book book = borrowRecord.getBook();
        book.setAvailable(true);
        borrowRecord.setReturnDate(LocalDate.now());
        borrowRecord.setReturned(true);
        bookRepository.save(book);
        return borrowRecordRepository.save(borrowRecord);
    }

    public void deleteBorrowRecord(Long id) {
        BorrowRecord borrowRecord = getBorrowRecordById(id);
        borrowRecordRepository.delete(borrowRecord);
    }
}