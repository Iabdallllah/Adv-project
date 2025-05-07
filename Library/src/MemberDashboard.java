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

public class MemberDashboard {
//هنا عرفنا اتنين متغيرات، واحد لـ LibrarySystem والتاني لـ Member اللي هو العضو الحالي اللي عامل تسجيل دخول.
    private LibrarySystem librarySystem;
    private Member member;
//ده الكونستركتور (constructor) بتاع الكلاس. أول ما نعمل object من MemberDashboard، بنمررله السيستم والعضو علشان يشتغل عليهم.
    public MemberDashboard(LibrarySystem librarySystem, Member member) {
        this.librarySystem = librarySystem;
        this.member = member;
    }
//دي الميثود اللي بتعرض الواجهة بتاعة العضو. بناخد الـ Stage (الشاشة الأساسية) ونعمل عليه كل العناصر الرسومية.
    public void showMemberDashboard(Stage primaryStage) {
        //بنعرض رسالة ترحيب فيها اسم العضو اللي دخل.
        Label welcomeLabel = new Label("📖 Welcome, " + member.getName() + "!");
        //بنحدد الخط وحجمه، وبنخلي لون الكلام أبيض.
        welcomeLabel.setFont(Font.font("Segoe UI", 24));
        welcomeLabel.setTextFill(Color.WHITE);

//بننشئ خمس أزرار، كل زر ليه label وألوان للزر العادي ولما الماوس ييجي عليه.
        Button borrowBookBtn = createStyledButton("📚 Borrow a Book", "#3498db", "#2980b9");
        Button returnBookBtn = createStyledButton("🔄 Return a Book", "#e67e22", "#d35400");
        Button viewBooksBtn = createStyledButton("📋 View All Books", "#9b59b6", "#8e44ad");
        Button myBooksBtn = createStyledButton("📕 My Borrowed Books", "#1abc9c", "#16a085");
        Button logoutBtn = createStyledButton("🔓 Logout", "#e67e22", "#d35400");

//بنحط كل العناصر دي في VBox (يعني ترتيب عمودي) وبنحدد المسافة ما بينهم بـ 20 بيكسل.
        VBox vbox = new VBox(20, welcomeLabel, borrowBookBtn, returnBookBtn, viewBooksBtn, myBooksBtn,logoutBtn);
//بنحط padding حوالي العناصر، وبنخلي كل حاجة في النص
        vbox.setPadding(new Insets(30));
        vbox.setAlignment(Pos.CENTER);
//بنحط خلفية متدرجة على VBox وظل خارجي خفيف.
        vbox.setStyle("-fx-background-color: linear-gradient(to bottom right, #2c3e50, #34495e);");
        vbox.setEffect(new DropShadow(10, Color.BLACK));
//بنجهز الـ Scene (المشهد)، ونعرضها في الـ Stage (الشاشة) ونحدد عنوان النافذة.
        Scene scene = new Scene(vbox, 600, 450);
        primaryStage.setTitle("Member Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
//بنربط كل زر بالميثود المناسبة اللي بتتنفذ لما المستخدم يضغط عليه.
        borrowBookBtn.setOnAction(e -> borrowBook());
        returnBookBtn.setOnAction(e -> returnBook());
        viewBooksBtn.setOnAction(e -> viewAllBooks());
        myBooksBtn.setOnAction(e -> viewMyBorrowedBooks());
//الزر بتاع تسجيل الخروج بيقفل الشاشة دي، وبيرجعك لشاشة تسجيل الدخول.
        logoutBtn.setOnAction(e -> {
            primaryStage.close();  // إغلاق النافذة الحالية
            Main loginForm = new Main(); // إنشاء كائن من Main
            loginForm.start(new Stage());  // تشغيل Main في نافذة جديدة
        });
    }
//ميثود بتاخد الكلام اللي يظهر على الزر، ولونه العادي ولون الـ hover، وبتصمم الزر بشكل موحد.
    private Button createStyledButton(String text, String color, String hoverColor) {
        //بنحدد أبعاد الزر.
        Button button = new Button(text);
        button.setPrefWidth(250);
        button.setPrefHeight(45);
        //بنحط الشكل الأساسي للزر، لونه ونصه وحجمه وحواف دائرية.
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 20;");
        //بنغير اللون لما الماوس ييجي أو يخرج من الزر.
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + hoverColor + "; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 20;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 20;"));
        return button;
    }
