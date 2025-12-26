package dao;
import java.sql.*;
import models.User;

//import com.feyza.models.User;

public interface UsersDAO {
  public User getUser(Connection con, String username) throws SQLException;
  public int insertUser(Connection con, User u) throws SQLException;
  public int updateUser(Connection con, User u) throws SQLException;
  public int deleteUser(Connection con, User u) throws SQLException;
  public int updateUser(Connection con, User u, float balance) throws SQLException;
}

//the interface defines the rules for how user data is accessed in the database
//without implementing the actual SQL.

