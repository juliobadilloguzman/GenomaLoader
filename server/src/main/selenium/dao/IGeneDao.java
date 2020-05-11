package selenium.dao;

import selenium.bean.Gene;

import java.sql.SQLException;

public interface IGeneDao {
    void storeGene (Gene gene) throws SQLException;
}
