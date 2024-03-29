package dbService;

import accounts.AccountService;
import accounts.User;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBService {
    private final Connection connection;

    public DBService() {
        this.connection = getH2Connection();
    }

    public User getUser(long id) throws SQLException {
        try {
            return (new AccountService(connection).get(id));
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public long addUser(User user) throws SQLException {
        try {
            connection.setAutoCommit(false);
            AccountService accountService = new AccountService(connection);
            accountService.createTable();
            accountService.insertUser(user);
            connection.commit();
            return accountService.getUserId(user.getLogin());
        }
        catch (SQLException e) {
            try {
                connection.rollback();
            }
            catch (SQLException ignore) {
            }
            throw new SQLException(e);
        }
        finally {
            try {
                connection.setAutoCommit(true);
            }
            catch (SQLException ignore) {
            }
        }
    }

    public void cleanUp() throws SQLException {
        AccountService dao = new AccountService(connection);
        try {
            dao.dropTable();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
    public static Connection getH2Connection() {
        try {
            String url = "jdbc:h2:./h2db";
            String name = "test";
            String pass = "test";

            JdbcDataSource ds = new JdbcDataSource();
            ds.setURL(url);
            ds.setUser(name);
            ds.setPassword(pass);

            Connection connection = DriverManager.getConnection(url, name, pass);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
