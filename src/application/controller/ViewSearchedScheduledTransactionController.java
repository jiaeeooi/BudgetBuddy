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

public class ViewSearchedScheduledTransactionController {

	private CommonObjs commonObjs = CommonObjs.getInstance();

	@FXML
	private Button backButton;
	
	@FXML
    private TableView<ScheduledTransaction> searchedScheduledTransactionTable;
	
	@FXML
    private TableColumn<ScheduledTransaction, String> scheduleNameColumn;
    
    @FXML
    private TableColumn<ScheduledTransaction, String> accountColumn;
    
    @FXML
    private TableColumn<ScheduledTransaction, String> transactionTypeColumn;
    
    @FXML
    private TableColumn<ScheduledTransaction, String> frequencyColumn;
    
    @FXML
    private TableColumn<ScheduledTransaction, Integer> dueDateColumn;
    
    @FXML
    private TableColumn<ScheduledTransaction, Double> paymentAmountColumn;
    
    private static final String ScheduledTransactionsURL = "jdbc:sqlite:db/ScheduledTransactions.db";
    
    private String searchText;
    
    @FXML
    public void initialize() {
        // Set up cell value factories for the TableView columns
        scheduleNameColumn.setCellValueFactory(new PropertyValueFactory<>("scheduleName"));
        accountColumn.setCellValueFactory(new PropertyValueFactory<>("account"));
        transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        frequencyColumn.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        paymentAmountColumn.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));

        // Add click listener
        searchedScheduledTransactionTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-click
                ScheduledTransaction selectedScheduledTransaction = searchedScheduledTransactionTable.getSelectionModel().getSelectedItem();
                if (selectedScheduledTransaction != null) {
                    openScheduledTransactionEditor(selectedScheduledTransaction);
                }
            }
        });
    }
    
    private void openScheduledTransactionEditor(ScheduledTransaction scheduledTransaction) {
        try {
            URL url = getClass().getClassLoader().getResource("view/EditScheduledTransaction.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            AnchorPane editorPane = loader.load();

            // Pass scheduled transaction data to editor controller
            EditScheduledTransactionController controller = loader.getController();
            controller.setScheduledTransactionData(scheduledTransaction);
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
        
        ObservableList<ScheduledTransaction> searchResults = FXCollections.observableArrayList();

        String query = "SELECT * FROM ScheduledTransactions WHERE LOWER(schedule_name) LIKE ? ORDER BY due_date ASC";

        try (Connection conn = Database.connect(ScheduledTransactionsURL);
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, "%" + searchText + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ScheduledTransaction scheduledTransaction = new ScheduledTransaction(
                        rs.getString("schedule_name"),
                        rs.getString("account_name"),
                        rs.getString("transaction_type"),
                        rs.getString("frequency"),
                        rs.getInt("due_date"),
                        rs.getDouble("payment_amount")
                );
                searchResults.add(scheduledTransaction);
            }

            searchedScheduledTransactionTable.setItems(searchResults);

        } catch (SQLException e) {
            System.out.println("Error during search: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleBack() {
        URL url = getClass().getClassLoader().getResource("view/ViewScheduledTransaction.fxml");
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
