package application;

import java.sql.Connection;
import java.sql.DriverManager; 
import java.sql.SQLException; 
import java.sql.Statement;

public class Database {

	private static final String databaseURL = "jdbc:sqlite:db/Database.db";
	
	public static Connection connect() {  //this allows us to connect to SQLite
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC"); 
			conn = DriverManager.getConnection(databaseURL); 
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("JDBC Driver not found ");
			e.printStackTrace();
		}
		return conn; 
	}
	
	public static void initializeDatabase() {
		String sql = "CREATE TABLE IF NOT EXISTS bank (\n" //Create the table and set up the columns
		        + "    id INTEGER PRIMARY KEY AUTOINCREMENT, \n" // Auto-incrementing primary key
				+ "		name TEXT NOT NULL, \n"  //Use string adding for formatting you can write it as a whole string if you want 
				+ " 	balance REAL NOT NULL, \n"
				+ "		opening TEXT NOT NULL \n"
				+ ");"; 
		try (Connection conn = connect(); 
			 Statement stmt = conn.createStatement()){
			stmt.execute(sql); //Execute the statement to create the table 
		} catch (SQLException e) {
			System.out.println(e.getMessage()); 
		}
	}
}