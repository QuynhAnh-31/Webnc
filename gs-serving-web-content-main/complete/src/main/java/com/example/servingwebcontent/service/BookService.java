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

    // Thêm sách
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    // Sửa sách
    public Book updateBook(Long id, Book updatedBook) {
        Optional<Book> existingBook = bookRepository.findById(id);
        if (existingBook.isPresent()) {
            Book book = existingBook.get();
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setCategory(updatedBook.getCategory());
            book.setAvailable(updatedBook.isAvailable());
            return bookRepository.save(book);
        }
        return null;
    }

    // Xóa sách
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    // Lấy tất cả sách
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Tìm kiếm sách theo tên
    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    // Lấy sách theo ID
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }
}