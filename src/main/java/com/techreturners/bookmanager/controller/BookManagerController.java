package com.techreturners.bookmanager.controller;

import com.techreturners.bookmanager.model.Book;
import com.techreturners.bookmanager.service.BookManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
public class BookManagerController {

    @Autowired
    BookManagerService bookManagerService;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookManagerService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping({"/{bookId}"})
    public ResponseEntity<Book> getBookById(@PathVariable Long bookId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        Book queryBook = null;
        try {
            queryBook = bookManagerService.getBookById(bookId);
        } catch (Exception e){
            httpHeaders.add("book", "book " + bookId + " not exist");
            return new ResponseEntity<>(queryBook, httpHeaders, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(queryBook, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        HttpHeaders httpHeaders = new HttpHeaders();
        Book queryBook = null;
        try {
            queryBook = bookManagerService.getBookById(book.getId());
            if (queryBook != null){
                httpHeaders.add("book", "book " + queryBook.getId().toString() + "already exist");
                return new ResponseEntity<>( httpHeaders, HttpStatus.IM_USED);
            }
        } catch (Exception e){ }

        Book newBook = bookManagerService.insertBook(book);

        httpHeaders.add("book", "/api/v1/book/" + newBook.getId().toString());
        return new ResponseEntity<>(newBook, httpHeaders, HttpStatus.CREATED);
    }

    //User Story 4 - Update Book By Id Solution
    @PutMapping({"/{bookId}"})
    public ResponseEntity<Book> updateBookById(@PathVariable("bookId") Long bookId, @RequestBody Book book) {
        HttpHeaders httpHeaders = new HttpHeaders();
        Book queryBook = null;
        queryBook = bookManagerService.getBookById(book.getId());
        if (queryBook == null){
            httpHeaders.add("book", "book " + queryBook.getId().toString() + "not exist");
            return new ResponseEntity<>(queryBook, httpHeaders, HttpStatus.NO_CONTENT);
        }
        bookManagerService.updateBookById(bookId, book);
        return new ResponseEntity<>(bookManagerService.getBookById(bookId), HttpStatus.OK);
    }

    @DeleteMapping({"/{bookId}"})
    public ResponseEntity<Book> deleteBookById(@PathVariable("bookId") Long bookId) {
        bookManagerService.deleteBookById(bookId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("book", String.format("book %d deleted.", bookId));
        return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
    }
}
