package application.controller;

import java.io.IOException;
import java.net.URL;

import application.CommonObjs;
import application.Transaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class ReadTransactionByAccountController {

	private CommonObjs commonObjs = CommonObjs.getInstance();
	
	@FXML
	private TextField accountDropdown;

	@FXML
	private TextField transactionTypeDropdown;
	
	@FXML
	private TextField transactionDateField;
	
	@FXML
	private TextField transactionDescriptionField;
	
	@FXML
	private TextField paymentAmountField;
	
	@FXML
	private TextField depositAmountField;
	
	private String previousAccountText;
	
	@FXML
	private Button backButton;
	
	@FXML
    public void initialize() {
        // Set all the fields to be non-editable
        accountDropdown.setEditable(false);
        transactionTypeDropdown.setEditable(false);
        transactionDateField.setEditable(false);
        transactionDescriptionField.setEditable(false);
        paymentAmountField.setEditable(false);
        depositAmountField.setEditable(false);
    }
	
	public void setTransactionData(Transaction transaction) {
		accountDropdown.setText(transaction.getAccount());
        transactionTypeDropdown.setText(transaction.getTransactionType());
        transactionDateField.setText(transaction.getTransactionDate());
        transactionDescriptionField.setText(transaction.getTransactionDescription());
        paymentAmountField.setText(String.valueOf(transaction.getPaymentAmount()));
        depositAmountField.setText(String.valueOf(transaction.getDepositAmount()));
	}

	public void setPreviousAccountText(String accountText) {
		this.previousAccountText = accountText;
	}
	
	@FXML
	private void handleBack() {
		try {
	        URL url = getClass().getClassLoader().getResource("view/AccountReportPage.fxml");
	        FXMLLoader loader = new FXMLLoader(url);
	        AnchorPane pane = loader.load();

	        AccountReportPageController controller = loader.getController();
	        controller.initializeReport(previousAccountText);

	        HBox mainBox = commonObjs.getMainBox();
	        if (mainBox.getChildren().size() > 1) {
	            mainBox.getChildren().remove(1);
	        }
	        mainBox.getChildren().add(pane);

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
