package application.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.Database;
import application.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ViewTransactionController {

	@FXML
    private TableView<Transaction> transactionTable;
    
    @FXML
    private TableColumn<Transaction, String> accountColumn;

    @FXML
    private TableColumn<Transaction, String> transactionTypeColumn;
    
    @FXML
    private TableColumn<Transaction, String> transactionDateColumn;
    
    @FXML
    private TableColumn<Transaction, String> transactionDescriptionColumn;
    
    @FXML
    private TableColumn<Transaction, Double> paymentAmountColumn;
    
    @FXML
    private TableColumn<Transaction, Double> depositAmountColumn;
    
    private static final String TransactionsURL = "jdbc:sqlite:db/Transactions.db";
    
    @FXML
    public void initialize() {
        // Set up cell value factories for the TableView columns
        accountColumn.setCellValueFactory(new PropertyValueFactory<>("account"));
        transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        transactionDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDescription"));
        paymentAmountColumn.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        depositAmountColumn.setCellValueFactory(new PropertyValueFactory<>("depositAmount"));

        // Set a placeholder message for the TableView
        transactionTable.setPlaceholder(new Label("No content in table"));
        
        loadTransactions();
    }
    
    private void loadTransactions() {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();

        String query = "SELECT * FROM Transactions ORDER BY transaction_date DESC";

        try (Connection conn = Database.connect(TransactionsURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Check if ResultSet has any rows
            if (!rs.isBeforeFirst()) {
                return; // Exit if no records are found
            }

            // Populate the transactions list with data from ResultSet
            while (rs.next()) {
                Transaction transaction = new Transaction(
                        rs.getString("account_name"),
                        rs.getString("transaction_type"),
                        rs.getString("transaction_date"),
                        rs.getString("description"),
                        rs.getDouble("payment_amount"),
                        rs.getDouble("deposit_amount")
                );
                transactions.add(transaction);
            }

            // Set the items in the TableView
            transactionTable.setItems(transactions);

        } catch (SQLException e) {
            System.out.println("Error fetching transactions: " + e.getMessage());
        }
    }

}
