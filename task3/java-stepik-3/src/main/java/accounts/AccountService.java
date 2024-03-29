package accounts;

import dbService.Executor;
import java.sql.Connection;
import java.sql.SQLException;

public class AccountService {
    private Executor executor;

    public AccountService(Connection connection) {
        this.executor = new Executor(connection);
        try {
            createTable();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create table", e);
        }
    }

    public User get(long id) throws SQLException {
        return executor.execQuery("select * from users where id='" + id + "'", result -> {
            result.next();
            return new User(result.getLong(1), result.getString(2), result.getString(3));
        });
    }

    public User getUserByLogin(String login) throws SQLException {
        return executor.execQuery("select * from users where login='" + login + "'", result -> {
            result.next();
            return new User(result.getLong(1), result.getString(2), result.getString(3));
        });
    }

    public long getUserId(String login) throws SQLException {
        return executor.execQuery("select id from users where login='" + login + "'", result -> {
            result.next();
            return result.getLong(1);
        });
    }

    public void insertUser(User user) throws SQLException {
        executor.execUpdate("insert into users (login, password) values ('" + user.getLogin() + "','" + user.getPassword() + "')");
    }

    public void createTable() throws SQLException {
        executor.execUpdate("create table if not exists users (id bigint auto_increment, login varchar(256), password varchar(256), primary key (id))");
    }

    public void dropTable() throws SQLException {
        executor.execUpdate("drop table users");
    }
}