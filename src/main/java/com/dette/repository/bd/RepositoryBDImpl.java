// package com.dette.repository.bd;

// // import java.sql.Connection;
// // import java.sql.PreparedStatement;
// // import java.sql.SQLException;
// import java.util.List;

// import com.dette.core.repository.Repository;

// public class RepositoryBDImpl<T> implements Repository<T> {
//     @Override
//     public void insert(T data) {
//         System.out.println("Creation de compte dans une BD");

//     }

//     @Override
//     public List<T> selectAll() {
//         return null;
//     }

// }

// package com.dette.repository.bd;

// import com.dette.repository.Repository;
// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.SQLException;
// import java.util.List;

// public abstract class RepositoryBDImpl<T> implements Repository<T> {

//     protected String tableName; 

//     public RepositoryBDImpl(String tableName) {
//         this.tableName = tableName;
//     }

//     @Override
//     public void insert(T data) {
        
//         String query = getInsertQuery();

//         try (Connection connection = MySQLConnection.getConnection();
//              PreparedStatement preparedStatement = connection.prepareStatement(query)) {

//             setPreparedStatement(preparedStatement, data);  // Définir les paramètres dynamiquement
//             preparedStatement.executeUpdate();
//             System.out.println("Données insérées dans la table " + tableName + " avec succès.");

//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }

//     @Override
//     public List<T> selectAll() {
      
//         String query = "SELECT * FROM " + tableName;

//         return null;
//     }

//     // Chaque classe enfant devra fournir la requête SQL exacte pour l'insertion
//     protected abstract String getInsertQuery();

//     // Chaque classe enfant devra définir comment remplir le PreparedStatement avec les données
//     protected abstract void setPreparedStatement(PreparedStatement preparedStatement, T data) throws SQLException;
// }

