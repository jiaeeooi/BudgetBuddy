package application.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import application.CommonObjs;
import application.Database;
import application.Transaction;
import application.Transactionable;

public class EditTransactionController {

	private CommonObjs commonObjs = CommonObjs.getInstance();
	
	@FXML
	private ChoiceBox<String> accountDropdown;

	@FXML
	private ChoiceBox<String> transactionTypeDropdown;
	
	@FXML
	private DatePicker transactionDateField;
	
	@FXML
	private TextField transactionDescriptionField;
	
	@FXML
	private TextField paymentAmountField;
	
	@FXML
	private TextField depositAmountField;
	
	@FXML
	private Button submitButton;
	
	@FXML
	private Button backButton;
	
	private String previousSearchQuery;
	
	private Transactionable transactionToEdit;
	
	private static final String TransactionsURL = "jdbc:sqlite:db/Transactions.db";
	
	@FXML 
	public void initialize() {
		accountDropdown.getItems().addAll(Database.getAccountNames());
        transactionTypeDropdown.getItems().addAll(Database.getTransactionTypes());
	}
	
	public void setTransactionData(Transactionable transaction) {
		this.transactionToEdit = transaction;
		
		if (transactionToEdit instanceof Transaction) {
	        Transaction trans = (Transaction) transactionToEdit;
	        
	        // Pre-fill the fields with transaction data
	        accountDropdown.setValue(trans.getAccount());
	        transactionTypeDropdown.setValue(trans.getTransactionType());
	        transactionDateField.setValue(LocalDate.parse(trans.getTransactionDate()));
	        transactionDescriptionField.setText(trans.getTransactionDescription());
	        paymentAmountField.setText(String.valueOf(trans.getPaymentAmount()));
	        depositAmountField.setText(String.valueOf(trans.getDepositAmount()));
	    } else {
	        // Handle the case where it's not a Transaction
	        showAlert(AlertType.ERROR, "Invalid Transaction", "The provided transaction is not valid.");
	    }
	}
	
	public void setPreviousSearchQuery(String searchQuery) {
	    this.previousSearchQuery = searchQuery;
	}
	
	@FXML
	private void handleSubmit() {
		String account = accountDropdown.getValue();
	    String transactionType = transactionTypeDropdown.getValue();
	    LocalDate transactionDate = transactionDateField.getValue(); 
	    String description = transactionDescriptionField.getText(); 
	    double paymentAmount = 0.0;
	    double depositAmount = 0.0;
	    boolean isPaymentProvided = !paymentAmountField.getText().isEmpty();
	    boolean isDepositProvided = !depositAmountField.getText().isEmpty();
	    
	    if (account == null || transactionType == null || transactionDate == null || description.isEmpty()) {
	        showAlert(AlertType.ERROR, "Input Validation Failed", "Please fill in all required fields.");
	        return;
	    }
	    
	    try {
            if (isPaymentProvided) {
                paymentAmount = Double.parseDouble(paymentAmountField.getText());
            }
            if (isDepositProvided) {
                depositAmount = Double.parseDouble(depositAmountField.getText());
            }
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Invalid Amount", "Payment and deposit amounts must be valid numbers.");
            return;
        }
	    
	    if (!isPaymentProvided && !isDepositProvided) {
            showAlert(AlertType.ERROR, "Input Validation Failed", "Either Payment Amount or Deposit Amount must be provided.");
            return;
        }
	    
	    if (paymentAmount < 0 || depositAmount < 0) {
            showAlert(AlertType.ERROR, "Invalid Amount", "Amounts must be non-negative.");
            return;
        }
	    
	    if (isPaymentProvided) {
	        paymentAmount = Math.round(paymentAmount * 100.0) / 100.0;
	    }
	    
	    if (isDepositProvided) {
	        depositAmount = Math.round(depositAmount * 100.0) / 100.0;
	    }
	    
	    updateTransaction(transactionToEdit.getId(), account, transactionType, transactionDate, description, paymentAmount, depositAmount);
	    
	    showAlert(AlertType.INFORMATION, "Transaction Updated", "The transaction was successfully updated.");
	}
	
	private void updateTransaction(int id, String account, String transactionType, LocalDate transactionDate, String description, double paymentAmount, double depositAmount) {
		String sql = "UPDATE Transactions SET account_name = ?, transaction_type = ?, transaction_date = ?, description = ?, payment_amount = ?, deposit_amount = ? WHERE id = ?";
        try (Connection conn = Database.connect(TransactionsURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, account);
            pstmt.setString(2, transactionType);
            pstmt.setString(3, transactionDate.toString());
            pstmt.setString(4, description);
            pstmt.setDouble(5, paymentAmount);
            pstmt.setDouble(6, depositAmount);
            pstmt.setInt(7, id);
            pstmt.executeUpdate();

            System.out.println("Transaction updated");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showAlert(AlertType.ERROR, "Database Error", "Could not update the transaction in the database.");
        }
	}
	
	@FXML
	private void handleBack() {
		try {
	        // Load ViewSearchedTransaction.fxml
	        URL url = getClass().getClassLoader().getResource("view/ViewSearchedTransaction.fxml");
	        FXMLLoader loader = new FXMLLoader(url);
	        AnchorPane pane = loader.load();

	        // Get the controller instance for ViewSearchedTransactionController
	        ViewSearchedTransactionController controller = loader.getController();

	        // Pass the previous search query and any relevant data
	        if (previousSearchQuery != null && !previousSearchQuery.isEmpty()) {
	            controller.setSearchQuery(previousSearchQuery); // Restore the search query
	        }

	        // Replace the current view with the previous one
	        HBox mainBox = commonObjs.getMainBox();
	        if (mainBox.getChildren().size() > 1) {
	            mainBox.getChildren().remove(1);
	        }
	        mainBox.getChildren().add(pane);

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
