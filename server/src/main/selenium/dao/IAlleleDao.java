package selenium.dao;

import selenium.bean.Allele;

import java.sql.SQLException;

public interface IAlleleDao {
    String storeAllele (Allele allele) throws SQLException;
}
