package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			HBox mainBox = (HBox)FXMLLoader.load(getClass().getClassLoader().getResource("view/Main.fxml"));
			Scene scene = new Scene(mainBox);
			scene.getStylesheets().add(getClass().getClassLoader().getResource("css/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Budget Buddy");
			primaryStage.show();
			
			//Keep a reference of the mainBox inside the commonObjs object
			CommonObjs commonObjs = CommonObjs.getInstance();
			commonObjs.setMainBox(mainBox);
			
		} catch(Exception e) {
			e.printStackTrace();
		}

		Database.initializeAccountsDatabase(); 
		Database.initializeTransactionTypesDatabase();
		Database.initializeTransactionsDatabase();
		Database.initializeScheduledTransactionsDatabase();
		
		// Check for due transactions upon launch
        NotificationManager.checkScheduledTransactions();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
