package selenium.dao;

import selenium.bean.Gene;
import selenium.bean.Reference;
import selenium.util.MySQLConnection;

import java.sql.*;

public class ReferenceDao implements IReferenceDao {
    @Override
    public void storeReference(Reference reference) throws SQLException {
        Connection connection = MySQLConnection.getConnection("parcial2", "root","");
        String query = "INSERT INTO reference VALUES(?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, reference.getIdReference());
        ps.setString(2, reference.getId());
        ps.setString(3, reference.getTitle());
        ps.setString(4, reference.getAuthors());
        ps.setString(5, reference.getArticleAbstract());
        ps.setString(6, reference.getPublicationTitle());

        ps.executeQuery();
        ps.close();
        connection.close();
    }
}
