package application.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.CommonObjs;
import application.Database;
import application.Transaction;
import application.Transactionable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class TransactionTypeReportPageController {

	private CommonObjs commonObjs = CommonObjs.getInstance();
	
	@FXML
    private TableView<Transactionable> transactionTypeReportTable;

    @FXML
    private TableColumn<Transactionable, String> accountColumn;

    @FXML
    private TableColumn<Transactionable, String> transactionTypeColumn;

    @FXML
    private TableColumn<Transactionable, String> transactionDateColumn;

    @FXML
    private TableColumn<Transactionable, String> transactionDescriptionColumn;

    @FXML
    private TableColumn<Transactionable, Double> paymentAmountColumn;

    @FXML
    private TableColumn<Transactionable, Double> depositAmountColumn;

    private static final String TransactionsURL = "jdbc:sqlite:db/Transactions.db";

    private String transactionTypeText;
	
    public void initialize() {
		accountColumn.setCellValueFactory(new PropertyValueFactory<>("account"));
        transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        transactionDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDescription"));
        paymentAmountColumn.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        depositAmountColumn.setCellValueFactory(new PropertyValueFactory<>("depositAmount"));
		
        // Add click listener
        transactionTypeReportTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double click
                Transactionable selectedTransaction = transactionTypeReportTable.getSelectionModel().getSelectedItem();
                if (selectedTransaction != null) {
                    openTransactionReader(selectedTransaction);
                }
            }
        });
	}
    
	private void openTransactionReader(Transactionable transaction) {
		try {
            URL url = getClass().getClassLoader().getResource("view/ReadTransactionByType.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            AnchorPane readerPane = loader.load();

            // Pass transaction data to editor controller
            ReadTransactionByTypeController controller = loader.getController();
            controller.setTransactionData(transaction);
            controller.setPreviousTransactionTypeText(transactionTypeText);
            
            HBox mainBox = commonObjs.getMainBox();
            if (mainBox.getChildren().size() > 1) {
                mainBox.getChildren().remove(1);
            }
            mainBox.getChildren().add(readerPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public void initializeReport(String selectedAccountOrType) {
		this.transactionTypeText = selectedAccountOrType;
		
		ObservableList<Transactionable> reportResults = FXCollections.observableArrayList();
		
		String query = "SELECT * FROM Transactions WHERE transaction_type = ? ORDER BY transaction_date DESC";
		 
		try (Connection conn = Database.connect(TransactionsURL);
			PreparedStatement pstmt = conn.prepareStatement(query)) {

			pstmt.setString(1, transactionTypeText);
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	        	Transactionable transaction = new Transaction(
	        			rs.getInt("id"),
	                    rs.getString("account_name"),
	                    rs.getString("transaction_type"),
	                    rs.getString("transaction_date"),
	                    rs.getString("description"),
	                    rs.getDouble("payment_amount"),
	                    rs.getDouble("deposit_amount")
	            );
	            reportResults.add(transaction);
	        }

	        transactionTypeReportTable.setItems(reportResults);

	    } catch (SQLException e) {
	        System.out.println("Error retrieving report: " + e.getMessage());
	    }
	}
}
