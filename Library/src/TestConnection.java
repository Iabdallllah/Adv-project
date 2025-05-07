import java.sql.*;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe", "system", "1234"
            );
            System.out.println(" Connection Successful!");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
