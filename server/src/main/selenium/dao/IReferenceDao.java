package selenium.dao;

import selenium.bean.Reference;

import java.sql.SQLException;

public interface IReferenceDao {
    void storeReference(Reference reference) throws SQLException;
}
