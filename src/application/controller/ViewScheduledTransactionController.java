package application.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.Database;
import application.ScheduledTransaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ViewScheduledTransactionController {
	
	@FXML
    private TableView<ScheduledTransaction> scheduledTransactionTable;
	
	@FXML
    private TableColumn<ScheduledTransaction, String> scheduleNameColumn;
    
    @FXML
    private TableColumn<ScheduledTransaction, String> accountColumn;
    
    @FXML
    private TableColumn<ScheduledTransaction, String> transactionTypeColumn;
    
    @FXML
    private TableColumn<ScheduledTransaction, String> frequencyColumn;
    
    @FXML
    private TableColumn<ScheduledTransaction, Integer> dueDateColumn;
    
    @FXML
    private TableColumn<ScheduledTransaction, Double> paymentAmountColumn;
    
    private static final String ScheduledTransactionsURL = "jdbc:sqlite:db/ScheduledTransactions.db";
    
    @FXML
    public void initialize() {
        // Set up cell value factories for the TableView columns
        scheduleNameColumn.setCellValueFactory(new PropertyValueFactory<>("scheduleName"));
        accountColumn.setCellValueFactory(new PropertyValueFactory<>("account"));
        transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        frequencyColumn.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        paymentAmountColumn.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));

        // Set a placeholder message for the TableView
        scheduledTransactionTable.setPlaceholder(new Label("No content in table"));

        loadScheduledTransactions();
    }
    
    private void loadScheduledTransactions() {
        ObservableList<ScheduledTransaction> scheduledTransactions = FXCollections.observableArrayList();

        String query = "SELECT * FROM ScheduledTransactions ORDER BY due_date DESC";

        try (Connection conn = Database.connect(ScheduledTransactionsURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Check if ResultSet has any rows
            if (!rs.isBeforeFirst()) {
                return; // Exit if no records are found
            }

            // Populate the scheduledTransactions list with data from ResultSet
            while (rs.next()) {
                ScheduledTransaction scheduledTransaction = new ScheduledTransaction(
                        rs.getString("schedule_name"),
                        rs.getString("account_name"),
                        rs.getString("transaction_type"),
                        rs.getString("frequency"),
                        rs.getInt("due_date"),
                        rs.getDouble("payment_amount")
                );
                scheduledTransactions.add(scheduledTransaction);
            }

            // Set the items in the TableView
            scheduledTransactionTable.setItems(scheduledTransactions);

        } catch (SQLException e) {
            System.out.println("Error fetching scheduled transactions: " + e.getMessage());
        }
    }
}
