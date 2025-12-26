package driver;

import models.User;
import utils.Bank;
import utils.Connect;

import java.sql.Connection;
import java.sql.SQLException;//entry point of the application
import java.util.Scanner;

//displays splash menu, handles user input
  /*public static void main(String ... args) throws SQLException {
    boolean done = false;
    while(!done) {
      Console c = System.console();
      Bank.showSplashMenu();
      String selection = c.readLine();
      switch (selection) {
        case "1":
          Bank.createUserAccount();
          break;
        case "2":

          doActions();
          break;
        case "3":
          //log.info("bye");
          System.out.println("bye");



          done = true;
          break;
        //default: log.info("What would you like to do?");
        //System.out.println("What would you like to do?");
      }
    }
  }

  */
public class BankDriver {
  //private Connection connect = Connect.getConnection();
  public static void main(String... args) throws SQLException {
    boolean done = false;
    Scanner scanner = new Scanner(System.in);

    while (!done) {
      Bank.showSplashMenu();
      String selection = scanner.nextLine();

      switch (selection) {
        case "1":
          Bank.createUserAccount();
          break;
        case "2":
          doActions();
          break;
        case "3":
          System.out.println("bye");
          done = true;
          break;
        default:
          System.out.println("Invalid option. Try again.");
      }
    }

    scanner.close();
  }


  public static void doActions() {
    String username = Bank.readUsername();
    String password = Bank.readPassword();
    try (Connection con = Connect.getConnection()) {
      User user = Bank.login(con, username, password);
      if(user == null) {
        //log.info(Bank.BAD_LOGIN);
        System.out.println(Bank.BAD_LOGIN);
      }

      while(user != null) {
        Bank.showLoggedinMenu();
        int selection = Bank.getSelection();
        switch (selection) {
          case 1: //deposit
            Bank.deposit(con, user);
            break;
          case 2: //withdraw
            Bank.withdraw(con, user);
            break;
          case 3: //view balance
            Bank.view(con, user);
            break;
          case 4: //logout
            user = null;
            break;
          default:
            //log.info("Invalid selection");
            System.out.println("Invalid selection");
        }
      }
    } //end loggedinMenu
    catch(SQLException e) {
      //log.info("Connection failed");
      System.out.println("Connection failed");
    }

  }
}