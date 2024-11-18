package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException; 
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {

	private static final String AccountsURL = "jdbc:sqlite:db/Accounts.db";
	private static final String TransactionTypesURL = "jdbc:sqlite:db/TransactionTypes.db";
	private static final String TransactionsURL = "jdbc:sqlite:db/Transactions.db";
	private static final String ScheduledTransactionsURL = "jdbc:sqlite:db/ScheduledTransactions.db";

	
	public static Connection connect(String url) {  //this allows us to connect to SQLite
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC"); 
			conn = DriverManager.getConnection(url); 
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("JDBC Driver not found ");
			e.printStackTrace();
		}
		return conn; 
	}
	
	public static void initializeAccountsDatabase() {
		String sql = "CREATE TABLE IF NOT EXISTS Accounts (\n" //Create the table and set up the columns
		        + "    id INTEGER PRIMARY KEY AUTOINCREMENT, \n" // Auto-incrementing primary key
				+ "		name TEXT NOT NULL, \n"  //Use string adding for formatting you can write it as a whole string if you want 
				+ " 	balance REAL NOT NULL, \n"
				+ "		opening TEXT NOT NULL \n"
				+ ");"; 
		try (Connection conn = connect(AccountsURL); 
			 Statement stmt = conn.createStatement()){
			stmt.execute(sql); //Execute the statement to create the table 
		} catch (SQLException e) {
			System.out.println(e.getMessage()); 
		}
	}
	
	public static void initializeTransactionTypesDatabase() {
		String sql = "CREATE TABLE IF NOT EXISTS TransactionTypes (\n" //Create the table and set up the column
		        + "    id INTEGER PRIMARY KEY AUTOINCREMENT, \n" // Auto-incrementing primary key
				+ "		type TEXT NOT NULL \n"  
				+ ");"; 
		try (Connection conn = connect(TransactionTypesURL); 
			 Statement stmt = conn.createStatement()){
			stmt.execute(sql); //Execute the statement to create the table 
		} catch (SQLException e) {
			System.out.println(e.getMessage()); 
		}
	}
	
	public static void initializeTransactionsDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS Transactions (\n"
                + "    id INTEGER PRIMARY KEY AUTOINCREMENT, \n"
                + "    account_name TEXT NOT NULL, \n"
                + "    transaction_type TEXT NOT NULL, \n"
                + "    transaction_date TEXT NOT NULL, \n"
                + "    description TEXT NOT NULL, \n"
                + "    payment_amount REAL, \n"
                + "    deposit_amount REAL \n"
                + ");"; 
        try (Connection conn = connect(TransactionsURL); 
             Statement stmt = conn.createStatement()){
            stmt.execute(sql); 
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
    }
	
	public static void initializeScheduledTransactionsDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS ScheduledTransactions (\n"
                + "    id INTEGER PRIMARY KEY AUTOINCREMENT, \n"
                + "    schedule_name TEXT NOT NULL, \n"
                + "    account_name TEXT NOT NULL, \n"
                + "    transaction_type TEXT NOT NULL, \n"
                + "    frequency TEXT NOT NULL, \n"
                + "    due_date INTEGER NOT NULL, \n"
                + "    payment_amount REAL NOT NULL \n"
                + ");"; 
        try (Connection conn = connect(ScheduledTransactionsURL); 
             Statement stmt = conn.createStatement()){
            stmt.execute(sql); 
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        }
    }
	
	// Method to retrieve account names from the database
    public static List<String> getAccountNames() {
        List<String> accountNames = new ArrayList<>();
        String query = "SELECT name FROM Accounts"; // Adjust the table name if necessary

        try (Connection conn = connect(AccountsURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                accountNames.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return accountNames;
    }
    
    // Method to retrieve transaction types from the database
    public static List<String> getTransactionTypes() {
        List<String> transactionTypes = new ArrayList<>();
        String query = "SELECT type FROM TransactionTypes"; // Adjust the table name if necessary

        try (Connection conn = connect(TransactionTypesURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                transactionTypes.add(rs.getString("type"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return transactionTypes;
    }
}