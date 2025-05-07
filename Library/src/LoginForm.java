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
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginForm {

    private LibrarySystem librarySystem;  // هنا بنعرف متغير علشان نربط النظام بتاع المكتبة
    private boolean isMemberLogin; // متغير بيوضح إذا كان ده تسجيل دخول عضو أو أمين مكتبة

    // هنا بنعمل constructor علشان نمرر البيانات اللازمة للكلاس ده
    public LoginForm(LibrarySystem librarySystem, boolean isMemberLogin) {
        this.librarySystem = librarySystem; // بنخزن النظام بتاع المكتبة
        this.isMemberLogin = isMemberLogin; // بنحدد إذا كان تسجيل دخول عضو ولا أمين مكتبة
    }

    // هنا بنعرض شاشة تسجيل الدخول
    public void showLoginForm(Stage primaryStage) {
        // هنا بنعرض العنوان (اسم الواجهة) بناءً على نوع المستخدم (عضو أو أمين مكتبة)
        Label titleLabel = new Label(isMemberLogin ? "Member Login" : "Librarian Login");
        titleLabel.setFont(Font.font("Arial", 24));  // بنحدد نوع وحجم الخط
        titleLabel.setTextFill(Color.web("#ffffff")); // بنحدد لون النص

        // هنا بنعمل input field لاسم المستخدم
        Label nameLabel = new Label("Name:");
        nameLabel.setTextFill(Color.WHITE);  // بنحدد لون النص للـ label
        TextField nameField = new TextField();  // بنعمل حقل نصي لإدخال الاسم
        nameField.setPromptText("Enter your name");  // بنعرض نص إرشادي داخل الـ TextField
        styleInput(nameField); // بننظم شكل الـ TextField باستخدام دالة styleInput

        // هنا بنعمل input field لرقم المستخدم (ID)
        Label idLabel = new Label("User ID:");
        idLabel.setTextFill(Color.WHITE); // نفس فكرة الاسم، بنحدد لون النص
        TextField idField = new TextField(); // حقل إدخال الـ ID
        idField.setPromptText("Enter your ID");  // نص إرشادي لرقم الـ ID
        styleInput(idField);  // بننظم شكل الـ TextField

        // هنا بنضيف زرار الدخول
        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(200);  // بنحدد عرض الزرار
        loginButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 20;");  // بنغير شكل الزرار
        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-background-radius: 20;")); // لما الماوس يدخل على الزرار
        loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 20;")); // لما الماوس يخرج من الزرار

        // هنا بنعمل GridPane عشان ننظم شكل الفورم
        GridPane formGrid = new GridPane();
        formGrid.setVgap(15);  // المسافة بين الخلايا رأسياً
        formGrid.setHgap(10);  // المسافة بين الخلايا أفقياً
        formGrid.setAlignment(Pos.CENTER);  // نحط الخلايا في المنتصف
        formGrid.add(nameLabel, 0, 0);  // إضافة اللابل بتاع الاسم في المكان المحدد
        formGrid.add(nameField, 1, 0);  // إضافة الـ TextField بتاع الاسم
        formGrid.add(idLabel, 0, 1);  // إضافة اللابل بتاع الـ ID
        formGrid.add(idField, 1, 1);  // إضافة الـ TextField بتاع الـ ID
        formGrid.add(loginButton, 1, 2);  // إضافة الزرار في المكان المحدد

        // هنا بنعمل VBox عشان نرتب كل العناصر فوق بعض
        VBox root = new VBox(20, titleLabel, formGrid);  // هنا بنحدد مسافة الـ VBox بين العناصر
        root.setAlignment(Pos.CENTER);  // بنخلي العناصر في المنتصف
        root.setPadding(new Insets(40));  // بنحط padding حوالين العناصر
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #2c3e50, #34495e);");  // بنضيف تأثير للخلفية (لون تدريجي)
        root.setEffect(new DropShadow(10, Color.BLACK));  // بنضيف تأثير الظل على الخلفية

        Scene scene = new Scene(root, 450, 350);  // بنعمل مشهد (Scene) ونعرضه في حجم 450x350
        primaryStage.setScene(scene);  // بنحدد الـ Scene على الـ Stage
        primaryStage.setTitle(isMemberLogin ? "Member Login" : "Librarian Login");  // بنحدد عنوان الـ Stage بناءً على نوع الدخول
        primaryStage.show();  // عرض الواجهة

        // هنا بنحدد الحدث اللي يحصل لما الزرار يتضغط
        loginButton.setOnAction(e -> {
            String name = nameField.getText().trim();  // بنأخذ النص من الـ TextField بتاع الاسم
            String userID = idField.getText().trim();  // بنأخذ النص من الـ TextField بتاع الـ ID

            // لو المستخدم مش دخل أي بيانات، هنعرض رسالة خطأ
            if (name.isEmpty() || userID.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter both name and user ID.");
                return;
            }

            // من هنا بنبدأ نعمل منطق التحقق من البيانات في قاعدة البيانات
            try (Connection conn = DB.DBConnection()) {  // بنعمل اتصال بقاعدة البيانات
                String query;
                if (isMemberLogin) {  // لو التسجيل دخول عضو
                    query = "SELECT * FROM MEMBER_LOGINS WHERE ID = ? AND NAME = ?";  // الاستعلام لقاعدة بيانات الأعضاء
                } else {  // لو التسجيل دخول أمين مكتبة
                    query = "SELECT * FROM LIBRARIAN_LOGINS WHERE ID = ? AND NAME = ?";  // الاستعلام لقاعدة بيانات الأمناء
                }

                try (PreparedStatement stmt = conn.prepareStatement(query)) {  // بنجهز الاستعلام وننفذه
                    stmt.setInt(1, Integer.parseInt(userID));  // بنحول الـ ID لعدد صحيح ونمرره
                    stmt.setString(2, name);  // بنمرر الاسم
                    ResultSet rs = stmt.executeQuery();  // بننفذ الاستعلام
                    if (rs.next()) {  // لو لقيت نتيجة
                        // لو تسجيل الدخول ناجح، هنفتح الواجهة المناسبة
                        if (isMemberLogin) {
                            Member member = new Member(name, userID);  // بنعمل عضو جديد
                            librarySystem.addMember(member);  // بنضيفه للنظام
                            MemberDashboard memberDashboard = new MemberDashboard(librarySystem, member);  // بنفتح الواجهة بتاعة العضو
                            memberDashboard.showMemberDashboard(primaryStage);  // عرض الواجهة
                        } else {
                            Librarian librarian = new Librarian(name, userID);  // بنعمل أمين مكتبة جديد
                            LibrarianDashboard librarianDashboard = new LibrarianDashboard(librarySystem, librarian);  // بنفتح الواجهة بتاعة الأمين
                            librarianDashboard.showLibrarianDashboard(primaryStage);  // عرض الواجهة
                        }
                    } else {  // لو فشل تسجيل الدخول
                        showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid User ID or Name.");
                    }
                }
            } catch (SQLException ex) {  // لو في مشكلة في الاتصال بقاعدة البيانات
                showAlert(Alert.AlertType.ERROR, "Database Error", "Database connection failed: " + ex.getMessage());
            }
        });
    }

    // هنا بنعمل دالة تنسيق الحقول (Style) زي الـ TextField
    private void styleInput(TextField field) {
        field.setStyle("-fx-background-radius: 10; -fx-background-color: #ecf0f1; -fx-padding: 10;");  // تنسيق الخلفية والحدود
        field.setPrefWidth(200);  // تحديد عرض الـ TextField
    }

    // هنا بنعرض رسالة تنبيه (Alert) لو في خطأ
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);  // بنحدد نوع التنبيه
        alert.setTitle(title);  // بنحدد العنوان
        alert.setHeaderText(null);  // مش هنضيف عنوان فرعي
        alert.setContentText(message);  // بنعرض الرسالة
        alert.showAndWait();  // عرض التنبيه وانتظار المستخدم
    }
}
