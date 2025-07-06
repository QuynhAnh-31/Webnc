package com.example.servingwebcontent.repository;

import com.example.servingwebcontent.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}