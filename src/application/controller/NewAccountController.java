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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
	
	private List<String> accountNames = new ArrayList<>();
	
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
		 
		 if (openingBalance <= 0) {
			 showAlert(AlertType.ERROR, "Invalid Balance", "Opening balance must be greater than 0.");
			 return;
	     }
	     
	     // Check for duplicate account name
	     if (accountNames.contains(accountName)) {
	    	 showAlert(AlertType.ERROR, "Duplicate Account Name", "An account with this name already exists.");
	         return;
	     }
	     
	     accountNames.add(accountName);
	     
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
}

