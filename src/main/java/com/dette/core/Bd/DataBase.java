// package com.dette.core.Bd;

// import java.sql.ResultSet;
// import java.sql.SQLException;

// public interface DataBase {
//     void openConnection();
//     void closeConnection();
//     ResultSet executeSelect(String sql)throws SQLException;
//     int executeUpdate(String sql) throws SQLException;
//     void initPreparedStatement(String sql) throws SQLException;
    
// }


package com.dette.core.Bd;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DataBase {
    void openConnection();
    void closeConnection();
    ResultSet executeSelect(String sql) throws SQLException;
    int executeUpdate(String sql) throws SQLException;
    void initPreparedStatement(String sql) throws SQLException;
}
