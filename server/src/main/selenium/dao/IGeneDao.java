package selenium.dao;

import selenium.bean.Gene;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IGeneDao {
    String storeGene (Gene gene) throws SQLException;
    ArrayList<String> geneNames()throws SQLException;
}
