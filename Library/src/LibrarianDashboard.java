package org.example;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibrarianDashboard {

    private LibrarySystem librarySystem;
    private Librarian librarian;

    public LibrarianDashboard(LibrarySystem librarySystem, Librarian librarian) {
        this.librarySystem = librarySystem;
        this.librarian = librarian;
    }

    public void showLibrarianDashboard(Stage primaryStage) {
        // رسالة ترحيبية مخصصة
        Label welcomeLabel = new Label("📚 Welcome, " + librarian.getName() + "!");
        welcomeLabel.setFont(Font.font("Segoe UI", 24));
        welcomeLabel.setTextFill(Color.WHITE);
        // أزرار الوظائف المختلفة
        Button addBookBtn = createStyledButton("➕ Add a Book", "#27ae60", "#229954");
        Button removeBookBtn = createStyledButton("❌ Remove a Book", "#c0392b", "#a93226");
        Button viewBooksBtn = createStyledButton("📖 View All Books", "#2980b9", "#2471a3");
        Button viewMembersBtn = createStyledButton("📋 View All Members", "#8e44ad", "#7d3c98");
        Button logoutBtn = createStyledButton("🔓 Logout", "#e67e22", "#d35400");

        // ترتيب العناصر داخل VBox
        VBox vbox = new VBox(20, welcomeLabel, addBookBtn, removeBookBtn, viewBooksBtn, viewMembersBtn, logoutBtn);
        vbox.setPadding(new Insets(30));
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: linear-gradient(to bottom right, #2e4053, #34495e);");
        vbox.setEffect(new DropShadow(10, Color.BLACK));
        // إعداد المشهد
        Scene scene = new Scene(vbox, 550, 420);
        primaryStage.setTitle("Librarian Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
        // هنا بنربط كل زرار بالوظيفة اللي المفروض ينفذها
        addBookBtn.setOnAction(e -> addBook());
        removeBookBtn.setOnAction(e -> removeBook());
        viewBooksBtn.setOnAction(e -> viewAllBooks());
        viewMembersBtn.setOnAction(e -> viewAllMembers());
        // الزرار الخاص بتسجيل الخروج بيرجعنا للشاشة الرئيسية
        logoutBtn.setOnAction(e -> {
            primaryStage.close();  // بيقفل الشاشة الحالية
            Main loginForm = new Main();// بننشئ كائن من Main علشان نرجع لشاشة تسجيل الدخول
            loginForm.start(new Stage());   // تشغيل الشاشة الجديدة
        });


    }
    // ميثود خاصة بتصميم الزرائر مع تأثير hover
    private Button createStyledButton(String text, String color, String hoverColor) {
        Button button = new Button(text);
        button.setPrefWidth(250);
        button.setPrefHeight(45);
        // تصميم الشكل الأساسي للزرار
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 20;");
        // تغيير الشكل لما الماوس ييجي عليه
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + hoverColor + "; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 20;"));
        // يرجع للشكل الأصلي لما الماوس يبعد
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 20;"));
        return button;
    }

    // دي الميثود اللي بتفتح فورم لإضافة كتاب جديد
    private void addBook() {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("➕ Add Book");

        // إنشاء الليبلز لكل حقل
        Label titleLabel = new Label("Title:");
        Label authorLabel = new Label("Author:");
        Label idLabel = new Label("Book ID:");
        titleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #2c3e50;");
        authorLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #2c3e50;");
        idLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #2c3e50;");

        // إنشاء الحقول النصية لإدخال البيانات
        TextField titleField = new TextField();
        TextField authorField = new TextField();
        TextField idField = new TextField();
        // ستايل موحد للحقول
        String inputStyle = "-fx-background-color: #ffffff; -fx-border-color: #bdc3c7; -fx-border-radius: 8px; -fx-padding: 6px;";
        titleField.setStyle(inputStyle);
        authorField.setStyle(inputStyle);
        idField.setStyle(inputStyle);
// تجميع العناصر داخل VBox
        VBox vbox = new VBox(12, titleLabel, titleField, authorLabel, authorField, idLabel, idField);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #ecf0f1;");
// وضع الـVBox داخل الدايالوج
        dialog.getDialogPane().setContent(vbox);
        // زرار الإضافة
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
        // تخصيص ستايل الزرار
        Button addBtn = (Button) dialog.getDialogPane().lookupButton(addButtonType);
        addBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10px;");
        addBtn.setOnMouseEntered(e -> addBtn.setStyle("-fx-background-color: #1e8449; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10px;"));
        addBtn.setOnMouseExited(e -> addBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10px;"));
// زرار الإلغاء
        Button cancelBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10px;");
        cancelBtn.setOnMouseEntered(e -> cancelBtn.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10px;"));
        cancelBtn.setOnMouseExited(e -> cancelBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10px;"));
// لما المستخدم يضغط Add، بنرجع كائن كتاب جديد
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Book(titleField.getText(), authorField.getText(), idField.getText());
            }
            return null;
        });
// لما المستخدم يضغط OK، بنضيف الكتاب للنظام وقاعدة البيانات
        dialog.showAndWait().ifPresent(book -> {
            librarian.addBook(librarySystem.getBooks(), book);
            updateBookInDatabase(book, "INSERT");
            showAlert(Alert.AlertType.INFORMATION, "Success", "✅ Book added successfully!");
        });
    }

// الميثود الخاصة بحذف كتاب باستخدام الـ ID
    private void removeBook() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("❌ Remove Book");
        // الليبل وحقل الإدخال
        Label label = new Label("Book ID:");
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: #2c3e50;");

        TextField idField = new TextField();
        idField.setPromptText("Enter book ID");
        idField.setStyle("-fx-background-color: #ffffff; -fx-border-color: #bdc3c7; -fx-border-radius: 8px; -fx-padding: 6px;");

        VBox vbox = new VBox(12, label, idField);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #ecf0f1;");

        dialog.getDialogPane().setContent(vbox);
        // زرار الحذف والإلغاء
        ButtonType removeButtonType = new ButtonType("Remove", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(removeButtonType, ButtonType.CANCEL);

        Button removeBtn = (Button) dialog.getDialogPane().lookupButton(removeButtonType);
        removeBtn.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10px;");
        removeBtn.setOnMouseEntered(e -> removeBtn.setStyle("-fx-background-color: #922b21; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10px;"));
        removeBtn.setOnMouseExited(e -> removeBtn.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10px;"));

        Button cancelBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelBtn.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10px;");
        cancelBtn.setOnMouseEntered(e -> cancelBtn.setStyle("-fx-background-color: #707b7c; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10px;"));
        cancelBtn.setOnMouseExited(e -> cancelBtn.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10px;"));

        dialog.setResultConverter(dialogButton -> {
  // هنا بنحدد لو المستخدم ضغط على زرار "Remove"، نرجّع الـ Book ID اللي كتبه

            if (dialogButton == removeButtonType) {
                return idField.getText();// بيرجع الـ ID اللي المستخدم كتبه علشان نستخدمه لاحقًا
            }
            return null;// لو المستخدم قفل الديالوج أو ضغط Cancel، مش بنرجّع حاجة
        });

        dialog.showAndWait().ifPresent(bookId -> {
            // بعد ما المستخدم يضغط "Remove" ويتأكد من الحذف
            boolean deleted = deleteBookById(bookId); // بنستدعي دالة بتحاول تحذف الكتاب بالـ ID ده من قاعدة البيانات
            if (deleted) {
                // لو الحذف تم بنجاح، نعرض رسالة نجاح
                showAlert(Alert.AlertType.INFORMATION, "Success", "✅ Book removed successfully!");
            } else {
                // لو معرفناش نحذف (مثلاً الـ ID غلط)، نعرض رسالة خطأ
                showAlert(Alert.AlertType.ERROR, "Error", "❌ Failed to remove book. Please check the ID.");
            }
        });
    }

    private boolean deleteBookById(String bookId) {
        try (
                // هنا بنفتح اتصال بقاعدة البيانات Oracle
                Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "libs", "12345");
                // بنجهز جملة SQL لحذف الكتاب اللي الـ ID بتاعه مساوي للـ bookId اللي المستخدم كتبه
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM BOOKS WHERE ID = ?")) {
            // بنحدد قيمة الـ ID في الاستعلام
            stmt.setString(1, bookId);
            // بننفذ عملية الحذف وبنخزن عدد الصفوف اللي اتأثرت (يعني عدد الكتب اللي اتحذفت)
            int rowsAffected = stmt.executeUpdate();
            // لو عدد الصفوف أكتر من صفر، يبقى تم الحذف بنجاح
            return rowsAffected > 0;
        } catch (SQLException e) {
            // لو حصل خطأ أثناء الاتصال أو التنفيذ، بنعرض رسالة خطأ للمستخدم
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
            return false;
        }
    }



    private void viewAllBooks() {
        // بننشئ جدول لعرض الكتب
        TableView<Book> tableView = new TableView<>();
        // بنضيف أعمدة الجدول: ID - Title - Author - Status (متاح أو مستعار)
        tableView.getColumns().addAll(
                createColumn("ID", b -> b.getId()),
                createColumn("Title", b -> b.getTitle()),
                createColumn("Author", b -> b.getAuthor()),
                createColumn("Status", b -> b.isAvailable() ? "✅ Available" : "❌ Borrowed")
        );

        // بنجيب الكتب من قاعدة البيانات
        List<Book> books = fetchBooksFromDatabase();

        // بنرتب الكتب باستخدام خوارزمية Bubble Sort حسب الـ ID
        bubbleSortBooksById(books);

        // بنعرض الكتب المرتبة في جدول العرض
        tableView.getItems().setAll(books);

        // إنشاء مربع نص للبحث عن كتاب حسب الـ ID
        TextField searchField = new TextField();
        searchField.setPromptText("Enter Book ID to search...");
        searchField.setStyle("-fx-background-color: #ffffff; -fx-border-color: #bdc3c7; -fx-border-radius: 8px; -fx-padding: 6px;");
        // زر البحث
        Button searchBtn = createStyledButton("🔍 Search", "#2980b9", "#2471a3");
        searchBtn.setOnAction(e -> {
            String bookId = searchField.getText().trim(); // ناخد قيمة الـ ID اللي المستخدم كتبه
            if (!bookId.isEmpty()) {
                // نعمل بحث خطي في الليست
                Book foundBook = linearSearchBookById(books, bookId);
                if (foundBook != null) {
                    // لو لقينا الكتاب نعرض رسالة نجاح
                    showAlert(Alert.AlertType.INFORMATION, "Book Found", "✅ Book found: " + foundBook.getTitle());
                } else {
                    // لو مش موجود، نعرض رسالة تحذير
                    showAlert(Alert.AlertType.WARNING, "Book Not Found", "❌ Book with ID: " + bookId + " not found.");
                }
            }
        });
        // زر الرجوع
        Button closeBtn = createStyledButton("🔙 Back", "#7f8c8d", "#707b7c");
        closeBtn.setOnAction(e -> ((Stage) closeBtn.getScene().getWindow()).close());

        // زر التحديث: يعيد جلب البيانات من قاعدة البيانات ويحدث الجدول
        Button refreshBtn = createStyledButton("🔄 Refresh", "#16a085", "#138d75");
        refreshBtn.setOnAction(e -> {
            books.clear();// نمسح الكتب القديمة
            books.addAll(fetchBooksFromDatabase()); // نجيبها من جديد
            bubbleSortBooksById(books);// نرتبها
            tableView.getItems().setAll(books);// نعرضها تاني
        });
        // بنحط كل حاجة في VBox ونعرضها في نافذة جديدة
        VBox vbox = new VBox(15, new Label("📚 All Books"), searchField, searchBtn, tableView, refreshBtn, closeBtn);
        vbox.setPadding(new Insets(25));
        vbox.setAlignment(Pos.CENTER);

        Stage stage = new Stage();
        stage.setTitle("All Books");
        stage.setScene(new Scene(vbox, 700, 500));
        stage.show();
    }



    private void viewAllMembers() {
        // بننشئ جدول لعرض الأعضاء
        TableView<Member> tableView = new TableView<>();

        // بنضيف أعمدة: ID و Name
        tableView.getColumns().addAll(
                createColumn("ID", m -> m.getUserID()),
                createColumn("Name", m -> m.getName())
        );

        // نجيب الأعضاء من قاعدة البيانات ونعرضهم
        tableView.getItems().setAll(fetchMembersFromDatabase());

        // زر الرجوع
        Button closeBtn = createStyledButton("🔙 Back", "#7f8c8d", "#707b7c");
        closeBtn.setOnAction(e -> ((Stage) closeBtn.getScene().getWindow()).close());

        VBox vbox = new VBox(15, new Label("👥 All Members"), tableView, closeBtn);
        vbox.setPadding(new Insets(25));
        vbox.setAlignment(Pos.CENTER);

        Stage stage = new Stage();
        stage.setTitle("All Members");
        stage.setScene(new Scene(vbox, 700, 500));
        stage.show();
    }


    private List<Member> fetchMembersFromDatabase() {
        List<Member> members = new ArrayList<>();
        try (
                // اتصال بقاعدة البيانات
                Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "libs", "12345");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT ID, NAME FROM MEMBER_LOGINS")
        ) {
            // نمر على كل صف في النتيجة ونضيف العضو إلى الليست
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");

                // بنحوّل الـ ID لسترينج علشان يتماشى مع الـ Member class
                members.add(new Member(String.valueOf(id), name));
            }
        } catch (SQLException e) {
            // في حالة الخطأ، نعرض رسالة للمستخدم
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }
        return members;
    }


    private <T> TableColumn<T, String> createColumn(String title, java.util.function.Function<T, String> mapper) {
        // بنعمل عمود جديد بعنوان معين
        TableColumn<T, String> column = new TableColumn<>(title);

        // نحدد إزاي نجيب البيانات لكل صف في العمود باستخدام الـ mapper
        column.setCellValueFactory(cellData -> new SimpleStringProperty(mapper.apply(cellData.getValue())));

        // نحدد شوية تنسيقات للعمود
        column.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER-LEFT; -fx-font-family: 'Segoe UI';");

        return column;
    }


    private boolean updateBookInDatabase(Book book, String action) {
        try (
                // بنفتح اتصال بقاعدة البيانات
                Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "libs", "12345")
        ) {
            PreparedStatement stmt;

            if ("INSERT".equalsIgnoreCase(action)) {
                // لو الإجراء هو "إضافة" كتاب
                stmt = conn.prepareStatement("INSERT INTO BOOKS (ID, TITLE, AUTHOR, STATUS) VALUES (?, ?, ?, ?)");
                stmt.setString(1, book.getId()); // ID بتاع الكتاب
                stmt.setString(2, book.getTitle()); // العنوان
                stmt.setString(3, book.getAuthor()); // المؤلف
                stmt.setString(4, book.isAvailable() ? "Available" : "Borrowed"); // الحالة

            } else if ("DELETE".equalsIgnoreCase(action)) {
                // لو الإجراء هو "مسح" كتاب
                stmt = conn.prepareStatement("DELETE FROM BOOKS WHERE ID = ?");
                stmt.setString(1, book.getId()); // ID الكتاب اللي عايزين نمسحه

            } else {
                return false; // لو الإجراء مش معروف، بنرجّع false
            }

            stmt.executeUpdate(); // تنفيذ الأمر
            return true; // لو مفيش أخطاء، العملية نجحت
        } catch (SQLException e) {
            // لو حصل خطأ، نعرض رسالة للمستخدم
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
            return false;
        }
    }


    private List<Book> fetchBooksFromDatabase() {
        List<Book> books = new ArrayList<>(); // ليست هتحتوي على الكتب

        try (
                // الاتصال بقاعدة البيانات
                Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "libs", "12345");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT ID, TITLE, AUTHOR, STATUS FROM BOOKS")
        ) {
            while (rs.next()) {
                // بنقرأ البيانات من كل صف
                String id = rs.getString("ID");
                String title = rs.getString("TITLE");
                String author = rs.getString("AUTHOR");
                String status = rs.getString("STATUS");

                // تحويل الحالة إلى boolean (متاح أو لا)
                boolean isAvailable = "Available".equalsIgnoreCase(status);

                // إنشاء كائن Book جديد وإضافته للليست
                books.add(new Book(title, author, id, isAvailable));
            }
        } catch (SQLException e) {
            // عرض رسالة خطأ لو حصلت مشكلة
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }

        return books; // رجعنا الليست اللي فيها كل الكتب
    }

    public void bubbleSortBooksById(List<Book> books) {
        int n = books.size();

        // بنمشي على الليست مرتين علشان نبدل أماكن الكتب حسب الترتيب
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // بنقارن بين ID بتاع الكتاب الحالي واللي بعده
                if (books.get(j).getId().compareTo(books.get(j + 1).getId()) > 0) {
                    // لو الترتيب غلط، نبدلهم
                    Book temp = books.get(j);
                    books.set(j, books.get(j + 1));
                    books.set(j + 1, temp);
                }
            }
        }
    }


    public Book linearSearchBookById(List<Book> books, String bookId) {
        for (Book book : books) {
            if (book.getId().equals(bookId)) {
                return book; // لو لقينا الكتاب، نرجعه
            }
        }
        return null; // لو ملقيناش حاجة، نرجع null
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
