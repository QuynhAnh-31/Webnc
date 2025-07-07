package com.example.servingwebcontent.service;

import com.example.servingwebcontent.model.Book;
import com.example.servingwebcontent.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    // Lấy tất cả sách
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Tìm kiếm sách theo tiêu đề
    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    // Thêm sách
    public void addBook(Book book) {
        if (book.getStatus() == null || book.getStatus().isEmpty()) {
            book.setStatus("available");
        }
        bookRepository.save(book);
    }

    // Lấy sách theo ID
    public Optional<Book> getBookById(Integer id) {
        return bookRepository.findById(id);
    }

    // Cập nhật sách
    public void updateBook(Integer id, Book updatedBook) {
        Optional<Book> existingBook = bookRepository.findById(id);
        if (existingBook.isPresent()) {
            Book book = existingBook.get();
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setCategory(updatedBook.getCategory());
            book.setAvailable(updatedBook.isAvailable());
            book.setStatus(updatedBook.getStatus()); // Đồng bộ thêm trường status nếu cần
            bookRepository.save(book);
        } else {
            throw new IllegalArgumentException("Sách với ID " + id + " không tồn tại");
        }
    }

    // Cập nhật trạng thái sách
    public void updateBookStatus(Integer id, String status) {
        Optional<Book> existingBook = bookRepository.findById(id);
        if (existingBook.isPresent()) {
            Book book = existingBook.get();
            book.setStatus(status);
            // Nếu muốn đồng bộ trường available với status:
            book.setAvailable("available".equals(status));
            bookRepository.save(book);
        } else {
            throw new IllegalArgumentException("Sách với ID " + id + " không tồn tại");
        }
    }

    // Xóa sách
    public void deleteBook(Integer id) {
        if (!bookRepository.existsById(id)) {
            throw new IllegalArgumentException("Sách với ID " + id + " không tồn tại");
        }
        bookRepository.deleteById(id);
    }

    // Lưu sách (thêm mới hoặc cập nhật)
    public void saveBook(Book book) {
        bookRepository.save(book);
    }
}