package org.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateAccountForm {

    private LibrarySystem librarySystem;// كائن من النظام الرئيسي عشان نقدر نرجع للـ login form بعد إنشاء الحساب

    public CreateAccountForm(LibrarySystem librarySystem) {
        this.librarySystem = librarySystem;// بنخزن المرجع للنظام الرئيسي في المتغير المحلي
    }

    public void showCreateAccountForm(Stage primaryStage) {
        //عنوان الفورم
        Label titleLabel = new Label("Create Account");
        titleLabel.setFont(Font.font("Arial", 24));// حجم الخط
        titleLabel.setTextFill(Color.web("#ffffff"));// لون الخط أبيض

        // حقل الاسم
        Label nameLabel = new Label("Name:");
        nameLabel.setTextFill(Color.WHITE);// لون التسمية أبيض
        TextField nameField = new TextField();// حقل لإدخال الاسم
        nameField.setPromptText("Enter your name");
        styleInput(nameField);// تطبيق تنسيق مخصص للحقل

        // حقل الـ ID
        Label idLabel = new Label("User ID:");
        idLabel.setTextFill(Color.WHITE);
        TextField idField = new TextField();
        idField.setPromptText("Enter your ID");
        styleInput(idField);

        // اختيار الدور: عضو ولا أمين مكتبة
        Label roleLabel = new Label("Select Role:");
        roleLabel.setTextFill(Color.WHITE);
        ToggleGroup roleGroup = new ToggleGroup(); // علشان نخلي اختيار واحد فقط
        RadioButton memberButton = new RadioButton("Member");
        RadioButton librarianButton = new RadioButton("Librarian");
        memberButton.setToggleGroup(roleGroup);
        librarianButton.setToggleGroup(roleGroup);

        // زرار إنشاء الحساب
        Button createButton = new Button("Create Account");
        createButton.setPrefWidth(200);
        createButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 20;");
        // تغيير لون الزر لما الماوس يقف عليه
        createButton.setOnMouseEntered(e -> createButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-background-radius: 20;"));
        createButton.setOnMouseExited(e -> createButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 20;"));

        // تصميم واجهة الفورم باستخدام GridPane
        GridPane formGrid = new GridPane();
        formGrid.setVgap(15);// مسافة رأسية بين العناصر
        formGrid.setHgap(10);// مسافة أفقية
        formGrid.setAlignment(Pos.CENTER);// توسيط المحتوى
        // إضافة العناصر للمصفوفة
        formGrid.add(nameLabel, 0, 0);
        formGrid.add(nameField, 1, 0);
        formGrid.add(idLabel, 0, 1);
        formGrid.add(idField, 1, 1);
        formGrid.add(roleLabel, 0, 2);
        formGrid.add(memberButton, 1, 2);
        formGrid.add(librarianButton, 1, 3);
        formGrid.add(createButton, 1, 4);

        // تجميع العناصر كلها في VBox
        VBox root = new VBox(20, titleLabel, formGrid);
        root.setAlignment(Pos.CENTER);// توسيط رأسي
        root.setPadding(new Insets(40));// حواف داخلية
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #2c3e50, #34495e);"); // خلفية بتدرج لوني
        root.setEffect(new DropShadow(10, Color.BLACK)); // ظل للصندوق
        // عرض المشهد في الـ Stage

        Scene scene = new Scene(root, 450, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Create Account");
        primaryStage.show();

        //حدث الضغط على زر إنشاء الحساب
        createButton.setOnAction(e -> {
            String name = nameField.getText().trim(); // نجيب الاسم
            String userID = idField.getText().trim();// نجيب الـ ID
// التحقق إن الحقول مش فاضية وإن فيه دور مختار
            if (name.isEmpty() || userID.isEmpty() || (!memberButton.isSelected() && !librarianButton.isSelected())) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields and select a role.");
                return;
            }
//إدخال البيانات في قاعدة البيانات حسب الدور
            try (Connection conn = DB.DBConnection()) {
                String insertQuery;
                // لو المستخدم اختار Librarian نستخدم جدول أمناء المكتبة
                if (librarianButton.isSelected()) {
                    insertQuery = "INSERT INTO LIBRARIAN_LOGINS (ID, NAME) VALUES (?, ?)";
                } else {
                    insertQuery = "INSERT INTO MEMBER_LOGINS (ID, NAME) VALUES (?, ?)";
                }

                try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                    stmt.setInt(1, Integer.parseInt(userID)); // نحول الـ ID لرقم صحيح
                    stmt.setString(2, name);// نحط الاسم
                    stmt.executeUpdate();// تنفيذ الإدخال
                } catch (SQLException ex) {
                    // لو فيه مشكلة في الـ INSERT
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to connect to database: " + ex.getMessage());
                    return;
                }
            } catch (SQLException ex) {
                // لو فيه مشكلة في الاتصال نفسه
                showAlert(Alert.AlertType.ERROR, "Database Error", "Database connection failed: " + ex.getMessage());
                return;
            }

            // بعد إنشاء الحساب، نرجّع المستخدم لشاشة تسجيل الدخول
            if (librarianButton.isSelected()) {
                LoginForm loginForm = new LoginForm(librarySystem, false); // false معناها Librarian
                loginForm.showLoginForm(primaryStage);
            } else {
                LoginForm loginForm = new LoginForm(librarySystem, true);  // true معناها Member
                loginForm.showLoginForm(primaryStage);
            }
        });
    }
    // دالة لتنسيق الحقول بشكل موحّد
    private void styleInput(TextField field) {
        field.setStyle("-fx-background-radius: 10; -fx-background-color: #ecf0f1; -fx-padding: 10;");
        field.setPrefWidth(200);
    }
    // دالة مساعدة لعرض رسائل تنبيه للمستخدم
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
