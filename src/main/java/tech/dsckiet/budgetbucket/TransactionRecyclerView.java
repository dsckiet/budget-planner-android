package tech.dsckiet.budgetbucket;

public class TransactionRecyclerView {
    private String transactionCompany, transactionAmount;

    public TransactionRecyclerView() {
    }

    public TransactionRecyclerView(String transactionCompany, String transactionAmount) {
        this.transactionCompany = transactionCompany;
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
    public String getTransactionCompany(){
        return transactionCompany;
    }

    public void setTransactionCompany(String transactionCompany) {
        this.transactionCompany = transactionCompany;
    }
}
