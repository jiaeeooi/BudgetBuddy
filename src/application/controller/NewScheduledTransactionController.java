package application.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import application.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class NewScheduledTransactionController {

	@FXML
	private TextField scheduleNameField;
	
	@FXML
	private ChoiceBox<String> accountDropdown;

	@FXML
	private ChoiceBox<String> transactionTypeDropdown;
	
	@FXML
	private ChoiceBox<String> frequencyDropdown;
	
	@FXML
	private TextField dueDateField;
	
	@FXML
	private TextField paymentAmountField;
	
	@FXML
	private Button submitButton;
	
	@FXML
	private Button cancelButton;
	
	private static final String ScheduledTransactionsURL = "jdbc:sqlite:db/ScheduledTransactions.db";
	
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
        
     // Set frequency dropdown with a single, hard-coded value "Monthly"
        frequencyDropdown.getItems().add("Monthly");
        frequencyDropdown.setValue("Monthly"); // Set "Monthly" as the default value
	}
	
	@FXML
	private void handleCancel() {
		scheduleNameField.clear();
        accountDropdown.setValue(firstAccountName);
        transactionTypeDropdown.setValue(firstTransactionType);
        frequencyDropdown.setValue("Monthly");
        dueDateField.clear();
        paymentAmountField.clear();
    }
	
	@FXML
	private void handleSubmit() {
		String scheduleName = scheduleNameField.getText();
	    String account = accountDropdown.getValue();
	    String transactionType = transactionTypeDropdown.getValue();
	    String frequency = frequencyDropdown.getValue();
	    String dueDate = dueDateField.getText();
	    String paymentAmountText = paymentAmountField.getText();
	    
	    if (scheduleName.isEmpty() || account == null || transactionType == null || frequency == null || dueDate.isEmpty() || paymentAmountText.isEmpty()) {
	        showAlert(Alert.AlertType.ERROR, "Input Validation Failed", "Please fill in all required fields.");
	        return;
	    }
	    
	    if (isDuplicateScheduleName(scheduleName)) {
	    	 showAlert(AlertType.ERROR, "Duplicate Schedule's Name", "A schedule with this name already exists.");
	         return;
	     }
	    
	    /* 
	    // This block of code is to check if the entered due date is a valid Integer
	    int dueDate;
	    try {
	        dueDate = Integer.parseInt(dueDateText);
	    } catch (NumberFormatException e) {
	        showAlert(Alert.AlertType.ERROR, "Invalid Due Date", "Due date must be a valid integer.");
	        return;
	    }
	    */
	    
	    double paymentAmount;
	    try {
	        paymentAmount = Double.parseDouble(paymentAmountText);
	    } catch (NumberFormatException e) {
	        showAlert(Alert.AlertType.ERROR, "Invalid Payment Amount", "Payment amount must be a valid number.");
	        return;
	    }
	    
	    if (paymentAmount < 0) {
	        showAlert(Alert.AlertType.ERROR, "Invalid Payment Amount", "Payment amount must be greater than or equal to 0.");
	        return;
	    }
	    
	    paymentAmount = Math.round(paymentAmount * 100.0) / 100.0;
	    
	    insertScheduledTransaction(scheduleName, account, transactionType, frequency, dueDate, paymentAmount);
	    
	    showAlert(Alert.AlertType.INFORMATION, "Scheduled Transaction Saved", "The scheduled transaction was successfully saved.");
	}
	
	private void insertScheduledTransaction(String scheduleName, String account, String transactionType, String frequency, String dueDate, double paymentAmount) {
	    String sql = "INSERT INTO ScheduledTransactions (schedule_name, account_name, transaction_type, frequency, due_date, payment_amount) VALUES (?, ?, ?, ?, ?, ?)";
	    try (Connection conn = Database.connect(ScheduledTransactionsURL); 
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, scheduleName);
	        pstmt.setString(2, account);
	        pstmt.setString(3, transactionType);
	        pstmt.setString(4, frequency);
	        pstmt.setString(5, dueDate);
	        pstmt.setDouble(6, paymentAmount);
	        pstmt.executeUpdate();                   
	        System.out.println("Scheduled transaction inserted");
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	        showAlert(Alert.AlertType.ERROR, "Database Error", "Could not save the scheduled transaction to the database.");
	    }
	}
	
	private boolean isDuplicateScheduleName(String scheduleName) {
	    String sql = "SELECT COUNT(*) FROM ScheduledTransactions WHERE schedule_name = ?";
	    
	    try (Connection conn = Database.connect(ScheduledTransactionsURL); 
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, scheduleName);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) > 0; // Returns true if a schedule with this name exists
	        }
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	        showAlert(Alert.AlertType.ERROR, "Database Error", "Could not check for duplicate schedule name.");
	    }
	    return false; // Returns false if there was an error or schedule name does not exist
	}

	private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
