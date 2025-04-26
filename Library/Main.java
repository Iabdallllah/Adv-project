import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class Main extends JFrame {
    private LibrarySystem librarySystem;
    private JTextArea outputArea;

    public Main() {
        librarySystem = new LibrarySystem();
        initializeSampleData();

        setTitle("📚 Library Management System");
        setSize(900, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // Colors and Fonts
        Color background = new Color(30, 33, 40);
        Color foreground = new Color(230, 230, 230);
        Color buttonColor = new Color(0, 123, 255);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 28);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);
        Font outputFont = new Font("Consolas", Font.PLAIN, 14);

        getContentPane().setBackground(background);

        // Title
        JLabel titleLabel = new JLabel("📚 Library Management System", JLabel.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(foreground);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Output Area
        outputArea = new JTextArea();
        outputArea.setFont(outputFont);
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(40, 44, 52));
        outputArea.setForeground(foreground);
        outputArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180)));
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 12, 12));
        buttonPanel.setBackground(background);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        String[] buttonLabels = {
            "List All Books", "Search Book", "Sort Books by Title",
            "Add Member", "Borrow Book", "Return Book"
        };
        JButton[] buttons = new JButton[buttonLabels.length];

        for (int i = 0; i < buttonLabels.length; i++) {
            buttons[i] = new JButton(buttonLabels[i]);
            buttons[i].setBackground(buttonColor);
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setFocusPainted(false);
            buttons[i].setFont(buttonFont);
            buttons[i].setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            buttonPanel.add(buttons[i]);
        }

        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        buttons[0].addActionListener(e -> listAllBooks());
        buttons[1].addActionListener(e -> searchBook());
        buttons[2].addActionListener(e -> {
            librarySystem.sortBooksByTitle();
            outputArea.setText("✅ Books sorted by title.\n\n");
            listAllBooks();
        });
        buttons[3].addActionListener(e -> addMember());
        buttons[4].addActionListener(e -> borrowBook());
        buttons[5].addActionListener(e -> returnBook());
    }

    private void initializeSampleData() {
        librarySystem.addBook(new Book("Atomic Habits", "James Clear", "B001"));
        librarySystem.addBook(new Book("Rich Dad Poor Dad", "Robert Kiyosaki", "B002"));
        librarySystem.addBook(new Book("The 7 Habits of Highly Effective Teens", "Sean Covey", "B003"));
    }

    private void listAllBooks() {
        outputArea.setText("📚 All Books in Library:\n\n");
        for (Book book : librarySystem.getBooks()) {
            outputArea.append(book + "\n");
        }
    }

    private void searchBook() {
        String title = JOptionPane.showInputDialog(this, "Enter book title to search:");
        if (title != null && !title.isEmpty()) {
            Book foundBook = librarySystem.searchBook(title);
            if (foundBook != null) {
                outputArea.setText("🔍 Book found:\n\n" + foundBook);
            } else {
                outputArea.setText("❌ Book not found with title: " + title);
            }
        }
    }

    private void addMember() {
        String name = JOptionPane.showInputDialog(this, "Enter member name:");
        String id = JOptionPane.showInputDialog(this, "Enter member ID:");

        if (name != null && !name.isEmpty() && id != null && !id.isEmpty()) {
            Member newMember = new Member(name, id);
            librarySystem.addMember(newMember);
            outputArea.setText("✅ New member added:\nName: " + name + "\nID: " + id);
        }
    }

    private void borrowBook() {
        if (librarySystem.getMembers().isEmpty()) {
            outputArea.setText("⚠️ No members available. Please add a member first.");
            return;
        }

        Member member = chooseMember("Select member to borrow:");
        if (member == null) return;

        ArrayList<Book> availableBooks = new ArrayList<>();
        for (Book book : librarySystem.getBooks()) {
            if (book.isAvailable()) availableBooks.add(book);
        }

        if (availableBooks.isEmpty()) {
            outputArea.setText("⚠️ No books available for borrowing.");
            return;
        }

        Book bookToBorrow = chooseBook(availableBooks, "Select book to borrow:");
        if (bookToBorrow == null) return;

        try {
            member.borrowBook(bookToBorrow);
            outputArea.setText("📖 Book borrowed successfully:\n" +
                    "Member: " + member.getName() + "\nBook: " + bookToBorrow.getTitle());
        } catch (Exception e) {
            outputArea.setText("❌ Error: " + e.getMessage());
        }
    }

    private void returnBook() {
        if (librarySystem.getMembers().isEmpty()) {
            outputArea.setText("⚠️ No members available.");
            return;
        }

        Member member = chooseMember("Select member to return a book:");
        if (member == null || member.getBorrowedBooks().isEmpty()) {
            outputArea.setText("⚠️ This member has no borrowed books.");
            return;
        }

        Book bookToReturn = chooseBook(member.getBorrowedBooks(), "Select book to return:");
        if (bookToReturn == null) return;

        try {
            member.returnBook(bookToReturn);
            outputArea.setText("✅ Book returned successfully:\n" +
                    "Member: " + member.getName() + "\nBook: " + bookToReturn.getTitle());
        } catch (Exception e) {
            outputArea.setText("❌ Error: " + e.getMessage());
        }
    }

    private Member chooseMember(String message) {
        ArrayList<Member> members = librarySystem.getMembers();
        String[] options = members.stream()
                .map(m -> m.getName() + " (" + m.getUserID() + ")")
                .toArray(String[]::new);

        String selected = (String) JOptionPane.showInputDialog(this, message, "Members",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (selected == null) return null;

        for (Member m : members) {
            if (selected.contains(m.getUserID())) return m;
        }
        return null;
    }

    private Book chooseBook(ArrayList<Book> books, String message) {
        String[] options = books.stream()
                .map(b -> b.getTitle() + " by " + b.getAuthor())
                .toArray(String[]::new);

        String selected = (String) JOptionPane.showInputDialog(this, message, "Books",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (selected == null) return null;

        for (Book b : books) {
            if (selected.contains(b.getTitle())) return b;
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
