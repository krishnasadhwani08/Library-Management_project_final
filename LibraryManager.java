package com.vityarthi.library.service;

import com.vityarthi.library.model.Book;
import com.vityarthi.library.model.Member;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryManager {
    private List<Book> books;
    private List<Member> members;
    private static final String DATA_FILE = "data/library.ser";
    private int nextBookSNo = 101;
    private int nextMemberId = 1001;

    public LibraryManager() {
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
        loadData();
    }
    
    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(books);
            oos.writeObject(members);
            oos.writeObject(nextBookSNo);
            oos.writeObject(nextMemberId);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        File dataFile = new File(DATA_FILE);
        if (dataFile.exists() && dataFile.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
                books = (List<Book>) ois.readObject();
                members = (List<Member>) ois.readObject();
                nextBookSNo = (int) ois.readObject();
                nextMemberId = (int) ois.readObject();
                System.out.println("Data loaded successfully.");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading data (File might be corrupted). Starting fresh.");
            }
        }
        
        if (books == null) books = new ArrayList<>();
        if (members == null) members = new ArrayList<>();

        // Dummy data for first run
        if (books.isEmpty() && members.isEmpty()) {
            books.add(new Book(nextBookSNo++, "Java Reference", "H. Schildt", 5));
            members.add(new Member(nextMemberId++, "Test Student", "9999999999"));
        }
    }

    public void addBook(String title, String author, int qty) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title) && book.getAuthor().equalsIgnoreCase(author)) {
                book.increaseQuantity(qty);
                System.out.println("Book updated.");
                saveData();
                return;
            }
        }
        books.add(new Book(nextBookSNo++, title, author, qty));
        System.out.println("Book added. S.No: " + (nextBookSNo - 1));
        saveData();
    }

    public List<Book> getAllBooks() { return books; }

    public List<Book> searchBooks(String keyword) {
        List<Book> results = new ArrayList<>();
        String search = keyword.toLowerCase();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(search) || 
                book.getAuthor().toLowerCase().contains(search) || 
                String.valueOf(book.getSNo()).contains(search)) {
                results.add(book);
            }
        }
        return results;
    }

    public void addMember(String name, String phone) {
        members.add(new Member(nextMemberId++, name, phone));
        System.out.println("Member registered. ID: " + (nextMemberId - 1));
        saveData();
    }

    public List<Member> getAllMembers() { return members; }

    public String issueBook(int sNo, int memberId) {
        Book book = books.stream().filter(b -> b.getSNo() == sNo).findFirst().orElse(null);
        Member member = members.stream().filter(m -> m.getMemberId() == memberId).findFirst().orElse(null);

        if (book == null) return "Error: Book not found.";
        if (member == null) return "Error: Member not found.";
        if (book.getAvailableQty() <= 0) return "Error: Out of stock.";
        if (member.getBorrowedCount() >= 5) return "Error: Borrow limit reached.";

        book.issueBook();
        member.incrementBorrowed();
        saveData();
        return "Success: Issued '" + book.getTitle() + "' to " + member.getName();
    }

    public String returnBook(int sNo, int memberId) {
        Book book = books.stream().filter(b -> b.getSNo() == sNo).findFirst().orElse(null);
        Member member = members.stream().filter(m -> m.getMemberId() == memberId).findFirst().orElse(null);

        if (book == null) return "Error: Book not found.";
        if (member == null) return "Error: Member not found.";
        
        // Validation fix: Ensure user actually has this book (simplified logic)
        if (member.getBorrowedCount() <= 0) return "Error: This member has no books to return.";

        book.returnBook();
        member.decrementBorrowed();
        saveData();
        return "Success: Returned '" + book.getTitle() + "'";
    }

    public void shutdown() {
        saveData();
    }
}