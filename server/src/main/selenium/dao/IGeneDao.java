package selenium.dao;

import selenium.bean.Gene;

import java.sql.SQLException;

public interface IGeneDao {
    String storeGene (Gene gene) throws SQLException;
}
