package com.vityarthi.library.model;

import java.io.Serializable;

public class Member implements Serializable {
    private static final long serialVersionUID = 1L;

    private int memberId;
    private String name;
    private String phone;
    private int borrowedCount;

    public Member(int memberId, String name, String phone) {
        this.memberId = memberId;
        this.name = name;
        this.phone = phone;
        this.borrowedCount = 0;
    }

    public int getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public int getBorrowedCount() { return borrowedCount; }
    
    public void incrementBorrowed() {
        this.borrowedCount++;
    }

    public void decrementBorrowed() {
        if (this.borrowedCount > 0) {
            this.borrowedCount--;
        }
    }

    public void showMemberDetails() {
        System.out.printf("%-10d | %-25s | %-15s | %-10d%n",
                          memberId, name, phone, borrowedCount);
    }
}