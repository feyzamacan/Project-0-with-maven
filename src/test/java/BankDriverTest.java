import dao.UsersDaoImp;
import models.User;
import utils.Bank;
import utils.Connect;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BankDriverTest {

  private static Connection con;
  private static UsersDaoImp uDao;

  @BeforeAll
  public static void setup() throws SQLException {
    con = Connect.getConnection();
    uDao = new UsersDaoImp();
  }

  @BeforeEach
  public void startTransaction() throws SQLException {
    con.setAutoCommit(false);
  }

  @AfterEach
  public void rollbackTransaction() throws SQLException {
    con.rollback();
    con.setAutoCommit(true);
  }

  @Test
  @Order(1)
  public void testCreateUserAccount() throws SQLException {
    User user = new User();
    user.setUsername("testuser1");
    user.setPassword("pass");
    user.setFirstname("First");
    user.setLastname("Last");
    user.setBalance(0);

    int rows = uDao.insertUser(con, user);
    assertEquals(1, rows);

    User fetched = uDao.getUser(con, "testuser1");
    assertNotNull(fetched);
    assertEquals("testuser1", fetched.getUsername());
  }

  @Test
  @Order(2)
  public void testUsernameTaken() throws SQLException {
    User user = new User();
    user.setUsername("takenUser");
    user.setPassword("123");
    user.setFirstname("F");
    user.setLastname("L");
    user.setBalance(0);
    uDao.insertUser(con, user);

    assertTrue(Bank.usernameTaken(con, "takenUser"));
    assertFalse(Bank.usernameTaken(con, "nonexistentUser"));
  }

  @Test
  @Order(3)
  public void testLoginSuccess() throws SQLException {
    User user = new User();
    user.setUsername("loginUser");
    user.setPassword("secret");
    user.setFirstname("F");
    user.setLastname("L");
    user.setBalance(100);
    uDao.insertUser(con, user);

    User loggedIn = Bank.login(con, "loginUser", "secret");
    assertNotNull(loggedIn);
    assertEquals("loginUser", loggedIn.getUsername());
  }

  @Test
  @Order(4)
  public void testLoginFail() throws SQLException {
    assertNull(Bank.login(con, "fakeUser", "wrongPass"));
  }

  @Test
  @Order(5)
  public void testDeposit() throws SQLException {
    User user = new User();
    user.setUsername("depUser");
    user.setPassword("123");
    user.setFirstname("F");
    user.setLastname("L");
    user.setBalance(0);
    uDao.insertUser(con, user);

    uDao.updateUser(con, user, 500);
    User updated = uDao.getUser(con, "depUser");
    assertEquals(500, updated.getBalance());
  }

  @Test
  @Order(6)
  public void testWithdrawValid() throws SQLException {
    User user = new User();
    user.setUsername("withUser");
    user.setPassword("123");
    user.setFirstname("F");
    user.setLastname("L");
    user.setBalance(1000);
    uDao.insertUser(con, user);

    uDao.updateUser(con, user, -300);
    User updated = uDao.getUser(con, "withUser");
    assertEquals(700, updated.getBalance());
  }

  @Test
  @Order(7)
  public void testWithdrawExceed() throws SQLException {
    User user = new User();
    user.setUsername("overUser");
    user.setPassword("123");
    user.setFirstname("F");
    user.setLastname("L");
    user.setBalance(200);
    uDao.insertUser(con, user);

    SQLException exception = assertThrows(SQLException.class, () -> {
      uDao.updateUser(con, user, -300);
    });
    assertTrue(exception.getMessage().contains("exceeds"));
  }

  @Test
  @Order(8)
  public void testViewBalance() throws SQLException {
    User user = new User();
    user.setUsername("viewUser");
    user.setPassword("123");
    user.setFirstname("F");
    user.setLastname("L");
    user.setBalance(1500);
    uDao.insertUser(con, user);

    User fetched = uDao.getUser(con, "viewUser");
    assertEquals(1500, fetched.getBalance());
  }

  @Test
  @Order(9)
  public void testUpdateUserInfo() throws SQLException {
    User user = new User();
    user.setUsername("updateUser");
    user.setPassword("oldPass");
    user.setFirstname("OldF");
    user.setLastname("OldL");
    user.setBalance(50);
    uDao.insertUser(con, user);

    user.setPassword("newPass");
    user.setFirstname("NewF");
    user.setLastname("NewL");
    uDao.updateUser(con, user);

    User fetched = uDao.getUser(con, "updateUser");
    assertEquals("newPass", fetched.getPassword());
    assertEquals("NewF", fetched.getFirstname());
    assertEquals("NewL", fetched.getLastname());
  }

  @Test
  @Order(10)
  public void testDeleteUser() throws SQLException {
    User user = new User();
    user.setUsername("deleteUser");
    user.setPassword("123");
    user.setFirstname("F");
    user.setLastname("L");
    user.setBalance(0);
    uDao.insertUser(con, user);

    int deleted = uDao.deleteUser(con, user);
    assertEquals(1, deleted);

    User fetched = uDao.getUser(con, "deleteUser");
    assertNull(fetched);
  }
}


/*
//package feyza;
import dao.UsersDaoImp;
import models.User;
import utils.Bank;
import java.sql.*;

import utils.Connect;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

//import org.junit.jupiter.api.Test;
/**
 * Unit test for app.
 */
/*
public class BankDriverTest 
{
  private Connection connect = Connect.getConnection();
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
  */