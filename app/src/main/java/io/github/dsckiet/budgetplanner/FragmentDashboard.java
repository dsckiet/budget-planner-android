package io.github.dsckiet.budgetplanner;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class FragmentDashboard extends Fragment {

    private RecyclerView recyclerViewTransaction;
    private TransactionAdapter mAdapter;


    public FragmentDashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dashboard,container,false);

        return rootView;
    }
    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ArrayList<TransactionRecyclerView> transactionList = new ArrayList<>();
        recyclerViewTransaction = view.findViewById(R.id.recycler_view_transaction);
        transactionList.add(new TransactionRecyclerView("PayTM","Rs. 110"));
        transactionList.add(new TransactionRecyclerView("Cash","Rs. 20"));
        transactionList.add(new TransactionRecyclerView("PhonePe","Rs. 80"));
        transactionList.add(new TransactionRecyclerView("Cash","Rs. 80"));
        transactionList.add(new TransactionRecyclerView("Google Pay","Rs. 60"));

        mAdapter = new TransactionAdapter(transactionList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(FragmentDashboard.this.getActivity());
        recyclerViewTransaction.setLayoutManager(layoutManager);

        recyclerViewTransaction.setItemAnimator(new DefaultItemAnimator());
        // adding the divider between the elements
//        recyclerViewTransaction.addItemDecoration(new DividerItemDecoration(HomeFragment.this.getActivity(),LinearLayoutManager.VERTICAL));
        recyclerViewTransaction.setAdapter(mAdapter);
    }

}
