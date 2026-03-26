package com.vityarthi.library;

import com.vityarthi.library.model.Book;
import com.vityarthi.library.model.Member;
import com.vityarthi.library.service.LibraryManager;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static LibraryManager manager;
    private static Scanner scanner;

    public static void main(String[] args) {
        manager = new LibraryManager();
        scanner = new Scanner(System.in);
        int choice;

        do {
            displayMainMenu();
            try {
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1: addBookMenu(); break;
                    case 2: showAllBooks(); break;
                    case 3: searchBookMenu(); break;
                    case 4: addMemberMenu(); break;
                    case 5: showAllMembers(); break;
                    case 6: issueBookMenu(); break;
                    case 7: returnBookMenu(); break;
                    case 0: 
                        manager.shutdown();
                        System.out.println("\nExiting application. Data saved."); 
                        break;
                    default:
                        System.out.println("\nInvalid choice.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\n--- Invalid Input: Please enter a number. ---");
                scanner.nextLine(); 
                choice = -1;
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                choice = -1;
            }
        } while (choice != 0);
        scanner.close();
    }

    private static void displayMainMenu() {
        System.out.println("\n==== Vityarthi Library Management System ====");
        System.out.println("1. Add New Book");
        System.out.println("2. Show All Books");
        System.out.println("3. Search Books");
        System.out.println("4. Register Member");
        System.out.println("5. Show All Members");
        System.out.println("6. Issue Book");
        System.out.println("7. Return Book");
        System.out.println("0. Exit");
        System.out.println("----------------------------------------------");
    }

    private static void addBookMenu() {
        System.out.println("\n--- Add New Book ---");
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Author: ");
        String author = scanner.nextLine();
        System.out.print("Quantity: ");
        int qty;
        try {
            qty = scanner.nextInt();
            scanner.nextLine();
            manager.addBook(title, author, qty);
        } catch (InputMismatchException e) {
            System.out.println("Invalid quantity.");
            scanner.nextLine();
        }
    }

    private static void showAllBooks() {
        List<Book> books = manager.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("\nNo books found.");
            return;
        }
        System.out.printf("%-5s | %-30s | %-20s | %-10s | %-10s%n", "S.No", "Title", "Author", "Avail", "Total");
        System.out.println("-".repeat(85));
        for (Book book : books) book.showBookDetails();
    }

    private static void searchBookMenu() {
        System.out.print("\nEnter keyword: ");
        String keyword = scanner.nextLine();
        List<Book> results = manager.searchBooks(keyword);
        if (results.isEmpty()) {
            System.out.println("No matching books.");
            return;
        }
        for (Book book : results) book.showBookDetails();
    }

    private static void addMemberMenu() {
        System.out.print("\nMember Name: ");
        String name = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        manager.addMember(name, phone);
    }

    private static void showAllMembers() {
        List<Member> members = manager.getAllMembers();
        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }
        System.out.printf("%-10s | %-25s | %-15s | %-10s%n", "ID", "Name", "Phone", "Borrowed");
        System.out.println("-".repeat(70));
        for (Member member : members) member.showMemberDetails();
    }

    private static void issueBookMenu() {
        try {
            System.out.print("\nBook S.No: ");
            int sNo = scanner.nextInt();
            System.out.print("Member ID: ");
            int mid = scanner.nextInt();
            scanner.nextLine();
            System.out.println(manager.issueBook(sNo, mid));
        } catch (Exception e) {
            System.out.println("Invalid input.");
            scanner.nextLine();
        }
    }

    private static void returnBookMenu() {
        try {
            System.out.print("\nBook S.No: ");
            int sNo = scanner.nextInt();
            System.out.print("Member ID: ");
            int mid = scanner.nextInt();
            scanner.nextLine();
            System.out.println(manager.returnBook(sNo, mid));
        } catch (Exception e) {
            System.out.println("Invalid input.");
            scanner.nextLine();
        }
    }
}