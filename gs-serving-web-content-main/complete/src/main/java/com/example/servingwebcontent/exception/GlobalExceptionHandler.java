package com.example.servingwebcontent.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException ex, Model model) {
        logger.error("Không tìm thấy tài nguyên: {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(InvalidOperationException.class)
    public String handleInvalidOperationException(InvalidOperationException ex, Model model) {
        logger.error("Thao tác không hợp lệ: {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrityViolationException(DataIntegrityViolationException ex, Model model) {
        logger.error("Lỗi cơ sở dữ liệu: {}", ex.getMessage());
        model.addAttribute("errorMessage", "Lỗi cơ sở dữ liệu: Không thể thực hiện thao tác");
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        logger.error("Lỗi không mong muốn: {}", ex.getMessage());
        model.addAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn");
        return "error";
    }
}