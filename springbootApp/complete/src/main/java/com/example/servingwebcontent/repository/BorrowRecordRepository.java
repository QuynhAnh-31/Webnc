package com.example.servingwebcontent.repository;

import com.example.servingwebcontent.model.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Integer> {

    @Query("SELECT br FROM BorrowRecord br WHERE LOWER(br.user.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(br.book.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<BorrowRecord> findByUserNameOrBookTitleContainingIgnoreCase(String keyword);

    List<BorrowRecord> findByUserId(Integer userId);

    List<BorrowRecord> findByBookId(Integer bookId);

    List<BorrowRecord> findByStatus(BorrowRecord.Status status);

    @Query("SELECT br FROM BorrowRecord br WHERE br.extendCount >= :extendCount")
    List<BorrowRecord> findByExtendCountGreaterThanOrEqual(Integer extendCount);

    @Query("SELECT br FROM BorrowRecord br WHERE " +
           "(LOWER(br.user.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(br.book.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:status IS NULL OR br.status = :status)")
    List<BorrowRecord> searchByKeywordAndStatus(String keyword, BorrowRecord.Status status);
}