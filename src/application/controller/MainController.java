package application.controller;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class MainController {
	
	@FXML HBox mainBox;
	
	@FXML 
	public void defineNewAccountOp() {
		URL url = getClass().getClassLoader().getResource("view/NewAccount.fxml");
		try {
			AnchorPane pane1 = (AnchorPane) FXMLLoader.load(url);
			if (mainBox.getChildren().size() > 1) {
				mainBox.getChildren().remove(1);
			}
			mainBox.getChildren().add(pane1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void viewAccountOp() {
		URL url = getClass().getClassLoader().getResource("view/ViewAccount.fxml");
		try {
			AnchorPane pane1 = (AnchorPane) FXMLLoader.load(url);
			if (mainBox.getChildren().size() > 1) {
				mainBox.getChildren().remove(1);
			}
			mainBox.getChildren().add(pane1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void defineNewTransactionTypeOp() {
		URL url = getClass().getClassLoader().getResource("view/NewTransactionType.fxml");
		try {
			AnchorPane pane1 = (AnchorPane) FXMLLoader.load(url);
			if (mainBox.getChildren().size() > 1) {
				mainBox.getChildren().remove(1);
			}
			mainBox.getChildren().add(pane1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void defineNewTransactionOp() {
		URL url = getClass().getClassLoader().getResource("view/NewTransaction.fxml");
		try {
			AnchorPane pane1 = (AnchorPane) FXMLLoader.load(url);
			if (mainBox.getChildren().size() > 1) {
				mainBox.getChildren().remove(1);
			}
			mainBox.getChildren().add(pane1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void viewTransactionOp() {
		URL url = getClass().getClassLoader().getResource("view/ViewTransaction.fxml");
		try {
			AnchorPane pane1 = (AnchorPane) FXMLLoader.load(url);
			if (mainBox.getChildren().size() > 1) {
				mainBox.getChildren().remove(1);
			}
			mainBox.getChildren().add(pane1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void defineNewScheduledTransactionOp() {
		URL url = getClass().getClassLoader().getResource("view/NewScheduledTransaction.fxml");
		try {
			AnchorPane pane1 = (AnchorPane) FXMLLoader.load(url);
			if (mainBox.getChildren().size() > 1) {
				mainBox.getChildren().remove(1);
			}
			mainBox.getChildren().add(pane1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void viewScheduledTransactionOp() {
		URL url = getClass().getClassLoader().getResource("view/ViewScheduledTransaction.fxml");
		try {
			AnchorPane pane1 = (AnchorPane) FXMLLoader.load(url);
			if (mainBox.getChildren().size() > 1) {
				mainBox.getChildren().remove(1);
			}
			mainBox.getChildren().add(pane1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void viewReportOp() {
		URL url = getClass().getClassLoader().getResource("view/ViewReport.fxml");
		try {
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
