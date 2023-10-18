package com.example.marketplace.services;

import com.example.marketplace.appexceptions.AppException;
import com.example.marketplace.dto.BookDto;
import com.example.marketplace.entities.Book;
import com.example.marketplace.entities.Views;
import com.example.marketplace.repositories.BookRepository;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) throws AppException{
        return bookRepository.findById(id).orElseThrow(() -> new AppException("error", "Book with id %d doesn't exists".formatted(id), 404));
    }

    @Transactional
    public Book updateBook(Long id, BookDto newValue) throws AppException {
        if(bookRepository.existsById(id)){
            Book book = bookRepository.findById(id).get();
            if(newValue.getAuthor() != null){
                book.setAuthor(newValue.getAuthor());
            }
            if(newValue.getTitle() != null){
                book.setTitle(newValue.getTitle());
            }
            if(newValue.getPrice() != null){
                book.setPrice(newValue.getPrice());
            }
            if(newValue.getSellerNumber() != null){
                book.setSellerNumber(newValue.getSellerNumber());
            }
            if(newValue.getInStock() != null){
                book.setInStock(newValue.getInStock());
            }
            return book;
        }
        throw new AppException("error", "Book with this id doesn't exists", 404);
    }

    @Transactional
    public Book createBook(BookDto bookDto) throws AppException{
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setPrice(bookDto.getPrice());
        book.setSellerNumber(bookDto.getSellerNumber());
        book.setInStock(bookDto.getInStock());
        bookRepository.save(book);
        return book;
    }

    @Transactional
    public Book deleteById(Long id) throws AppException{
        Book book = bookRepository.findById(id).orElse(null);
        if(book != null){
            bookRepository.deleteById(id);
            return book;
        }
        throw new AppException("error", "Book with this id doesn't exists", 404);
    }

    @Transactional
    public void deleteAll(){
        bookRepository.deleteAll();
    }
}