//هنا بنجهز نافذة حوار (Dialog) علشان المستخدم يدخل ID الكتاب اللي عايز يستعيره
    private void borrowBook() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Borrow Book");
//بنحط label بسيط يقول للمستخدم يدخل ID الكتاب
        Label label = new Label("Book ID:");
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");
//ده المكان اللي المستخدم هيكتب فيه رقم الكتاب اللي عايز يستعيره
        TextField textField = new TextField();
        textField.setPromptText("Enter book ID");
        textField.setStyle("-fx-background-color: #ffffff; -fx-border-color: #bdc3c7; -fx-border-radius: 8px; -fx-padding: 6px;");
//بنجمع الـ label و textfield في VBox، وبنظبط شكله
        VBox vbox = new VBox(12, label, textField);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #ecf0f1;");
//بنعرض الـ VBox جوه نافذة الحوار.
        dialog.getDialogPane().setContent(vbox);
//بنضيف زرين: واحد علشان يأكد، والتاني للإلغاء.
        ButtonType okButtonType = new ButtonType("Borrow", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        Button okBtn = (Button) dialog.getDialogPane().lookupButton(okButtonType);
//استايل الزر الأساسي
        okBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10px;");
// لما الماوس يقف عليه
        okBtn.setOnMouseEntered(e -> okBtn.setStyle("-fx-background-color: #d35400; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10px;"));
// لما الماوس يخرج منه
        okBtn.setOnMouseExited(e -> okBtn.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10px;"));
// بنجيب زرار الـ Cancel من الـ Dialog علشان نقدر نتحكم في شكله
        Button cancelBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
// بنحدد لون الزر الأساسي (لون أحمر) وشكل الخط وخلافه
        cancelBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10px;");
// لما الماوس يدخل على الزر، نغير لونه للغامق علشان يدي تأثير Hover
        cancelBtn.setOnMouseEntered(e -> cancelBtn.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10px;"));
// لما الماوس يخرج، نرجعه للونه الأساسي
        cancelBtn.setOnMouseExited(e -> cancelBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10px;"));
// هنا بنحدد إيه اللي هيحصل لما المستخدم يضغط على زرار في الـ Dialog
        dialog.setResultConverter(dialogButton -> {
// لو المستخدم ضغط على زرار الـ OK (اللي هو في حالة borrow بيكون "Borrow")
            if (dialogButton == okButtonType) {
// نرجع النص اللي كتبه المستخدم في TextField (يعني ID بتاع الكتاب)
                return textField.getText();
            }
// لو ضغط Cancel أو قفل، نرجّع null (يعني مفيش حاجة)
            return null;
        });
// بعد ما الـ Dialog يظهر ويستنى المستخدم، بنشوف هو كتب ID ولا لأ
        dialog.showAndWait().ifPresent(bookId -> {
// بندور على الكتاب باستخدام الـ ID اللي دخل المستخدم
            Book book = fetchBookById(bookId);
// لو الكتاب مش موجود، نظهر رسالة خطأ
            if (book == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "❌ Book not found.");
            } else {
                try {
// بنغيّر حالة الكتاب في قاعدة البيانات إنها "Borrowed"
                    updateBookStatusInDatabase(bookId, "Borrowed");
// بنسجل في جدول "الكتب المستعارة" إن العضو استعار الكتاب ده
                    insertBorrowedRecord(bookId, member.getUserID()
                    );
// بنضيف الكتاب لقائمة الكتب اللي العضو استعارها
                    member.borrowBook(book);
// نظهر رسالة إن الكتاب استُعير بنجاح
                    showAlert(Alert.AlertType.INFORMATION, "Success", "✅ Book borrowed successfully!");
// نحدّث جدول الكتب المستعارة علشان يبان التغيير
                    refreshBorrowedBooks();
                } catch (Exception ex) {
// لو حصل أي خطأ في العملية، نظهر رسالة الخطأ
                    showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
                }
            }
        });
    }
 // دي الدالة اللي بتتعامل مع عملية إرجاع الكتاب
    private void returnBook() {
 // بنعمل Dialog جديد علشان نطلب من المستخدم يدخل ID الكتاب اللي عايز يرجعه
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Return Book");// عنوان الـ Dialog
// بنعمل لابل علشان نكتب للمستخدم إن يدخل Book ID
        Label label = new Label("Book ID:");
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");
// بنجهز TextField المستخدم هيكتب فيه الـ ID
        TextField textField = new TextField();
        textField.setPromptText("Enter book ID");
        textField.setStyle("-fx-background-color: #ffffff; -fx-border-color: #bdc3c7; -fx-border-radius: 8px; -fx-padding: 6px;");
// بنرتب اللابل والتكست في VBox
        VBox vbox = new VBox(12, label, textField);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #ecf0f1;");
// بنضيفهم للDialog
        dialog.getDialogPane().setContent(vbox);
// بنجهز زرار الإرجاع (OK) وزرار الإلغاء
        ButtonType okButtonType = new ButtonType("Return", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
// بنجيب زرار الإرجاع علشان ننسق شكله
        Button okBtn = (Button) dialog.getDialogPane().lookupButton(okButtonType);
        okBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10px;");
        okBtn.setOnMouseEntered(e -> okBtn.setStyle("-fx-background-color: #d35400; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10px;"));
        okBtn.setOnMouseExited(e -> okBtn.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10px;"));
// نفس الفكرة لزرار الإلغاء
        Button cancelBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10px;");
        cancelBtn.setOnMouseEntered(e -> cancelBtn.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10px;"));
        cancelBtn.setOnMouseExited(e -> cancelBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10px;"));
// بنحدد النتيجة اللي هترجع من الـ Dialog
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return textField.getText();// لو المستخدم ضغط "Return"، نرجع الـ ID
            }
            return null;// لو Cancel أو قفل الديالوج
        });
// لما المستخدم يضغط OK والديالوج يتقفل
        dialog.showAndWait().ifPresent(bookId -> {
            // بندور على الكتاب باستخدام الـ ID
            Book book = fetchBookById(bookId);
            // لو الكتاب مش موجود
            if (book == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "❌ Book not found.");
            } else {
                try {
// بنتأكد إن الكتاب مستعار فعلًا من العضو ده
                    if (isBookBorrowedByMember(bookId, member.getUserID())) {
// نرجّع حالة الكتاب لـ Available في قاعدة البيانات
                        updateBookStatusInDatabase(bookId, "Available");
// نحذف السطر اللي بيوضح إن العضو مستعير الكتاب ده
                        deleteBorrowedRecord(bookId, member.getUserID());
// بنشيل الكتاب من قائمة الكتب المستعارة للعضو
                        member.returnBook(book);

// نظهر رسالة نجاح
showAlert(Alert.AlertType.INFORMATION, "Success", "✅ Book returned successfully!");

// نحدّث جدول الكتب المستعارة علشان يتحدث في الواجهة
                        refreshBorrowedBooks();
                    } else {
// لو الكتاب مش مستعار من العضو ده، نعرض تحذير
                        showAlert(Alert.AlertType.WARNING, "Warning", "⚠️ This book is not borrowed by you.");
                    }

                } catch (Exception ex) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "✅ Book returned successfully!");
                }
            }
        });
    }

