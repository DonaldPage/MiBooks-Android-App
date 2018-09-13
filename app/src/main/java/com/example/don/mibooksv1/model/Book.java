package com.example.don.mibooksv1.model;

public class Book {

    private int id;
    private String title;
    private String category;
    private String isbn;
    private String description;
    private String image;

    public Book() {
    }

    public Book(String title, String category, String isbn, String description, String image) {
        this.title = title;
        this.category = category;
        this.isbn = isbn;
        this.description = description;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

