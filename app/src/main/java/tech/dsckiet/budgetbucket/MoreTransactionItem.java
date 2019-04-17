package tech.dsckiet.budgetbucket;

public class MoreTransactionItem {

    private int mAmount;
    private String mType;

    public MoreTransactionItem(String type, int amount){
        mAmount = amount;
        mType = type;
    }

    public int getmAmount() {
        return mAmount;
    }

    public String getmType() {
        return mType;
    }
}
