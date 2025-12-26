package utils;

import dao.UsersDaoImp;
import models.User;

import java.util.*;
import java.sql.*;

public class Bank {

  public static final String CREATE_USR = "Username: ";
  public static final String CREATE_P = "Password: ";
  public static final String CREATE_FIRSTNAME = "First name: ";
  public static final String CREATE_LASTNAME = "Last name: ";
  public static final int STARTING_BALANCE = 0;
  public static final String USR_TAKEN = "username taken";
  public static final String READ_USERNAME = "Username: ";
  public static final String READ_P = "Password: ";
  public static final String DEPOSIT_AMT = "enter deposit amount: ";
  public static final String WITHDRAW_AMT = "enter withdrawal amount: ";
  public static final String BALANCE = "balance";
  public static final String BAD_LOGIN = "Wrong username or password";
  public static final String E_DEPOSIT = "Something went wrong";
  public static final String E_WITHDRAW = "Something went wrong";

  private static final UsersDaoImp uDao = new UsersDaoImp();
  private static final Scanner scanner = new Scanner(System.in);

  private Bank() {}

  public static void print(Object o) {
    System.out.println(o);
  }

  // ---------- INPUT HELPERS ----------

  public static String createUsername(String msg) {
    System.out.print(msg);
    return scanner.nextLine();
  }

  public static String createPassword(String msg) {
    System.out.print(msg);
    return scanner.nextLine();
  }

  public static String readUsername() {
    System.out.print(READ_USERNAME);
    return scanner.nextLine();
  }

  public static String readPassword() {
    System.out.print(READ_P);
    return scanner.nextLine();
  }

  public static int getSelection() {
    try {
      return Integer.parseInt(scanner.nextLine());
    } catch (NumberFormatException e) {
      return -1;
    }
  }

  // ---------- USER LOGIC ----------

  public static User login(Connection con, String username, String password) throws SQLException {
    User user = uDao.getUser(con, username);
    if (user != null && password.equals(user.getPassword())) {
      return user;
    }
    return null;
  }

  public static boolean usernameTaken(Connection con, String username) throws SQLException {
    User user = uDao.getUser(con, username);
    if (user == null) {
      return false;
    }
    System.out.println(USR_TAKEN);
    return true;
  }

