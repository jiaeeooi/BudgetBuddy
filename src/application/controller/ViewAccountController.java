package application.controller;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.Account;
import application.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ViewAccountController {
	
    @FXML
    private TableView<Account> accountTable;
    
    @FXML
    private TableColumn<Account, String> accountNameColumn;
    
    @FXML
    private TableColumn<Account, Double> openingBalanceColumn;
    
    @FXML
    private TableColumn<Account, String> openingDateColumn;
    
    private static final String AccountsURL = "jdbc:sqlite:db/Accounts.db";
    
    @FXML
    public void initialize() {
    	// Set up the columns
        accountNameColumn.setCellValueFactory(new PropertyValueFactory<>("accountName"));
        openingBalanceColumn.setCellValueFactory(new PropertyValueFactory<>("openingBalance"));        
        openingDateColumn.setCellValueFactory(new PropertyValueFactory<>("openingDate"));

        // Set a placeholder message for the TableView
        accountTable.setPlaceholder(new Label("No content in table"));
        
        // Fetch and display the accounts
        fetchAccounts();
    }
    
    private void fetchAccounts() {
    	ObservableList<Account> accounts = FXCollections.observableArrayList();

        String query = "SELECT * FROM Accounts ORDER BY opening DESC";

        try (Connection conn = Database.connect(AccountsURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Check if ResultSet has any rows
            if (!rs.isBeforeFirst()) {
                return; // Exit if no records are found
            }

            // Populate the accounts list with data from ResultSet
            while (rs.next()) {
                Account account = new Account(
                        rs.getString("name"),
                        rs.getDouble("balance"),
                        rs.getString("opening")
                );
                accounts.add(account);
            }

            // Set the items in the TableView
            accountTable.setItems(accounts);

        } catch (SQLException e) {
            System.out.println("Error fetching accounts: " + e.getMessage());
        }
    }
}
