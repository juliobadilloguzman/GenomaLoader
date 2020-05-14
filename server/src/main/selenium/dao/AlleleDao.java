package selenium.dao;

import selenium.bean.Allele;
import selenium.util.MySQLConnection;

import java.sql.*;

public class AlleleDao implements IAlleleDao {
    @Override
    public String storeAllele(Allele allele) throws SQLException {
        Connection connection = MySQLConnection.getConnection("dba_parcial2", "root","5th1ra5ukham45anam");
        try{
            String query = "CALL allele_C(?, ?, ?, ?, ?, ?)";
            CallableStatement cs = connection.prepareCall(query);
            cs.setInt(1, allele.getIdGene());
            cs.setString(2, allele.getGeneAccession());
            cs.setString(3, allele.getSequenceStart());
            cs.setString(4, allele.getSequenceEnd());
            cs.setString(5, allele.getStrand());
            cs.setString(6, allele.getSequence());
            ResultSet rs = cs.executeQuery();
            if (rs.next()){
                allele.setIdAllele(rs.getInt("idAllele"));
                rs.close();
                cs.close();
                connection.close();
                return "allele stored successfully";
            }
        } catch(Exception ex) {
            System.out.println(this.getClass().getCanonicalName() + " -> " + ex.getMessage());
        }
        return "Allele storage failed";
    }
}
