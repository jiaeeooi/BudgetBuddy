package application;

public class Account {
    private String accountName;
    private String openingDate;
    private double openingBalance;

    public Account(String accountName, double openingBalance, String openingDate) {
        this.accountName = accountName;
        this.openingBalance = openingBalance;
        this.openingDate = openingDate;
    }

	public String getAccountName() {
        return accountName;
    }

    public String getOpeningDate() {
        return openingDate;
    }

    public double getOpeningBalance() {
        return openingBalance;
    }
}
