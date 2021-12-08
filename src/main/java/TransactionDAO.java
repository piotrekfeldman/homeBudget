import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class TransactionDAO {

    private final Connection connection;

    public TransactionDAO() {

        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance", "newuser", "Password123!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    boolean update(Transaction transaction) {
        final String sql = String.format("""
                        UPDATE
                            transaction
                        SET
                            type = '%s',
                            description = '%s',
                            amount = '%s',
                            date = '%s'
                        WHERE
                            id = %d
                        """,
                transaction.getType(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getDate().toString(),
                transaction.getId());
        try (Statement statement = connection.createStatement()) {
            int updatedRows = statement.executeUpdate(sql);
            return updatedRows != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    Double findByTransactionType(TransactionType transactionType) {
        Double amount = null;
        final String sql = String.format("SELECT SUM(amount) FROM transaction where type='%s'", transactionType);
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                amount = resultSet.getDouble(1);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return amount;


    }

    void save(Transaction transaction) {

        final String sql = String.format("INSERT INTO transaction(type, description, amount, date) VALUES ('%s','%s','%s','%s')",
                transaction.getType(), transaction.getDescription(), transaction.getAmount(), transaction.getDate().toString());

        try (Statement statement = connection.createStatement()) {

            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = statement.getGeneratedKeys();

            while (generatedKeys.next()) {
                transaction.setId(generatedKeys.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    boolean delete(int id) {
        final String sql = "DELETE FROM transaction WHERE id = " + id;
        try (Statement statement = connection.createStatement()) {
            int updatedRows = statement.executeUpdate(sql);
            return updatedRows != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
