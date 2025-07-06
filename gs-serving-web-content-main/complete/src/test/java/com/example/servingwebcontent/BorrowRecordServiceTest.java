package com.example.servingwebcontent;

import com.example.servingwebcontent.exception.InvalidOperationException;
import com.example.servingwebcontent.model.Book;
import com.example.servingwebcontent.model.BorrowRecord;
import com.example.servingwebcontent.model.User;
import com.example.servingwebcontent.repository.BookRepository;
import com.example.servingwebcontent.repository.BorrowRecordRepository;
import com.example.servingwebcontent.repository.UserRepository;
import com.example.servingwebcontent.service.BorrowRecordService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BorrowRecordServiceTest {
    @Mock
    private BorrowRecordRepository borrowRecordRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BorrowRecordService borrowRecordService;

    @Test
    public void testCreateBorrowRecord_BookNotAvailable() {
        BorrowRecord borrowRecord = new BorrowRecord();
        Book book = new Book();
        book.setId(1L);
        book.setAvailable(false);
        User user = new User();
        user.setId(1L);
        borrowRecord.setBook(book);
        borrowRecord.setUser(user);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(InvalidOperationException.class, () -> borrowRecordService.createBorrowRecord(borrowRecord));
    }

    @Test
    public void testReturnBook_AlreadyReturned() {
        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setId(1L);
        borrowRecord.setReturned(true);

        when(borrowRecordRepository.findById(1L)).thenReturn(Optional.of(borrowRecord));

        assertThrows(InvalidOperationException.class, () -> borrowRecordService.returnBook(1L));
    }
}