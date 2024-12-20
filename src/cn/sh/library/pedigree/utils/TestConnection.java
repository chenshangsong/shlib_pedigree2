package cn.sh.library.pedigree.utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        String url = "jdbc:virtuoso://10.1.31.194:1111/CHARSET=UTF-8";
        String username = "dba";
        String password = "Shlibrary123";

        try {
            Class.forName("virtuoso.jdbc4.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            if (connection != null) {
                System.out.println("Connection successful!");
                connection.close();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
