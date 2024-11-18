package application.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import application.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class NewTransactionController {

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
	private Button cancelButton;
	
	private static final String TransactionsURL = "jdbc:sqlite:db/Transactions.db";
	
	private String firstAccountName;
	private String firstTransactionType;
	
	@FXML
	public void initialize() {
		// Load account names into the dropdown
		List<String> accountList = Database.getAccountNames();
        accountDropdown.getItems().addAll(accountList);
        // Set the default value to the first item if available
        if (!accountList.isEmpty()) {
            firstAccountName = accountList.get(0);
        	accountDropdown.setValue(firstAccountName);
        }
        
        // Load transaction types into the dropdown
        List<String> transactionTypeList = Database.getTransactionTypes();
        transactionTypeDropdown.getItems().addAll(transactionTypeList);
        // Set the default value to the first item if available
        if (!transactionTypeList.isEmpty()) {
            firstTransactionType = transactionTypeList.get(0);
        	transactionTypeDropdown.setValue(firstTransactionType);
        }
        
		transactionDateField.setValue(LocalDate.now());
	}
	
	@FXML
	private void handleCancel() {
		// Reset dropdowns to their default values
        accountDropdown.setValue(firstAccountName);
        transactionTypeDropdown.setValue(firstTransactionType);
        
        // Clear the fields when Cancel is clicked
        transactionDateField.setValue(LocalDate.now());
        transactionDescriptionField.clear();
        paymentAmountField.clear();
        depositAmountField.clear();
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
	    
	    if (isPaymentProvided) {
	        try {
	            paymentAmount = Double.parseDouble(paymentAmountField.getText());
	        } catch (NumberFormatException e) {
	            showAlert(AlertType.ERROR, "Invalid Payment Amount", "Payment amount must be a valid number.");
	            return;
	        }
	    }
	    
	    if (isDepositProvided) {
	        try {
	            depositAmount = Double.parseDouble(depositAmountField.getText());
	        } catch (NumberFormatException e) {
	            showAlert(AlertType.ERROR, "Invalid Deposit Amount", "Deposit amount must be a valid number.");
	            return;
	        }
	    }
	    
	    if (!isPaymentProvided && !isDepositProvided) {
	        showAlert(AlertType.ERROR, "Input Validation Failed", "Either Payment Amount or Deposit Amount must be provided.");
	        return;
	    }
	    
		if (paymentAmount < 0) {
			showAlert(AlertType.ERROR, "Invalid Balance", "Payment Amount must be greater or equal to 0.");
			return;
	    }
		 
		if (depositAmount < 0) {
			showAlert(AlertType.ERROR, "Invalid Balance", "Deposit Amount must be greater or equal to 0.");
			return;
	    }
	    
	    if (isPaymentProvided) {
	        paymentAmount = Math.round(paymentAmount * 100.0) / 100.0;
	    }
	    
	    if (isDepositProvided) {
	        depositAmount = Math.round(depositAmount * 100.0) / 100.0;
	    }
	    
	    insertTransaction(account, transactionType, transactionDate, description, paymentAmount, depositAmount);
	    
	    showAlert(AlertType.INFORMATION, "Transaction Saved", "The transaction was successfully saved.");
	
	}
	
	private void insertTransaction(String account, String transactionType, LocalDate transactionDate, String description, double paymentAmount, double depositAmount) {
		String sql = "INSERT INTO Transactions(account_name, transaction_type, transaction_date, description, payment_amount, deposit_amount) VALUES(?, ?, ?, ?, ?, ?)";
	    try (Connection conn = Database.connect(TransactionsURL); // Assume you have defined TransactionsURL
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, account);
	        pstmt.setString(2, transactionType);
	        pstmt.setString(3, transactionDate.toString());
	        pstmt.setString(4, description);
	        pstmt.setDouble(5, paymentAmount);
	        pstmt.setDouble(6, depositAmount);
	        pstmt.executeUpdate();
	        System.out.println("Transaction inserted");
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	        showAlert(AlertType.ERROR, "Database Error", "Could not save the transaction to the database.");
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
