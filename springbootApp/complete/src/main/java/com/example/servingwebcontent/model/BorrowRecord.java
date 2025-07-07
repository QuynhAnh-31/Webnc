package com.example.servingwebcontent.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "borrow_records")
@Getter
@Setter
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Người mượn không được để trống")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Sách không được để trống")
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @NotNull(message = "Ngày mượn không được để trống")
    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;

    @NotNull(message = "Ngày phải trả không được để trống")
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @NotNull(message = "Trạng thái không được để trống")
    @Column(name = "status", nullable = false)
    private String status; // "borrowed", "returned", "overdue"

    @PrePersist
    protected void onCreate() {
        status = "borrowed";
    }
}