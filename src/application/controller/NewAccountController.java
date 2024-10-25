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
	
	@FXML  //the @FXML annotation is neccessary to make javaFX aware of the handle submit as an event handler 
	private void handleSubmit() {
		String accountName = accountNameField.getText();
		LocalDate openingDate = openDateField.getValue();
		int openingBalance = Integer.parseInt(openingBalanceField.getText()); //since we are using a text field, convert the string into an int 
		
		 if (accountName.isEmpty() || openingDate == null || openingBalance == 0) {
	            // Show an error alert if fields are empty
	            Alert alert = new Alert(AlertType.ERROR);
	            alert.setTitle("Error");
	            alert.setHeaderText("Input Validation Failed");
	            alert.setContentText("Please fill in all required fields and ensure your beginning deposit is not 0.");
	            alert.showAndWait();
	        } 
		 
		 else {
	            // Store or process user data (for now, just print it)
	            System.out.println("Account Name: " + accountName);
	            System.out.println("Opening Date: " + openingDate);
	            System.out.println("Opening Balance: " + openingBalance);
	        }

	
	
	}
	
	@FXML
	private void handleCancel() {
        // Clear the fields when Cancel is clicked
        accountNameField.clear();
        openDateField.setValue(null);
        openingBalanceField.clear();
    }
}

