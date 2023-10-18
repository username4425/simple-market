package com.example.marketplace.controllers;

import com.example.marketplace.appexceptions.AppException;
import com.example.marketplace.dto.BookDto;
import com.example.marketplace.entities.Book;
import com.example.marketplace.entities.Client;
import com.example.marketplace.entities.Views;
import com.example.marketplace.services.BookService;
import com.example.marketplace.services.ClientService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {
    private BookService bookService;
    private ClientService clientService;

    public BookController(BookService bookService, ClientService clientService) {
        this.bookService = bookService;
        this.clientService = clientService;
    }

    @GetMapping
    @CrossOrigin("*")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.ProductDescription.class)
    public List<Book> getAllBooks(){
        List<Book> books = bookService.getAllBooks();
        return books;
    }

    @GetMapping("/{id}")
    @CrossOrigin("*")
    @JsonView(Views.ProductDescription.class)
    public ResponseEntity<?> getBookById(@PathVariable Long id) throws AppException {
        Book book = bookService.getBookById(id);
        if(book == null) throw new AppException("error", "Book with id %d doesn't exists".formatted(id), 404);
        return ResponseEntity.ok().body(book);
    }

    @PostMapping
    @CrossOrigin("*")
    @JsonView(Views.ProductDescription.class)
    public ResponseEntity<?> createBook(@RequestBody BookDto bookDto, Principal principal) throws AppException{
        Client currentSeller = clientService.getClientByUsername(principal.getName());
        bookDto.setSellerNumber(currentSeller.getId());
        Book book = bookService.createBook(bookDto);
        return ResponseEntity.ok().body(book);
    }

    @PatchMapping("/{id}")
    @CrossOrigin("*")
    @JsonView(Views.ProductDescription.class)
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody BookDto bookDto, Principal principal) throws AppException{
        Client currentSeller = clientService.getClientByUsername(principal.getName());
        Book book = bookService.getBookById(id);
        if(!currentSeller.getId().equals(book.getSellerNumber()))
            throw new AppException("error", "Denied", 401);
        book = bookService.updateBook(id, bookDto);
        return ResponseEntity.ok().body(book);
    }

    @DeleteMapping("/{id}")
    @CrossOrigin("*")
    @JsonView(Views.ProductDescription.class)
    public ResponseEntity<?> deleteBook(@PathVariable Long id, Principal principal) throws AppException{
        Client currentSeller = clientService.getClientByUsername(principal.getName());
        Book book = bookService.getBookById(id);
        if(!currentSeller.getId().equals(book.getSellerNumber()))
            throw new AppException("error", "Denied", 401);
        bookService.deleteById(id);
        return ResponseEntity.ok().body(book);
    }
}
