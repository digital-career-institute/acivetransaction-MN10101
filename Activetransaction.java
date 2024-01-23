package jdbc;
import java.sql.*;
public class Activetransaction {
    private static Connection connection;
    private static void updateQuery() throws SQLException {
        // Check if there is an active transaction
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(1) AS count FROM INFORMATION_SCHEMA.INNODB_TRX WHERE trx_mysql_thread_id = CONNECTION_ID()")) {
            if (resultSet.next() && resultSet.getInt("count") == 0) {
                // No active transaction, perform the update
                try (java.sql.PreparedStatement preparedStatement = connection.prepareStatement("UPDATE employee SET age = ? WHERE first_name = 'Maya' AND last_name = 'Lola'")) {
                    preparedStatement.setInt(1, 25); // Set the new age value
                    preparedStatement.executeUpdate();
                    System.out.println("Update completed successfully.");
                }
            } else {
                System.out.println("There is an active transaction. Skipping update.");
            }
        }
    }
    public static void main(String[] args) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db1", "root", "");
            updateQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}