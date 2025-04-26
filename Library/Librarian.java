import java.util.ArrayList;

public class Librarian extends User {
    public Librarian(String name, String userID) {
        super(name, userID);
    }

    public void addBook(ArrayList<Book> books, Book newBook) {
        books.add(newBook);
    }

    public void removeBook(ArrayList<Book> books, Book bookToRemove) throws Exception {
        if (!books.contains(bookToRemove)) {
            throw new Exception("Book not found in library");
        }
        if (!bookToRemove.isAvailable()) {
            throw new Exception("Cannot remove a book that is borrowed");
        }
        books.remove(bookToRemove);
    }

    @Override
    public void displayInfo() {
        System.out.println("Librarian Name: " + getName() + ", ID: " + getUserID());
    }
}