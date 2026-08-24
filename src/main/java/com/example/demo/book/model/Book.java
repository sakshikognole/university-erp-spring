package com.example.demo.book.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    private String id;

    @NotBlank(message = "Book title is required")
    private String bookTitle;

    @NotBlank(message = "Author name is required")
    private String authorName;

    @NotNull(message = "Total copies is required")
    @Min(value = 0, message = "Total copies cannot be negative")
    private Integer totalCopies;

    @NotBlank(message = "Book location is required")
    private String bookLocation;

    @NotBlank(message = "Department is required")
    private String department;
}