  public static User createUserAccount() {
    User user = new User();

    try (Connection con = Connect.getConnection()) {
      String username;
      String password;

      do {
        System.out.print(CREATE_USR);
        username = scanner.nextLine();

        System.out.print(CREATE_P);
        password = scanner.nextLine();
      } while (usernameTaken(con, username));

      System.out.print(CREATE_FIRSTNAME);
      String firstname = scanner.nextLine();

      System.out.print(CREATE_LASTNAME);
      String lastname = scanner.nextLine();

      user.setUsername(username);
      user.setPassword(password);
      user.setFirstname(firstname);
      user.setLastname(lastname);
      user.setBalance(STARTING_BALANCE);

      uDao.insertUser(con, user);

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return user;
  }

  // ---------- ACCOUNT ACTIONS ----------

  public static void view(Connection con, User user) {
    try {
      user = uDao.getUser(con, user.getUsername());
      System.out.printf("Current Balance: %.2f%n", user.getBalance());
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void deposit(Connection con, User user) {
    Float amt = getAmountInput(DEPOSIT_AMT);
    if (amt == null) return;

    try {
      uDao.updateUser(con, user, amt);
    } catch (SQLException e) {
      System.out.println(E_DEPOSIT);
    }
  }

  public static void withdraw(Connection con, User user) {
    Float amt = getAmountInput(WITHDRAW_AMT);
    if (amt == null) return;

    try {
      uDao.updateUser(con, user, -amt);
    } catch (SQLException e) {
      System.out.println(E_WITHDRAW);
    }
  }

  private static Float getAmountInput(String prompt) {
    while (true) {
      System.out.print(prompt);
      String input = scanner.nextLine();

      if (input.equalsIgnoreCase("b")) return null;

      try {
        float amt = Float.parseFloat(input);
        if (amt <= 0) {
          System.out.println("Please enter a positive number");
        } else {
          return amt;
        }
      } catch (NumberFormatException e) {
        System.out.println("Enter a valid number");
      }
    }
  }

  // ---------- MENUS ----------

  public static void showSplashMenu() {
    System.out.println("1. First time login");
    System.out.println("2. Login");
    System.out.println("3. Exit");
  }

  public static void showLoggedinMenu() {
    System.out.println("1. Deposit");
    System.out.println("2. Withdraw");
    System.out.println("3. View");
    System.out.println("4. Logout");
  }
}




/*package utils;
import dao.UsersDaoImp;
import models.User;
//import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.LogManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Console;
import java.util.*;
import java.sql.*;

//business logic for the banking application
//handles user interactions, input validation, and calls DAO methods for database operations



public class Bank {
  public static final String CREATE_USR = "Username: ";
  public static final String CREATE_P = "Password: ";
  public static final String CREATE_FIRSTNAME = "First name: ";
  public static final String CREATE_LASTNAME = "Last name: ";
  public static final int STARTING_BALANCE = 0;
  public static final String USR_TAKEN = "username taken";
  public static final String READ_USERNAME = "Username: "; 
  public static final String READ_P = "Password: ";
  public static final String DEPOSIT_AMT = "enter deposit amount: ";
  public static final String WITHDRAW_AMT = "enter withdrawal amount: ";
  public static final String DELIMIT = " "; 
  public static final String BALANCE = "balance";
  public static final String BAD_LOGIN = "Wrong username or password";
  public static final String E_DEPOSIT = "Something went wrong";
  public static final String E_WITHDRAW = "Something went wrong";
  private static UsersDaoImp uDao = new UsersDaoImp();
  //table identifiers
  //private static Logger log = LogManager.getLogger(com.feyza.utils.Bank.class.getName());
  private Bank() {
    super();
  }
  public static void print(Object o) {
    System.out.println(o);
  }

  public static String createUsername(String msg) {
    Console c = System.console();
    return c.readLine(msg);
  }

  public static String createPassword(String msg) {
    Console c = System.console();
    System.out.println(msg);
    char[] pwd = c.readPassword();
    System.out.println(pwd);
    return new String(pwd);
  }
  public static User login(Connection con, String username, String password) throws SQLException {
    User user = uDao.getUser(con, username);
    if(user != null && (password.equals(user.getPassword()))) {
      return user; 
    }
    return null;
  } 

  public static boolean usernameTaken(Connection con, String username) throws SQLException {
    User user = uDao.getUser(con, username);
    if(user == null) {
      return false;
    }
    System.out.println(USR_TAKEN);
    return true;
  }

  public static User createUserAccount() {
    Console c = System.console();
    String username = null;
    char[] password = new char[50];
    String firstname = null;
    String lastname = null;
    UsersDaoImp uDao = new UsersDaoImp(); 
    User user = new User();
     
    try {
      Connection con = Connect.getConnection();
      do {
        username = c.readLine(CREATE_USR);
        password = c.readPassword(CREATE_P);
      }
      while (usernameTaken(con, username));

      firstname = c.readLine(CREATE_FIRSTNAME);
      lastname = c.readLine(CREATE_LASTNAME);
      user.setUsername(username);
      user.setPassword(new String(password));
      user.setFirstname(firstname);
      user.setLastname(lastname);
      user.setBalance(STARTING_BALANCE);
      uDao.insertUser(con, user);
      
    }
    catch(SQLException e) {
     //log.error(e);
    }
    return user;
  }
  //views balance of current user
  public static void view(Connection con, User user) {
    UsersDaoImp uDao = new UsersDaoImp(); 
    try {
      user = uDao.getUser(con, user.getUsername());
      System.out.println(String.format("Current Balance: %.2f", user.getBalance()));
    }
    catch(SQLException e) {
      //log.error("some error", e);
    }

  }

  public static String readUsername() {
    Console c = System.console();
    return c.readLine(READ_USERNAME);
  }
  public static String readPassword() {
    Console c = System.console();
    System.out.println(READ_P);
    return new String(c.readPassword());
  }

  public static void showSplashMenu() {
    System.out.println("1. First time login");
    System.out.println("2. Login");
    System.out.println("3. exit");
  }
  public static void showLoggedinMenu() {
    System.out.println("1. deposit");
    System.out.println("2. withdraw");
    System.out.println("3. view");
    System.out.println("4. logout");
  }
  public static Scanner getScanLine(Scanner sc, String target) {
    while(sc.hasNext())
    {
      try {
        if(sc.next().contains(target)) 
        {
          return sc;
        }
      }
      catch(NoSuchElementException e) {
        //log.error(e);
        break;
      }
    }
    return sc;
  }
  public static int getSelection() {
    Console c = System.console();
    String selection = c.readLine();
    Integer selectionInt = null; 
    try {
      selectionInt = Integer.parseInt(selection);  //getting input
    }
    catch(NumberFormatException e) {
      return 5;
    }
    return selectionInt;
  }

  public static void deposit(Connection con, User user) {
    UsersDaoImp uDao = new UsersDaoImp();
    Console c = System.console();
    String input = null;
    Float amt = null;
    do {
      try {
      
        input = c.readLine(WITHDRAW_AMT);
        if(input.equalsIgnoreCase("b")) {
          return; //go back to menu
        }
        amt = Float.parseFloat(input);
      }
      catch(NumberFormatException e) { 
        amt = null; 
      }
    }
    while(!validAmount(amt));
    try {
      uDao.updateUser(con, user, amt);
    }
    catch(SQLException e) {
      System.out.println(E_DEPOSIT);
    }
  }

  public static void withdraw(Connection con, User user) {
    UsersDaoImp uDao = new UsersDaoImp();
    Console c = System.console();
    String input = null;
    Float amt = null; 
    do {
      try {
        input = c.readLine(WITHDRAW_AMT);
        if(input.equalsIgnoreCase("b")) {
          return; //go back to menu
        }
        amt = Float.parseFloat(input);
      }
      catch(NumberFormatException e) { 
        amt = null; 
      }
    }
    while(!validAmount(amt));
    try {
      uDao.updateUser(con, user, (-1)*amt); 
    }
    catch(SQLException e) {
      System.out.println(E_WITHDRAW); 
    }
  }

  public static boolean validAmount(Float amt) {
    if(amt == null) {
      System.out.println("Enter a number");
      return false;
    }
    else if(amt < 0) {
      System.out.println("please enter a positive number");
      return false;
    }
    return true;
  }
}
*/