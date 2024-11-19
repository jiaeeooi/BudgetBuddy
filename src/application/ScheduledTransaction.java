package application;

public class ScheduledTransaction {
    private int id;
	private String scheduleName;
    private String account;
    private String transactionType;
    private String frequency;
    private int dueDate;
    private double paymentAmount;

    // Constructor
    public ScheduledTransaction(int id, String scheduleName, String account, String transactionType, String frequency, int dueDate, double paymentAmount) {
        this.id = id;
    	this.scheduleName = scheduleName;
        this.account = account;
        this.transactionType = transactionType;
        this.frequency = frequency;
        this.dueDate = dueDate;
        this.paymentAmount = paymentAmount;
    }

    // Getters and setters
    public int getId() { return id; }
    
    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public int getDueDate() {
        return dueDate;
    }

    public void setDueDate(int dueDate) {
        this.dueDate = dueDate;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
}
