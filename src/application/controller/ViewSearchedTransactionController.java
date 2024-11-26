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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class ViewSearchedTransactionController {

	private CommonObjs commonObjs = CommonObjs.getInstance();

	@FXML
	private Button backButton;
	
	@FXML
    private TableView<Transactionable> searchedTransactionTable;
	
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
	
    private String searchText;
    
    @FXML
    public void initialize() {
        // Set up cell value factories
        accountColumn.setCellValueFactory(new PropertyValueFactory<>("account"));
        transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        transactionDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDescription"));
        paymentAmountColumn.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        depositAmountColumn.setCellValueFactory(new PropertyValueFactory<>("depositAmount"));

        // Add click listener
        searchedTransactionTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double click
                Transactionable selectedTransaction = searchedTransactionTable.getSelectionModel().getSelectedItem();
                if (selectedTransaction != null) {
                    openTransactionEditor(selectedTransaction);
                }
            }
        });
    }
    
    private void openTransactionEditor(Transactionable transaction) {
        try {
            URL url = getClass().getClassLoader().getResource("view/EditTransaction.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            AnchorPane editorPane = loader.load();

            // Pass transaction data to editor controller
            EditTransactionController controller = loader.getController();
            controller.setTransactionData(transaction);
            controller.setPreviousSearchQuery(searchText);
            
            HBox mainBox = commonObjs.getMainBox();
            if (mainBox.getChildren().size() > 1) {
                mainBox.getChildren().remove(1);
            }
            mainBox.getChildren().add(editorPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void setSearchQuery(String searchText) {
	   
    	this.searchText = searchText;
    	
    	ObservableList<Transactionable> searchResults = FXCollections.observableArrayList();

    	String query = "SELECT * FROM Transactions WHERE LOWER(description) LIKE ? ORDER BY transaction_date DESC";

    	try (Connection conn = Database.connect(TransactionsURL);
            PreparedStatement pstmt = conn.prepareStatement(query)) {

           pstmt.setString(1, "%" + searchText + "%");
           ResultSet rs = pstmt.executeQuery();

           while (rs.next()) {
               Transaction transaction = new Transaction(
                       rs.getInt("id"),
            		   rs.getString("account_name"),
                       rs.getString("transaction_type"),
                       rs.getString("transaction_date"),
                       rs.getString("description"),
                       rs.getDouble("payment_amount"),
                       rs.getDouble("deposit_amount")
               );
               searchResults.add(transaction);
           }

           searchedTransactionTable.setItems(searchResults);

       } catch (SQLException e) {
           System.out.println("Error during search: " + e.getMessage());
       }
   	}
    
    
    @FXML
	private void handleBack() {
    	URL url = getClass().getClassLoader().getResource("view/ViewTransaction.fxml");
		try {
			HBox mainBox = commonObjs.getMainBox();
			AnchorPane pane1 = (AnchorPane) FXMLLoader.load(url);
			if (mainBox.getChildren().size() > 1) {
				mainBox.getChildren().remove(1);
			}
			mainBox.getChildren().add(pane1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
