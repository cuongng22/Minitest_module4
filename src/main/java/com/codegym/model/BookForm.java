package com.codegym.model;

import org.springframework.web.multipart.MultipartFile;

public class BookForm {
    private Long id;

    private String name;

    private double price;

    private String author;

    private MultipartFile avatar;

    private Category category;

    public BookForm() {
    }

    public BookForm(Long id, String name, double price, String author, MultipartFile avatar, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.author = author;
        this.avatar = avatar;
        this.category = category;
    }

    public BookForm(String name, double price, String author, MultipartFile avatar, Category category) {
        this.name = name;
        this.price = price;
        this.author = author;
        this.avatar = avatar;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public MultipartFile getAvatar() {
        return avatar;
    }

    public void setAvatar(MultipartFile avatar) {
        this.avatar = avatar;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
