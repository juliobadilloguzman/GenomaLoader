package selenium.dao;

import selenium.bean.Allele;
import selenium.util.MySQLConnection;

import java.sql.*;

public class AlleleDao implements IAlleleDao {
    @Override
    public void storeAllele(Allele allele) throws SQLException {
        Connection connection = MySQLConnection.getConnection("parcial2", "root","");
        String query = "INSERT INTO allele VALUES(?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, allele.getIdAllele());
        ps.setInt(2, allele.getIdGene());
        ps.setString(3, allele.getGeneAccession());
        ps.setString(4, allele.getSequenceStart());
        ps.setString(5, allele.getSequenceEnd());
        ps.setString(6, allele.getStrand());
        ps.setString(7, allele.getSequence());

        ps.executeQuery();
        ps.close();
        connection.close();
    }
}
