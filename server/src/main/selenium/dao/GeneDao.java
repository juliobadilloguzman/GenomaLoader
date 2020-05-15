package selenium.dao;

import selenium.bean.Gene;
import selenium.util.MySQLConnection;

import java.sql.*;
import java.util.ArrayList;

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

    @Override
    public ArrayList<String> geneNames() throws SQLException {
        ArrayList<String> result = new ArrayList<>();
        Connection connection = MySQLConnection.getConnection();
        // SELECT DISTINCT name FROM gene;
        try{
            String query = "SELECT DISTINCT name FROM gene";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                result.add(rs.getString(1));
            }
            rs.close();
            ps.close();
            connection.close();
            return result;
        } catch(Exception ex) {
            System.out.println(this.getClass().getCanonicalName() + " -> " + ex.getMessage());
            return null;
        }
    }
}
