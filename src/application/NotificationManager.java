package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class NotificationManager {

    private static final String ScheduledTransactionsURL = "jdbc:sqlite:db/ScheduledTransactions.db";

    public static void checkScheduledTransactions() {
        int today = LocalDate.now().getDayOfMonth();
        String sql = "SELECT * FROM ScheduledTransactions WHERE due_date = ?";

        try (Connection conn = Database.connect(ScheduledTransactionsURL); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, today);
            ResultSet rs = pstmt.executeQuery();

            StringBuilder notificationMessage = new StringBuilder();
            boolean hasDueTransactions = false;

            while (rs.next()) {
                hasDueTransactions = true;
                String scheduleName = rs.getString("schedule_name");
                String accountName = rs.getString("account_name");
                double paymentAmount = rs.getDouble("payment_amount");

                // Aggregate the information of all due transactions
                notificationMessage.append("Scheduled Transaction: '")
                                   .append(scheduleName)
                                   .append("' for Account: '")
                                   .append(accountName)
                                   .append("' with Payment: $")
                                   .append(paymentAmount)
                                   .append("\n");
            }

            if (hasDueTransactions) {
                // If there are due transactions, show aggregated notification
                Platform.runLater(() -> showNotification(notificationMessage.toString()));
            } else {
                // If no transactions are due, show no due transactions message
                Platform.runLater(() -> showNoTransactionsNotification());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void showNotification(String notificationMessage) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Scheduled Transactions Due Today");
        alert.setHeaderText(null);
        alert.setContentText(notificationMessage);
        alert.showAndWait();
    }

    private static void showNoTransactionsNotification() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("No Transactions Due Today");
        alert.setHeaderText(null);
        alert.setContentText("There are no scheduled transactions due today.");
        alert.showAndWait();
    }
}
