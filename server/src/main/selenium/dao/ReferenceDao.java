package selenium.dao;

import selenium.bean.Gene;
import selenium.bean.Reference;
import selenium.util.MySQLConnection;

import java.sql.*;

public class ReferenceDao implements IReferenceDao {
    @Override
    public String storeReference(Reference reference) throws SQLException {
        Connection connection = MySQLConnection.getConnection("parcial2", "root","5th1ra5ukham45anam");
        try{
            String query = "CALL reference_C(?, ?, ?, ?, ?)";
            CallableStatement cs = connection.prepareCall(query);
            cs.setString(1, reference.getId());
            cs.setString(2, reference.getTitle());
            cs.setString(3, reference.getAuthors());
            cs.setString(4, reference.getArticleAbstract());
            cs.setString(5, reference.getPublicationTitle());
            ResultSet rs = cs.executeQuery();
            if (rs.next()){
                reference.setIdReference(rs.getInt("idReference"));
                rs.close();
                cs.close();
                connection.close();
                return "Reference stored successfully";
            }
        } catch(Exception ex) {
            System.out.println(this.getClass().getCanonicalName() + " -> " + ex.getMessage());
        }
        return "Reference storage failed";
    }
}
