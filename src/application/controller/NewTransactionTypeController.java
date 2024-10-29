package application.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class NewTransactionTypeController {

	@FXML
	private TextField transactionTypeNameField;
	
	@FXML
	private Button submitButton;
	
	@FXML
	private Button cancelButton; 
	
	private static final String TransactionTypesURL = "jdbc:sqlite:db/TransactionTypes.db";
	
	@FXML 
	private void handleSubmit() {
		String transactionTypeName = transactionTypeNameField.getText();
		
		 if (transactionTypeName.isEmpty()) {
	         // Show an error alert if fields are empty
			 showAlert(AlertType.ERROR, "Input Validation Failed", "Please fill in all required fields.");
			 return;
	     } 
	     
	     // Check for duplicate account name
	     if (isDuplicateTransactionType(transactionTypeName)) {
	    	 showAlert(AlertType.ERROR, "Duplicate Transaction Type Name", "A transaction type with this name already exists.");
	         return;
	     }
	     
	     insertTransactionType(transactionTypeName);
	     
	     // Show success message
	     showAlert(AlertType.INFORMATION, "Transaction Type Created", "The transaction type was successfully created.");
	
	}
	
	
	private boolean isDuplicateTransactionType(String transactionTypeName) {
		String sql = "SELECT COUNT(*) FROM TransactionTypes WHERE type = ?";
        try (Connection conn = Database.connect(TransactionTypesURL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, transactionTypeName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if account exists
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showAlert(AlertType.ERROR, "Database Error", "Could not check for duplicate transaction type.");
        }
        return false; // Returns false if there was an error or transaction type does not exist
    }    	
	
	private void insertTransactionType(String transactionTypeName) {
		String sql = "INSERT INTO TransactionTypes(type) VALUES(?)";
        try (Connection conn = Database.connect(TransactionTypesURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, transactionTypeName);
            pstmt.executeUpdate();
            System.out.println("Transaction Type inserted");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showAlert(AlertType.ERROR, "Database Error", "Could not save the transaction type to the database.");
        }
    }

	@FXML
	private void handleCancel() {
        // Clear the field when Cancel is clicked
        transactionTypeNameField.clear();
    }
	
	private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
	
}
