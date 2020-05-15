package selenium.dao;

import selenium.util.MySQLConnection;

import java.sql.*;

public class Truncate {
    public String trunc() throws SQLException {
        Connection connection = MySQLConnection.getConnection();
        try{
            String query = "CALL clear()";
            CallableStatement cs = connection.prepareCall(query);
            cs.execute();
            cs.close();
            connection.close();
            return "Success";
        } catch(Exception ex) {
            System.out.println(this.getClass().getCanonicalName() + " -> " + ex.getMessage());
        }
        return "Failed";
    }
}
