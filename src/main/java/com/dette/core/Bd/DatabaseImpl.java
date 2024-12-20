// package com.dette.core.Bd;

// import java.sql.*;

// public class DatabaseImpl implements DataBase {
//     private static final String url = "jdbc:postgresql://localhost:5432/postgres";
//     private static final String username = "postgres";
//     private static final String password = "amina";

//     protected Connection connection;
//     protected PreparedStatement statement;




//     @Override
//     public void openConnection() {
//         try {
//             if (connection == null || connection.isClosed()) {
//                 connection = DriverManager.getConnection(url, username, password);
//                 System.out.println("Connexion réussie à la base de données PostgreSQL.");
//             }
//         } catch (SQLException e) {
//             System.err.println("Erreur lors de l'ouverture de la connexion : " + e.getMessage());
//             e.printStackTrace();
//         }
//     }

//     @Override
//     public void closeConnection() {
//         try {
//             if (connection != null && !connection.isClosed()) {
//                 connection.close();
//                 System.out.println("Connexion fermée avec succès.");
//             }
//         } catch (SQLException e) {
//             System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
//             e.printStackTrace();
//         }
//     }

//     @Override
//     public ResultSet executeSelect(String sql) throws SQLException {
//         openConnection();
//         try {
//             statement = connection.prepareStatement(sql);
//             ResultSet resultSet = statement.executeQuery();
//             return resultSet;
//         } catch (SQLException e) {
//             System.err.println("Erreur lors de l'exécution de la requête SELECT : " + e.getMessage());
//             e.printStackTrace();
//             throw e;
//         }
//     }

//     @Override
//     public int executeUpdate(String sql) throws SQLException {
//         openConnection();
//         try {
//             statement = connection.prepareStatement(sql);
//             int nbreLigneAffecte = statement.executeUpdate();
//             return nbreLigneAffecte;
//         } catch (SQLException e) {
//             System.err.println("Erreur lors de l'exécution de la requête UPDATE : " + e.getMessage());
//             e.printStackTrace();
//             throw e;
//         }
//     }

//     @Override
//     public void initPreparedStatement(String sql) throws SQLException {
//         if (connection != null && !connection.isClosed()) {
//             statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//         } else {
//             throw new SQLException("La connexion à la base de données est fermée.");
//         }
//     }
// }


package com.dette.core.Bd;

import java.sql.*;

public class DatabaseImpl implements DataBase {
    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private static final String username = "postgres";
    private static final String password = "amina";

    protected Connection connection;
    protected PreparedStatement statement;

    @Override
    public void openConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Connexion réussie à la base de données PostgreSQL.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ouverture de la connexion : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion fermée avec succès.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public ResultSet executeSelect(String sql) throws SQLException {
        openConnection();
        try {
            statement = connection.prepareStatement(sql);
            return statement.executeQuery();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'exécution de la requête SELECT : " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }



    @Override
    public int executeUpdate(String sql) throws SQLException {
        openConnection();
        try {
            statement = connection.prepareStatement(sql);
            return statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'exécution de la requête UPDATE : " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void initPreparedStatement(String sql) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } else {
            throw new SQLException("La connexion à la base de données est fermée.");
        }
    }

    // Ajout d'un getter pour la connexion
    public Connection getConnection() {
        return connection;
    }
}

