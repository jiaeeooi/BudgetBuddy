package application;

public class Transaction {
	
	private String account;
    private String transactionType;
    private String transactionDate;
    private String transactionDescription;
    private Double paymentAmount;
    private Double depositAmount;

    public Transaction(String account, String transactionType, String transactionDate, String transactionDescription, Double paymentAmount, Double depositAmount) {
        this.account = account;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.transactionDescription = transactionDescription;
        this.paymentAmount = paymentAmount;
        this.depositAmount = depositAmount;
    }

    public String getAccount() { return account; }
    public String getTransactionType() { return transactionType; }
    public String getTransactionDate() { return transactionDate; }
    public String getDescription() { return transactionDescription; }
    public Double getPaymentAmount() { return paymentAmount; }
    public Double getDepositAmount() { return depositAmount; }
}
