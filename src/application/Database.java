package application;

import java.sql.Connection;
import java.sql.DriverManager; 
import java.sql.SQLException; 
import java.sql.Statement;

public class Database {

	private static final String AccountsURL = "jdbc:sqlite:db/Accounts.db";
	private static final String TransactionTypesURL = "jdbc:sqlite:db/TransactionTypes.db";
	
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
}