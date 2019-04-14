package io.github.dsckiet.budgetplanner;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {

    private List<TransactionRecyclerView> transactionList;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView company,amount;
        public MyViewHolder(View view){
            super(view);
            company = view.findViewById(R.id.text_transaction_type);
            amount = view.findViewById(R.id.text_transaction_amount);
        }
    }
    public TransactionAdapter(List<TransactionRecyclerView> transactionList){
        this.transactionList = transactionList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.transaction_card,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, int i) {
        TransactionRecyclerView transactionRecyclerView = transactionList.get(i);
        holder.company.setText(transactionRecyclerView.getTransactionCompany());
        holder.amount.setText(transactionRecyclerView.getTransactionAmount());
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }
}


