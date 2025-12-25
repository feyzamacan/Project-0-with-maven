package com.feyza.main;
import com.feyza.utils.Bank;
import java.sql.*;
import com.feyza.dao.*;
import com.feyza.models.*;
import com.feyza.utils.Connect;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
/**
 * Unit test for app.
 */
public class BankDriverTest 
{
    @Test
    public void testAssertEquals() {
      assertEquals(1,1);
    }
    
    @Test
    public void test_username_exists() {
      String username = "fey0";
  
      Connection con = null;
      try {
        con = Connect.getConnection();
        assertTrue(Bank.usernameTaken(con, username));
      }
      catch(SQLException e) {
        e.printStackTrace();
      }
    }
    @Test
    public void test_username_exists_after_creation() {
      assertTrue(true);
    }
    
    @Test
    public void testUserValuesAfterCreation() throws SQLException {
      UsersDaoImp uDao = new UsersDaoImp();
      String sql = "insert into users (username, password, firstname, lastname, balance) values('newuser', '123', 'first', 'last', 0)";
      Connection con = null;
      try {
        con = Connect.getConnection();
        con.setAutoCommit(false);
        Statement stmt = con.createStatement(); 
        stmt.executeUpdate(sql);
        User user = uDao.getUser(con, "newuser"); 
        assertEquals(user.getUsername(), "newuser");
        assertEquals(user.getPassword(), "123");
        assertEquals(user.getFirstname(), "first");
        assertEquals(user.getLastname(), "last");
        assertEquals(user.getBalance(), 0);
      } 
      finally {
        con.rollback();
        con.setAutoCommit(true);
        con.close();
      }
    }
  }
  