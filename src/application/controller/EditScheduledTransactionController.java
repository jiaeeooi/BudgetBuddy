package application.controller;

import application.CommonObjs;
import application.Database;
import application.ScheduledTransaction;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class EditScheduledTransactionController {

	private CommonObjs commonObjs = CommonObjs.getInstance();
	
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
	private Button backButton;
	
	private String previousSearchQuery;
	
	private ScheduledTransaction scheduledTransactionToEdit;
	
	private static final String ScheduledTransactionsURL = "jdbc:sqlite:db/ScheduledTransactions.db";
	
	@FXML 
	public void initialize() {
		accountDropdown.getItems().addAll(Database.getAccountNames());
        transactionTypeDropdown.getItems().addAll(Database.getTransactionTypes());
        frequencyDropdown.getItems().add("Monthly");
	}
	
	public void setScheduledTransactionData(ScheduledTransaction scheduledTransaction) {
        this.scheduledTransactionToEdit = scheduledTransaction;
        
        scheduleNameField.setText(scheduledTransaction.getScheduleName());
        accountDropdown.setValue(scheduledTransaction.getAccount());
        transactionTypeDropdown.setValue(scheduledTransaction.getTransactionType());
        frequencyDropdown.setValue(scheduledTransaction.getFrequency());
        dueDateField.setText(String.valueOf(scheduledTransaction.getDueDate()));
        paymentAmountField.setText(String.valueOf(scheduledTransaction.getPaymentAmount()));
    }
	
	public void setPreviousSearchQuery(String searchQuery) {
	    this.previousSearchQuery = searchQuery;
	}
	
	@FXML
	private void handleSubmit() {
		
	}
	
	@FXML
	private void handleBack() {
		
	}
	
	private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
