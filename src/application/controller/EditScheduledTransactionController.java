package application.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.CommonObjs;
import application.Database;
import application.ScheduledTransaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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
		String scheduleName = scheduleNameField.getText();
		String account = accountDropdown.getValue();
        String transactionType = transactionTypeDropdown.getValue();
        String frequency = frequencyDropdown.getValue();
        String dueDateText = dueDateField.getText();
        String paymentAmountText = paymentAmountField.getText();
        
        
        if (scheduleName.isEmpty() || account == null || transactionType == null || frequency == null || dueDateText.isEmpty() || paymentAmountText.isEmpty()) {
	        showAlert(Alert.AlertType.ERROR, "Input Validation Failed", "Please fill in all required fields.");
	        return;
	    }

        if (!scheduleName.equals(scheduledTransactionToEdit.getScheduleName()) && isDuplicateScheduleName(scheduleName)) {
	    	 showAlert(AlertType.ERROR, "Duplicate Schedule's Name", "A schedule with this name already exists.");
	         return;
	    }
        
        int dueDate;
	    try {
	        dueDate = Integer.parseInt(dueDateText);
	    } catch (NumberFormatException e) {
	        showAlert(Alert.AlertType.ERROR, "Invalid Due Date", "Due date must be a valid integer.");
	        return;
	    }
        
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
        
        updateScheduledTransaction(scheduledTransactionToEdit.getId(), account, transactionType, frequency, dueDate, scheduleName, paymentAmount);
        
        showAlert(AlertType.INFORMATION, "Scheduled Transaction Updated", "The scheduled transaction was successfully updated.");
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
	
	private void updateScheduledTransaction(int id, String account, String transactionType, String frequency, int dueDate, String scheduleName, double paymentAmount) {
        String sql = "UPDATE ScheduledTransactions SET account_name = ?, transaction_type = ?, frequency = ?, due_date = ?, schedule_name = ?, payment_amount = ? WHERE id = ?";
        try (Connection conn = Database.connect(ScheduledTransactionsURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, account);
            pstmt.setString(2, transactionType);
            pstmt.setString(3, frequency);
            pstmt.setInt(4, dueDate);
            pstmt.setString(5, scheduleName);
            pstmt.setDouble(6, paymentAmount);
            pstmt.setInt(7, id);
            pstmt.executeUpdate();

            System.out.println("Scheduled Transaction updated");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showAlert(AlertType.ERROR, "Database Error", "Could not update the scheduled transaction in the database.");
        }
    }
	
	@FXML
	private void handleBack() {
		try {
            // Load ViewSearchedScheduledTransaction.fxml
            URL url = getClass().getClassLoader().getResource("view/ViewSearchedScheduledTransaction.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            AnchorPane pane = loader.load();

            // Get the controller instance for ViewSearchedScheduledTransactionController
            ViewSearchedScheduledTransactionController controller = loader.getController();

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
