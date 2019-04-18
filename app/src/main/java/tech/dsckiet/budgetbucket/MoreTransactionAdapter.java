package tech.dsckiet.budgetbucket;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MoreTransactionAdapter extends RecyclerView.Adapter<MoreTransactionAdapter.MoreTransactionViewHolder> {

    private ArrayList<MoreTransactionItem> mMoreTransactionList;

    public MoreTransactionAdapter(TransactionsActivity transactionsActivity, ArrayList<MoreTransactionItem> moreTransactionList) {
        mMoreTransactionList = moreTransactionList;
    }


    @Override
    public MoreTransactionViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transaction_card,viewGroup,false);
        return new MoreTransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder( MoreTransactionViewHolder moreTransactionViewHolder, int i) {

        MoreTransactionItem currentItem = mMoreTransactionList.get(i);

        String type = currentItem.getmType();
        String amount = currentItem.getmAmount();

        moreTransactionViewHolder.mTextViewType.setText(type);
        moreTransactionViewHolder.mTextViewAmount.setText("Rs. " + amount);

    }

    @Override
    public int getItemCount() {
        return mMoreTransactionList.size();
    }

    public class MoreTransactionViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextViewType;
        public TextView mTextViewAmount;

        public MoreTransactionViewHolder(View itemView) {
            super(itemView);
            mTextViewType = itemView.findViewById(R.id.text_transaction_type);
            mTextViewAmount = itemView.findViewById(R.id.text_transaction_amount);

        }
    }
}
