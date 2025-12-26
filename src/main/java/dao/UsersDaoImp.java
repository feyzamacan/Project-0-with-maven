package dao;

import models.User;
import java.sql.*;

public class UsersDaoImp implements UsersDAO {

  private static final String SQL_SELECT_USER = "SELECT * FROM users WHERE username = ?";
  private static final String SQL_INSERT_USER = "INSERT INTO users (username, password, firstname, lastname, balance) VALUES (?, ?, ?, ?, ?)";
  private static final String SQL_UPDATE_USER = "UPDATE users SET username=?, password=?, firstname=?, lastname=?, balance=? WHERE username=?";
  private static final String SQL_UPDATE_BALANCE = "UPDATE users SET balance = balance + ? WHERE username = ? RETURNING balance";
  private static final String SQL_DELETE_USER = "DELETE FROM users WHERE username = ?";

  private void requireConnection(Connection con) throws SQLException {
    if (con == null) {
      throw new SQLException("Database connection is null");
    }
  }

  @Override
  public User getUser(Connection con, String username) throws SQLException {
    requireConnection(con);
    try (PreparedStatement stmt = con.prepareStatement(SQL_SELECT_USER)) {
      stmt.setString(1, username);
      try (ResultSet rs = stmt.executeQuery()) {
        if (!rs.next()) return null;

        User user = new User();
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setFirstname(rs.getString("firstname"));
        user.setLastname(rs.getString("lastname"));
        user.setBalance(rs.getFloat("balance"));
        return user;
      }
    }
  }

  @Override
  public int insertUser(Connection con, User u) throws SQLException {
    requireConnection(con);
    try (PreparedStatement stmt = con.prepareStatement(SQL_INSERT_USER)) {
      stmt.setString(1, u.getUsername());
      stmt.setString(2, u.getPassword());
      stmt.setString(3, u.getFirstname());
      stmt.setString(4, u.getLastname());
      stmt.setFloat(5, u.getBalance());
      return stmt.executeUpdate();
    }
  }

  @Override
  public int updateUser(Connection con, User u) throws SQLException {
    requireConnection(con);
    try (PreparedStatement stmt = con.prepareStatement(SQL_UPDATE_USER)) {
      stmt.setString(1, u.getUsername());
      stmt.setString(2, u.getPassword());
      stmt.setString(3, u.getFirstname());
      stmt.setString(4, u.getLastname());
      stmt.setFloat(5, u.getBalance());
      stmt.setString(6, u.getUsername());
      return stmt.executeUpdate();
    }
  }

  /**
   * Deposit or withdraw an amount
   * amt > 0 = deposit
   * amt < 0 = withdraw
   */
  @Override
  public int updateUser(Connection con, User user, float amt) throws SQLException {
    requireConnection(con);
    boolean originalAutoCommit = con.getAutoCommit();
    con.setAutoCommit(false);

    try (PreparedStatement stmt = con.prepareStatement(SQL_UPDATE_BALANCE)) {
      stmt.setFloat(1, amt);
      stmt.setString(2, user.getUsername());

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          float newBalance = rs.getFloat("balance");
          if (newBalance < 0) {
            con.rollback();
            throw new SQLException("Withdrawal exceeds available balance");
          }
        } else {
          con.rollback();
          throw new SQLException("User not found");
        }
      }

      con.commit();
      return 1;

    } catch (SQLException e) {
      con.rollback();
      throw e;
    } finally {
      con.setAutoCommit(originalAutoCommit);
    }
  }

  @Override
  public int deleteUser(Connection con, User u) throws SQLException {
    requireConnection(con);
    try (PreparedStatement stmt = con.prepareStatement(SQL_DELETE_USER)) {
      stmt.setString(1, u.getUsername());
      return stmt.executeUpdate();
    }
  }
}
