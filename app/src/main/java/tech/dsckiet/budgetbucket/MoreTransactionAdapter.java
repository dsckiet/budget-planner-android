package tech.dsckiet.budgetbucket;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MoreTransactionAdapter extends RecyclerView.Adapter<MoreTransactionAdapter.MoreTransactionViewHolder> {

    private Context mContext;
    private ArrayList<MoreTransactionItem> mMoreTransactionList;

    public MoreTransactionAdapter(Context context, ArrayList<MoreTransactionItem> moreTransactionList) {
        mContext = context;
        mMoreTransactionList = moreTransactionList;
    }

    @NonNull
    @Override
    public MoreTransactionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.transaction_card,viewGroup,false);
        return new MoreTransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreTransactionViewHolder moreTransactionViewHolder, int i) {
        MoreTransactionItem currentItem = mMoreTransactionList.get(i);

        String type = currentItem.getmType();
        int amount = currentItem.getmAmount();

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

        public MoreTransactionViewHolder( View itemView) {
            super(itemView);
            mTextViewType = itemView.findViewById(R.id.text_transaction_type);
            mTextViewAmount = itemView.findViewById(R.id.text_transaction_amount);

        }
    }


}
