package org.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // بيعمل object من كلاس LibrarySystem (عشان يبقى عندك النظام جاهز تديه للـ LoginForm لما المستخدم يسجل الدخول)
        LibrarySystem librarySystem = new LibrarySystem();

        //بيعمل عنوان مكتوب
        Label title = new Label("📚 Welcome to the Library System");
        title.setFont(Font.font("Segoe UI", 28));
        title.setTextFill(Color.WHITE);

        // بيعمل زر مكتوب عليه Login as Member
        Button memberBtn = new Button("Login as Member");
        // بيعمل زر مكتوب عليه  Login as Librarian
        Button librarianBtn = new Button("Login as Librarian");
        // بيعمل زر مكتوب عليه Create Account (لإنشاء حساب)
        Button createAccountBtn = new Button("Create Account");

        // بيعمل استايل للزر لونه و الhover دي معناها لما اشاور علي الزر هيعمل لون مختلف
        styleButton(memberBtn, "#2980b9", "#27ae60");
        styleButton(librarianBtn, "#16a085", "#8e44ad");
        styleButton(createAccountBtn, "#e67e22", "#f39c12");

        //VBox: بتحط العناصر فوق بعض بمسافة 25 بين كل عنصر
        VBox root = new VBox(25, title, memberBtn, librarianBtn, createAccountBtn);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #2c3e50, #34495e);");
        root.setEffect(new DropShadow(10, Color.BLACK));

        Scene scene = new Scene(root, 500, 450);
        primaryStage.setTitle("Library System");
        primaryStage.setScene(scene);
        primaryStage.show();

        // أكشن زرار الميمبر
        memberBtn.setOnAction(e -> {
            LoginForm loginForm = new LoginForm(librarySystem, true);
            loginForm.showLoginForm(primaryStage);
        });

        // أكشن زر امين المكتبة
        librarianBtn.setOnAction(e -> {
            LoginForm loginForm = new LoginForm(librarySystem, false);
            loginForm.showLoginForm(primaryStage);
        });

        // أكشن زر إنشاء حساب
        createAccountBtn.setOnAction(e -> {
            CreateAccountForm createAccountForm = new CreateAccountForm(librarySystem);
            createAccountForm.showCreateAccountForm(primaryStage);
        });
    }

    //ميثود استايل الازرار

    private void styleButton(Button button, String color, String hoverColor) {
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 20;");
        button.setPrefWidth(220);
        button.setPrefHeight(40);
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + hoverColor + "; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 20;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 20;"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
