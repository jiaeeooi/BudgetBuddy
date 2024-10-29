package application.controller;

//This is the controller file, this handles the behavior of the fxml page for NewAccount 
//You can think of fxml being the front end and the controller files for being the backend for this GUI application
//css file is for styling obviously 


import javafx.fxml.FXML;
import javafx.scene.control.Button; //import all the fxml types for use here
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import application.Database;

public class NewAccountController {
	
	@FXML //this is necessary if you want javaFX to inject the corresponding UI componenet from the FXML file 
	private TextField accountNameField; //link the fxml components to the controller 
	
	@FXML
	private DatePicker openDateField; // format: visibility, fxml type, fx:id
	
	@FXML
	private TextField openingBalanceField;
	
	@FXML
	private Button submitButton;
	
	@FXML
	private Button cancelButton; 
	
	@FXML
	public void initialize() {
		openDateField.setValue(LocalDate.now());
	}
	
	@FXML 
	private void handleSubmit() {
		String accountName = accountNameField.getText();
		LocalDate openingDate = openDateField.getValue();
		double openingBalance; 
		
		 if (accountName.isEmpty() || openingDate == null || openingBalanceField.getText().isEmpty()) {
	         // Show an error alert if fields are empty
			 showAlert(AlertType.ERROR, "Input Validation Failed", "Please fill in all required fields.");
			 return;
	     } 
		 
		 try {
			 openingBalance = Double.parseDouble(openingBalanceField.getText());
		 } catch (NumberFormatException e) {
			 showAlert(AlertType.ERROR, "Invalid Balance", "Opening balance must be a valid number.");
	         return;
		 }
		 
		 if (openingBalance < 0) {
			 showAlert(AlertType.ERROR, "Invalid Balance", "Opening balance must be greater or equal to 0.");
			 return;
	     }
		 
		 // Round the opening balance to 2 decimal places
		 openingBalance = Math.round(openingBalance * 100.0) / 100.0;
	     
	     // Check for duplicate account name
	     if (isDuplicateAccount(accountName)) {
	    	 showAlert(AlertType.ERROR, "Duplicate Account Name", "An account with this name already exists.");
	         return;
	     }
	     
	     insertAccount(accountName, openingBalance, openingDate);
	     
	     // Show success message
	     showAlert(AlertType.INFORMATION, "Account Created", "The account was successfully created.");
	
	}
	
	@FXML
	private void handleCancel() {
        // Clear the fields when Cancel is clicked
        accountNameField.clear();
        openDateField.setValue(LocalDate.now());
        openingBalanceField.clear();
    }
	
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void insertAccount(String accountName, double openingBalance, LocalDate openingDate) {
        String sql = "INSERT INTO Accounts(name, balance, opening) VALUES(?, ?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountName);
            pstmt.setDouble(2, openingBalance);
            pstmt.setString(3, openingDate.toString());
            pstmt.executeUpdate();
            System.out.println("Account inserted");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showAlert(AlertType.ERROR, "Database Error", "Could not save the account to the database.");
        }
    }
    
    private boolean isDuplicateAccount(String accountName) {
        String sql = "SELECT COUNT(*) FROM Accounts WHERE name = ?";
        try (Connection conn = Database.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if account exists
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showAlert(AlertType.ERROR, "Database Error", "Could not check for duplicate account.");
        }
        return false; // Returns false if there was an error or account does not exist
    }    
}

