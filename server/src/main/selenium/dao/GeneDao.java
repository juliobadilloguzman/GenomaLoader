package selenium.dao;

import selenium.bean.Gene;
import selenium.util.MySQLConnection;

import java.sql.*;

public class GeneDao implements IGeneDao {
    @Override
    public String storeGene(Gene gene) throws SQLException {
        Connection connection = MySQLConnection.getConnection();
        try{
            String query = "CALL gene_C(?, ?, ?, ?, ?, ?)";
            CallableStatement cs = connection.prepareCall(query);
            cs.setString(1, gene.getId());
            cs.setString(2, gene.getName());
            cs.setString(3, gene.getNomenclatureName());
            cs.setString(4, gene.getSummary());
            cs.setInt(5, Integer.parseInt(gene.getChromosome()));
            cs.setString(6, gene.getLocus());
            ResultSet rs = cs.executeQuery();
            if (rs.next()){
                gene.setIdGene(rs.getInt("idGene"));
                rs.close();
                cs.close();
                connection.close();
                return "Gene stored successfully";
            }
        } catch(Exception ex) {
            System.out.println(this.getClass().getCanonicalName() + " -> " + ex.getMessage());
        }
        return "Gene storage failed";
    }
}
