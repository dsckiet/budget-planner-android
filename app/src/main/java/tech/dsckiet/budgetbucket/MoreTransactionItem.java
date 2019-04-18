package tech.dsckiet.budgetbucket;

public class MoreTransactionItem {

    private String mAmount;
    private String mType;

    public MoreTransactionItem(String type, String amount){
        mAmount = amount;
        mType = type;
    }

    public String getmAmount() {
        return mAmount;
    }

    public String getmType() {
        return mType;
    }
}