// دي دالة بسيطة بتستخدم علشان تحدث جدول الكتب المستعارة اللي ظاهر للعضو
    private void refreshBorrowedBooks() {
// بتستدعي الدالة اللي بتعرض كتب العضو اللي استعارها
        viewMyBorrowedBooks();
    }
// دي الدالة اللي بتعرض كل الكتب الموجودة في المكتبة سواء متاحة أو مستعارة
    private void viewAllBooks() {
// بنعمل جدول جديد من نوع TableView علشان نعرض فيه بيانات الكتب
        TableView<Book> tableView = new TableView<>();
        // بنضيف أعمدة للجدول: ID, Title, Author, Status
        tableView.getColumns().addAll(
                createColumn("ID", Book::getId),
                createColumn("Title", Book::getTitle),
                createColumn("Author", Book::getAuthor),
                createColumn("Status", b -> b.isAvailable() ? "✅ Available" : "❌ Borrowed")
        );
// بنجيب البيانات من قاعدة البيانات ونحطها في الجدول
        tableView.getItems().setAll(fetchBooksFromDatabase());
        // بنجهز زرار الرجوع (Back)
        Button closeBtn = createStyledButton("🔙 Back", "#7f8c8d", "#707b7c");
        closeBtn.setOnAction(e -> ((Stage) closeBtn.getScene().getWindow()).close());// لما يضغط عليه، يقفل النافذة
// زرار تحديث يعيد تحميل البيانات من قاعدة البيانات
        Button refreshBtn = createStyledButton("🔄 Refresh", "#16a085", "#138d75");
        refreshBtn.setOnAction(e -> tableView.getItems().setAll(fetchBooksFromDatabase()));
// بنجمع كل العناصر دي في VBox علشان نعرضهم في النافذة
        VBox vbox = new VBox(15, new Label("📚 All Books"), tableView, refreshBtn, closeBtn);
        vbox.setPadding(new Insets(25));// مسافات داخلية
        vbox.setAlignment(Pos.CENTER);// نخلي العناصر في المنتصف

        Stage stage = new Stage(); // بنفتح نافذة جديدة ونحط فيها الجدول والأزرار
        stage.setTitle("All Books"); // عنوان النافذة
        stage.setScene(new Scene(vbox, 700, 500));// الحجم 700×500
        stage.show();// نعرض النافذة
    }
    // الدالة دي شبيهة باللي فوق لكن بتعرض الكتب اللي العضو الحالي استعارها فقط
    private void viewMyBorrowedBooks() {
        // نفس الأعمدة: ID، Title، Author، Status
        TableView<Book> tableView = new TableView<>();
        tableView.getColumns().addAll(
                createColumn("ID", Book::getId),
                createColumn("Title", Book::getTitle),
                createColumn("Author", Book::getAuthor),
                createColumn("Status", b -> b.isAvailable() ? "✅ Available" : "❌ Borrowed")
        );
        // نحط البيانات في الجدول من الدالة fetchBorrowedBooksFromDatabase (كتب العضو بس)
        tableView.getItems().setAll(fetchBorrowedBooksFromDatabase());
        // زرار الرجوع
        Button closeBtn = createStyledButton("🔙 Back", "#7f8c8d", "#707b7c");
        closeBtn.setOnAction(e -> ((Stage) closeBtn.getScene().getWindow()).close());
        // زرار التحديث
        Button refreshBtn = createStyledButton("🔄 Refresh", "#16a085", "#138d75");
        refreshBtn.setOnAction(e -> tableView.getItems().setAll(fetchBorrowedBooksFromDatabase()));
        // بنعرض كل ده في VBox في نافذة جديدة
        VBox vbox = new VBox(15, new Label("📕 My Borrowed Books"), tableView, refreshBtn, closeBtn);
        vbox.setPadding(new Insets(25));
        vbox.setAlignment(Pos.CENTER);

        Stage stage = new Stage();
        stage.setTitle("My Borrowed Books");
        stage.setScene(new Scene(vbox, 700, 500));
        stage.show();
    }

