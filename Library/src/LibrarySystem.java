import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LibrarySystem {
    private ArrayList<Book> books; 
    private ArrayList<Member> members;

    public LibrarySystem() {
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) throws Exception {
        if (!books.contains(book)) {
            throw new Exception("Book not found in library");
        }
        if (!book.isAvailable()) {
            throw new Exception("Cannot remove a book that is currently borrowed");
        }
        books.remove(book); // Remove book from the library
    }

    public void addMember(Member member) { 
        members.add(member);
    }


    public ArrayList<Book> getBooks() {
        return books;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }
}
