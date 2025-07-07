package com.example.servingwebcontent.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Table(name = "borrow_records", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_book_id", columnList = "book_id")
})
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
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
    @Enumerated(EnumType.STRING)
   private Status status = Status.BORROWED; // Khởi tạo mặc định


    @NotNull(message = "Số lần gia hạn không được để trống")
    @Column(name = "extend_count", nullable = false)
    private Integer extendCount = 0;

    public enum Status {
        BORROWED, RETURNED, OVERDUE
    }

    @PrePersist
    protected void prePersist() {
        if (status == null) {
            status = Status.BORROWED;
        }
        if (extendCount == null) {
            extendCount = 0;
        }
    }

    @PreUpdate
    protected void preUpdate() {
        if (extendCount == null) {
            extendCount = 0;
        }
    }
}