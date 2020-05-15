package selenium.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/*public class MySQLConnection {
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
}*/

public class MySQLConnection {
    public static Connection getConnection(){
        String cadena = "jdbc:mysql://servidormysqlavanzadas.mysql.database.azure.com:3306/genoma?user=julioadmin@servidormysqlavanzadas&password=PaolaGuzman1996&serverTimezone=UTC&trustServerCertificate=true&autoReconnect=true&useSSL=false";
        Connection connection = null;
        try{
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            connection = DriverManager.getConnection(cadena);
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return connection;
    }
}
