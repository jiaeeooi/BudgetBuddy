package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import application.CommonObjs;
import application.Database;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert.AlertType;

public class ViewReportController {
	
	private CommonObjs commonObjs = CommonObjs.getInstance();
	
	@FXML
	private ChoiceBox<String> filterByDropdown;
	
	@FXML
	private ChoiceBox<String> accountOrTypeDropdown;
	
	@FXML
	private Button submitButton;

    // Called when the scene is initialized
    @FXML
    private void initialize() {
        // Populate the first ChoiceBox with options "Account" and "Transaction Type"
        filterByDropdown.getItems().addAll("Account", "Transaction Type");
        
        // Initially, clear the second dropdown
        accountOrTypeDropdown.getItems().clear();
        
        // Set up a listener for the filterByDropdown
        filterByDropdown.setOnAction(this::onFilterBySelected);
    }

    // This method is called when the user selects an option in the filterByDropdown
    private void onFilterBySelected(ActionEvent event) {
        String selectedFilter = filterByDropdown.getValue();

        // Clear the second dropdown before populating it based on the filter selection
        accountOrTypeDropdown.getItems().clear();

        // Update the second dropdown based on the selected filter
        if (selectedFilter.equals("Account")) {
            populateAccountDropdown();
        } else if (selectedFilter.equals("Transaction Type")) {
            populateTransactionTypeDropdown();
        }
    }

    // Populate the Account dropdown with available accounts from the database
    private void populateAccountDropdown() {
        List<String> accountNames = Database.getAccountNames();  // Fetch account names from your database
        accountOrTypeDropdown.setItems(FXCollections.observableArrayList(accountNames));
    }

    // Populate the Transaction Type dropdown with available transaction types from the database
    private void populateTransactionTypeDropdown() {
        List<String> transactionTypes = Database.getTransactionTypes();  // Fetch transaction types (strings) from your database
        accountOrTypeDropdown.setItems(FXCollections.observableArrayList(transactionTypes));
    }
	
	@FXML
	private void handleSubmit() {
		String selectedFilter = filterByDropdown.getValue();
        String selectedAccountOrType = accountOrTypeDropdown.getValue();
		
        // Check if both dropdowns have selections
        if (selectedFilter == null || selectedAccountOrType == null) {
        	showAlert(AlertType.ERROR, "Input Validation Failed", "Please fill in all required fields.");
            return;
        }
        
        // Load the report page and pass the selections to the next controller
        try {
            FXMLLoader loader;
            URL url;
        	
            if ("Account".equals(selectedFilter)) {
                url = getClass().getClassLoader().getResource("view/AccountReportPage.fxml");
            } else if ("Transaction Type".equals(selectedFilter)) {
                url = getClass().getClassLoader().getResource("view/TransactionTypeReportPage.fxml");
            } else {
                showAlert(AlertType.ERROR, "Invalid Filter", "The selected filter is not valid.");
                return;
            }
            
            loader = new FXMLLoader(url);
            AnchorPane reportPane = loader.load();
            
            // Get the controller of the loaded page and pass the selections
            if ("Account".equals(selectedFilter)) {
                AccountReportPageController controller = loader.getController();
                controller.initializeReport(selectedAccountOrType);
            } else {
                TransactionTypeReportPageController controller = loader.getController();
                controller.initializeReport(selectedAccountOrType);
            }

            // Update the main box with the report pane
            HBox mainBox = commonObjs.getMainBox();
            if (mainBox.getChildren().size() > 1) {
                mainBox.getChildren().remove(1);  // Remove existing content
            }
            mainBox.getChildren().add(reportPane);  // Add the new report pane
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