// دي دالة مساعدة بنستخدمها علشان نعمل عمود جديد في الجدول بطريقة مرنة
    private <T> TableColumn<T, String> createColumn(String title, java.util.function.Function<T, String> mapper) {
        // بنعمل عمود جديد بالعنوان اللي جاي في البراميتر
        TableColumn<T, String> column = new TableColumn<>(title);
        // بنحدد إزاي نجيب البيانات اللي هتتعرض في كل صف في العمود
        column.setCellValueFactory(cellData -> new SimpleStringProperty(mapper.apply(cellData.getValue())));
        // تنسيقات الشكل (فونت، محاذاة، نوع الخط)
        column.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER-LEFT; -fx-font-family: 'Segoe UI';");
        return column; // نرجع العمود
    }
// دي الدالة اللي بتجيب كل الكتب من قاعدة البيانات
    private List<Book> fetchBooksFromDatabase() {
        List<Book> books = new ArrayList<>();// بنجهز لستة فاضية نحط فيها الكتب
        try (
                // بنفتح اتصال بقاعدة البيانات
                Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "libs", "12345");
             Statement stmt = conn.createStatement();
                // بننفذ query علشان نجيب كل الكتب
                ResultSet rs = stmt.executeQuery("SELECT ID, TITLE, AUTHOR, STATUS FROM BOOKS")) {
            // بنعدي على كل صف (كل كتاب)
            while (rs.next()) {
                String id = rs.getString("ID");// ID بتاع الكتاب
                String title = rs.getString("TITLE"); // العنوان
                String author = rs.getString("AUTHOR");// المؤلف
                String status = rs.getString("STATUS");// الحالة (Available or Borrowed)
                boolean isAvailable = "Available".equalsIgnoreCase(status);// لو الحالة "Available"، يبقى الكتاب متاح
                // بنضيف الكتاب للليستة
                books.add(new Book(title, author, id, isAvailable));
            }

        } catch (SQLException e) {
            // لو حصل خطأ، نعرضه للمستخدم برسالة
            showAlert(Alert.AlertType.ERROR, "Database Error", "❌ Error connecting to the database: " + e.getMessage());
        }
        return books;// نرجع الليستة
    }
