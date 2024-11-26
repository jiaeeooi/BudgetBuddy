package application;

public class Transaction implements Transactionable{
	
	private int id;
	private String account;
    private String transactionType;
    private String transactionDate;
    private String transactionDescription;
    private Double paymentAmount;
    private Double depositAmount;

    public Transaction(int id, String account, String transactionType, String transactionDate, String transactionDescription, Double paymentAmount, Double depositAmount) {
    	this.id = id;
    	this.account = account;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.transactionDescription = transactionDescription;
        this.paymentAmount = paymentAmount;
        this.depositAmount = depositAmount;
    }

    public int getId() { return id; }
    public String getAccount() { return account; }
    public String getTransactionType() { return transactionType; }
    public String getTransactionDate() { return transactionDate; }
    public String getTransactionDescription() { return transactionDescription; }
    public Double getPaymentAmount() { return paymentAmount; }
    public Double getDepositAmount() { return depositAmount; }
}
