import java.util.ArrayList;

public class Member extends User {
    private ArrayList<Book> borrowedBooks;

    public Member(String name, String userID) {
        super(name, userID);
        this.borrowedBooks = new ArrayList<>();
    }

    public void borrowBook(Book book) throws Exception {
        if (!book.isAvailable()) {
            throw new Exception("Book is not available for borrowing");
        }
        borrowedBooks.add(book); // Add book to borrowed list
        book.setAvailable(false); // Mark book as borrowed
    }

    public void returnBook(Book book) throws Exception {
        if (!borrowedBooks.contains(book)) {
            throw new Exception("This book was not borrowed by this member");
        }
        borrowedBooks.remove(book); // Remove book from borrowed list
        book.setAvailable(true); // Mark book as available
    }

    public ArrayList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }
    // Override the displayInfo method to show member details
    @Override 
    public void displayInfo() {
        System.out.println("Member Name: " + getName() + ", ID: " + getUserID());
        System.out.println("Borrowed Books: " + borrowedBooks.size());
    }
}
