package com.example.quan_ly_sach.controller;

import com.example.quan_ly_sach.service.IBookService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {

    private final IBookService bookService;

    public BookController(IBookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String listBooks(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size) {
        model.addAttribute("bookPage", bookService.listBooks(PageRequest.of(page, size)));
        return "book/list";
    }

    @GetMapping("/borrow/{id}")
    public String borrowBook(@PathVariable Integer id, Model model) {
        String code = bookService.borrowBook(id);
        model.addAttribute("message", "Mượn sách thành công! Mã trả sách: " + code);
        return "book/borrow-success";
    }

    @GetMapping("/return")
    public String returnForm() {
        return "book/return-book";
    }

    @PostMapping("/return")
    public String checkReturnCode(@RequestParam String code, Model model) {
        var book = bookService.getBookByBorrowCode(code);
        model.addAttribute("book", book);
        model.addAttribute("code", code);
        return "book/return-book";
    }

    @PostMapping("/return/confirm")
    public String confirmReturn(@RequestParam String code, Model model) {
        bookService.returnBook(code);
        model.addAttribute("message", "Trả sách thành công");
        return "book/return-success";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        return "book/detail";
    }

}