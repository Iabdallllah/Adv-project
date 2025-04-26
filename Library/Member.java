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
        borrowedBooks.add(book);
        book.setAvailable(false);
    }

    public void returnBook(Book book) throws Exception {
        if (!borrowedBooks.contains(book)) {
            throw new Exception("This book was not borrowed by this member");
        }
        borrowedBooks.remove(book);
        book.setAvailable(true);
    }

    public ArrayList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    @Override
    public void displayInfo() {
        System.out.println("Member Name: " + getName() + ", ID: " + getUserID());
        System.out.println("Borrowed Books: " + borrowedBooks.size());
    }
}