package selenium.dao;

import selenium.bean.Allele;

import java.sql.SQLException;

public interface IAlleleDao {
    void storeAllele (Allele allele) throws SQLException;
}
