package selenium.dao;

import selenium.bean.Gene;
import selenium.util.MySQLConnection;

import java.sql.*;

public class GeneDao implements IGeneDao {
    @Override
    public void storeGene(Gene gene) throws SQLException {
        Connection connection = MySQLConnection.getConnection("parcial2", "root","");
        String query = "INSERT INTO gene VALUES(?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, gene.getIdGene());
        ps.setString(2, gene.getId());
        ps.setString(3, gene.getName());
        ps.setString(4, gene.getNomenclatureName());
        ps.setString(5, gene.getSummary());
        ps.setString(6, gene.getChromosome());
        ps.setString(7, gene.getLocus());

        ps.executeQuery();
        ps.close();
        connection.close();
    }
}