// الدالة دي بتجيب كل الكتب اللي العضو الحالي استعارها من قاعدة البيانات
    private List<Book> fetchBorrowedBooksFromDatabase() {
        List<Book> books = new ArrayList<>();// بنجهز لستة فاضية علشان نحط فيها الكتب اللي هنرجعها
        try (
                // بنفتح اتصال بقاعدة البيانات باستخدام
                Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "libs", "12345");
                // بنجهز query SQL فيه JOIN بين جدول الكتب وجدول الاستعارات علشان نجيب بس الكتب اللي العضو استعارها
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT B.ID, B.TITLE, B.AUTHOR, B.STATUS " +
                             "FROM BOOKS B " +
                             "JOIN BORROWED_BOOK BB ON B.ID = BB.BOOK_ID " +
                             "WHERE BB.MEMBER_ID = ?")) {
            // بنحدد العضو اللي عايزين نجيب استعاراته (العضو الحالي)
            stmt.setString(1, member.getUserID());
            ResultSet rs = stmt.executeQuery();// بننفذ query
// بنعدي على النتائج ونحول كل صف إلى كائن Book ونضيفه في الليستة
            while (rs.next()) {
                String id = rs.getString("ID");
                String title = rs.getString("TITLE");
                String author = rs.getString("AUTHOR");
                String status = rs.getString("STATUS");
                boolean isAvailable = "Available".equalsIgnoreCase(status);// بنحول الستاتس لبولين يعني موجود او مش موجود
                books.add(new Book(title, author, id, isAvailable));// نضيف الكتاب في الليستة
            }

        } catch (SQLException e) {
            // لو حصل خطأ في الاتصال أو التنفيذ، بنعرض رسالة خطأ للمستخدم
            showAlert(Alert.AlertType.ERROR, "Database Error", "❌ Error loading borrowed books: " + e.getMessage());
        }
        return books; // نرجع الليستة اللي فيها الكتب المستعارة
    }

