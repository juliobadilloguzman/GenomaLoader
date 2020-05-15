package selenium.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MySQLConnection {
    public static Connection getConnection() throws SQLException{
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://servidormysqlavanzadas.mysql.database.azure.com:3306/genoma?useSSL=true&trustServerCertificate=true?serverTimezone=UTC", "julioadmin@servidormysqlavanzadas","PaolaGuzman1996");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }
}
