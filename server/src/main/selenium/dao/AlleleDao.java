package selenium.dao;

import selenium.bean.Allele;
import selenium.util.MySQLConnection;

import java.sql.*;

public class AlleleDao implements IAlleleDao {
    @Override
    public String storeAllele(Allele allele) throws SQLException {
        Connection connection = MySQLConnection.getConnection("parcial2", "root","");
        try{
            String query = "INSERT INTO allele VALUES(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, allele.getIdAllele());
            ps.setInt(2, allele.getIdGene());
            ps.setString(3, allele.getGeneAccession());
            ps.setString(4, allele.getSequenceStart());
            ps.setString(5, allele.getSequenceEnd());
            ps.setString(6, allele.getStrand());
            ps.setString(7, allele.getSequence());
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return "Allele stored successfully";
            }
        } catch(Exception ex) {
            System.out.println(this.getClass().getCanonicalName() + " -> " + ex.getMessage());
        }
        return "Allele storage failed";
    }
}