// الدالة دي بتدور على كتاب معين باستخدام ID من بين كل الكتب في قاعدة البيانات
    private Book fetchBookById(String bookId) {
        for (Book book : fetchBooksFromDatabase()) {// بنجيب كل الكتب وبندور فيهم
            if (book.getId().equals(bookId)) {
                return book;// لو لقينا الكتاب اللي ID بتاعه مطابق، نرجعه
            }
        }
        return null;// لو ملقيناهوش، نرجع null
    }
// الدالة دي بتعدل حالة الكتاب في جدول الكتب (مثلاً من متاح لمستعار والعكس)
    private void updateBookStatusInDatabase(String bookId, String status) {
        try (
                // اتصال بقاعدة البيانات
                Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "libs", "12345");
                //  تحديث للـ status بتاع الكتاب باستخدام ID
             PreparedStatement stmt = conn.prepareStatement("UPDATE BOOKS SET STATUS = ? WHERE ID = ?")) {
            stmt.setString(1, status);// الحالة الجديدة
            stmt.setString(2, bookId);// ID بتاع الكتاب
            stmt.executeUpdate();// بننفذ التحديث
        } catch (SQLException e) {
            // لو حصل خطأ أثناء التحديث، بنعرض رسالة خطأ
            showAlert(Alert.AlertType.ERROR, "Database Error", "❌ Error updating book status: " + e.getMessage());
        }
    }
    // الدالة دي بتسجل إن عضو معين استعار كتاب معين في جدول BORROWED_BOOK
    private void insertBorrowedRecord(String bookId, String memberId) {
        try (
                // اتصال بقاعدة البيانات
                Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "libs", "12345");
                // إدخال (INSERT) في جدول BORROWED_BOOK
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO BORROWED_BOOK (BOOK_ID, MEMBER_ID) VALUES (?, ?)")) {
            stmt.setString(1, bookId);// ID بتاع الكتاب
            stmt.setString(2, memberId);// ID بتاع العضو
            stmt.executeUpdate();// تنفيذ الإدخال
        } catch (SQLException e) {
            // لو حصل خطأ في الإدخال، نعرضه للمستخدم
            showAlert(Alert.AlertType.ERROR, "Database Error", "❌ Error inserting borrowed record: " + e.getMessage());
        }
    }
// الدالة دي بتحذف سجل الاستعارة لما العضو يرجع الكتاب
    private void deleteBorrowedRecord(String bookId, String memberId) {
        try (
                // فتح الاتصال
                Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "libs", "12345");
                // حذف السجل اللي بيربط الكتاب بالعضو
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM BORROWED_BOOK WHERE BOOK_ID = ? AND MEMBER_ID = ?")) {
            stmt.setString(1, bookId);// ID الكتاب
            stmt.setString(2, memberId);// ID العضو
            stmt.executeUpdate();// تنفيذ الحذف
        } catch (SQLException e) {
            // لو حصل خطأ، نعرض رسالة
            showAlert(Alert.AlertType.ERROR, "Database Error", "❌ Error deleting borrowed record: " + e.getMessage());
        }
    }
// الدالة دي بتتحقق إذا كان عضو معين فعلاً مستعير كتاب معين ولا لأ
    private boolean isBookBorrowedByMember(String bookId, String memberId) {
        try (
                // فتح الاتصال بقاعدة البيانات
                Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "libs", "12345");
                //  بيرجع عدد السجلات اللي فيها نفس الـ BOOK_ID و MEMBER_ID
                PreparedStatement stmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM BORROWED_BOOK WHERE BOOK_ID = ? AND MEMBER_ID = ?")) {
            stmt.setString(1, bookId); // ID الكتاب
            stmt.setString(2, memberId);// ID العضو
            ResultSet rs = stmt.executeQuery(); // تنفيذ
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "❌ Error checking borrowed record: " + e.getMessage());
        }
        return false;// في حالة وجود خطأ أو لا يوجد سجل، نرجع false
    }



    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
