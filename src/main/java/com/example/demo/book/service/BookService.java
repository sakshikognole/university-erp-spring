package com.example.demo.book.service;

import com.example.demo.book.dto.PageResponse;
import com.example.demo.book.exception.ResourceNotFoundException;
import com.example.demo.book.model.Book;
import com.example.demo.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public PageResponse<Book> getAllBooks(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("bookTitle").ascending());
        Page<Book> result = (search == null || search.isBlank())
                ? bookRepository.findAll(pageable)
                : bookRepository.searchBooks(search.trim(), pageable);
        return toPageResponse(result);
    }

    public Book getById(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    public Book create(Book book) {
        book.setId(null); // let MongoDB generate it
        return bookRepository.save(book);
    }

    public Book update(String id, Book book) {
        Book existing = getById(id);
        existing.setBookTitle(book.getBookTitle());
        existing.setAuthorName(book.getAuthorName());
        existing.setTotalCopies(book.getTotalCopies());
        existing.setBookLocation(book.getBookLocation());
        existing.setDepartment(book.getDepartment());
        return bookRepository.save(existing);
    }

    public void delete(String id) {
        getById(id); // throws if not found
        bookRepository.deleteById(id);
    }

    private PageResponse<Book> toPageResponse(Page<Book> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}
