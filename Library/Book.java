public class Book {
    private String title;
    private String author;
    private String bookID;
    private boolean isAvailable;

    public Book(String title, String author, String bookID) {
        this.title = title;
        this.author = author;
        this.bookID = bookID;
        this.isAvailable = true;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getBookID() {
        return bookID;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Author: " + author + ", ID: " + bookID + 
               ", Available: " + (isAvailable ? "Yes" : "No");
    }
}
