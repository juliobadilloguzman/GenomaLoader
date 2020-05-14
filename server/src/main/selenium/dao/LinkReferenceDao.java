package selenium.dao;

import selenium.bean.Allele;
import selenium.bean.Reference;
import selenium.util.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LinkReferenceDao {
    public String linkReference(Reference reference, Allele allele) throws SQLException {
        Connection connection = MySQLConnection.getConnection("parcial2", "root","5th1ra5ukham45anam");
        try{
            String query = "INSERT INTO allele_biblography(allele_biblography.id_allele, allele_biblography.id_reference)VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, allele.getIdAllele());
            ps.setInt(2, reference.getIdReference());
            if(ps.execute()) {
                ps.close();
                connection.close();
                return "Reference stored successfully";
            }
        } catch(Exception ex) {
            System.out.println(this.getClass().getCanonicalName() + " -> " + ex.getMessage());
        }
        return "Reference storage failed";
    }
}
