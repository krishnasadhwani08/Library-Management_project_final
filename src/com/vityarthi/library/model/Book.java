package com.vityarthi.library.model;

import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int sNo;
    private String title;
    private String author;
    private int totalQty;
    private int issuedQty; 

    public Book(int sNo, String title, String author, int totalQty) {
        this.sNo = sNo;
        this.title = title;
        this.author = author;
        this.totalQty = totalQty;
        this.issuedQty = 0;
    }

    public int getSNo() { return sNo; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getTotalQty() { return totalQty; }
    public int getAvailableQty() { return totalQty - issuedQty; }
    public int getIssuedQty() { return issuedQty; }
    
    public void increaseQuantity(int qty) {
        this.totalQty += qty;
    }

    public boolean issueBook() {
        if (getAvailableQty() > 0) {
            issuedQty++;
            return true;
        }
        return false;
    }

    public boolean returnBook() {
        if (issuedQty > 0) {
            issuedQty--;
            return true;
        }
        return false;
    }

    public void showBookDetails() {
        System.out.printf("%-5d | %-30s | %-20s | %-10d | %-10d%n",
                          sNo, title, author, getAvailableQty(), totalQty);
    }
}