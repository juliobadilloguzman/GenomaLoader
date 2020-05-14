package selenium.dao;

import selenium.bean.Gene;
import selenium.bean.Reference;
import selenium.util.MySQLConnection;

import java.sql.*;

public class ReferenceDao implements IReferenceDao {
    @Override
    public String storeReference(Reference reference) throws SQLException {
        Connection connection = MySQLConnection.getConnection("parcial2", "root","");
        try{
            String query = "INSERT INTO reference VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, reference.getIdReference());
            ps.setString(2, reference.getId());
            ps.setString(3, reference.getTitle());
            ps.setString(4, reference.getAuthors());
            ps.setString(5, reference.getArticleAbstract());
            ps.setString(6, reference.getPublicationTitle());
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return "Reference stored successfully";
            }
        } catch(Exception ex) {
            System.out.println(this.getClass().getCanonicalName() + " -> " + ex.getMessage());
        }
        return "Reference storage failed";
    }
}
