package application;

public interface Transactionable {
	int getId();
	String getAccount();
	String getTransactionType();
	Double getPaymentAmount();
}
