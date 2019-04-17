package tech.dsckiet.budgetbucket;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import java.util.ArrayList;


public class FragmentDashboard extends Fragment {

    private RecyclerView recyclerViewTransaction;
    private TransactionAdapter mAdapter;
    private int BackIndex;
    private int Series1Index;
    private int Series2Index;
    private DecoView decoView;
    private final float seriesMax = 50f;
    private CardView cash_card;
    private CardView online_card;
    private CardView savings_card;
    private FloatingActionButton fab;
    private CardView more_transactions_card;

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
        transactionList.add(new TransactionRecyclerView("Online", "Rs. 110"));
        transactionList.add(new TransactionRecyclerView("Offline", "Rs. 20"));
        transactionList.add(new TransactionRecyclerView("Online", "Rs. 80"));
        transactionList.add(new TransactionRecyclerView("Offline", "Rs. 80"));
        transactionList.add(new TransactionRecyclerView("Offlinee", "Rs. 600"));

        mAdapter = new TransactionAdapter(transactionList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(FragmentDashboard.this.getActivity());
        recyclerViewTransaction.setLayoutManager(layoutManager);

        recyclerViewTransaction.setItemAnimator(new DefaultItemAnimator());
        // adding the divider between the elements
//        recyclerViewTransaction.addItemDecoration(new DividerItemDecoration(HomeFragment.this.getActivity(),LinearLayoutManager.VERTICAL));
        recyclerViewTransaction.setAdapter(mAdapter);

        decoView = view.findViewById(R.id.dynamic_arc_view);
        cash_card = view.findViewById(R.id.cash_card_dashboard);
        online_card = view.findViewById(R.id.online_card_dashboard);
        savings_card = view.findViewById(R.id.savings_card_dashboard);
        fab = view.findViewById(R.id.fab);
        more_transactions_card = view.findViewById(R.id.more_transaction);
        more_transactions_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),TransactionsActivity.class);
                startActivity(intent);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),AddCashTransactionActivity.class));
            }
        });
        createBackSeries();
        createBackSeries1();
        createBackSeries2();
        createEvents();
        cash_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEventCash();
            }
        });
        online_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEventOnline();
            }
        });
        savings_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBackSeries();
                createBackSeries1();
                createBackSeries2();
                createEvents();
            }
        });

    }

    private void createBackSeries(){
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#55D880"))
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(true)
                .build();
        BackIndex = decoView.addSeries(seriesItem);
    }

    private void createBackSeries1() {
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFAA00"))
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(false)
                .build();
        final TextView textPercentage = getView().findViewById(R.id.textPercentage);
        //final TextView txt1 = findViewById(R.id.t1);
        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                textPercentage.setText("Savings : " + "\n" +String.format("%.0f%%",100 - (percentFilled * 100f)));
                //txt1.setText(String.format("%.0f%%",  (percentFilled * 100f)-(1200/50)));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {
//                final TextView txt2 = findViewById(R.id.t2);
//                txt2.setText(Float.toString(percentComplete*100f));
            }
        });
        Series1Index = decoView.addSeries(seriesItem);
    }

    private void createBackSeries2() {
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#DF4141"))
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(false)
                .build();

        //final TextView textActivity2 = (TextView) findViewById(R.id.textActivity2);
        //final TextView txt2 = findViewById(R.id.t2);

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                //txt2.setText(String.format("%.0f%%",  (percentFilled * 100f)));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {
//                final TextView txt1 = findViewById(R.id.t1);
//                txt1.setText(Float.toString(percentComplete*100f));
            }
        });

        Series2Index = decoView.addSeries(seriesItem);
    }
    private void createEvents() {
        //decoView.executeReset();

        decoView.addEvent(new DecoEvent.Builder(seriesMax)
                .setIndex(BackIndex)
                .setDuration(2000)
                .setDelay(100)
                .build());

        decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(Series1Index)
                .setDuration(1000)
                .setDelay(1000)
                .build());

        decoView.addEvent(new DecoEvent.Builder(34f)
                .setIndex(Series1Index)
                .setDelay(1000)
                .build());
        decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(Series2Index)
                .setDuration(1000)
                .setDelay(1000)
                .build());

        decoView.addEvent(new DecoEvent.Builder(12f)
                .setIndex(Series2Index)
                .setDelay(1000)
                .build());
    }
    private void createEventCash(){
        decoView.executeReset();
        createBackSeries1a();
    }
    private void createEventOnline(){
        decoView.executeReset();
        createBackSeries2a();
    }
    private void createBackSeries1a() {
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFAA00"))
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(false)
                .build();
        final TextView textPercentage = getView().findViewById(R.id.textPercentage);
        //final TextView txt1 = findViewById(R.id.t1);
        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                textPercentage.setText("Spent : " + "\n" +String.format("%.0f%%",(percentFilled * 100f)-(1200/50)));
                //txt1.setText(String.format("%.0f%%",  (percentFilled * 100f)-(1200/50)));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {
//                final TextView txt2 = findViewById(R.id.t2);
//                txt2.setText(Float.toString(percentComplete*100f));
            }
        });
        Series1Index = decoView.addSeries(seriesItem);
        decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(Series1Index)
                .setDuration(1000)
                .setDelay(1000)
                .build());

        decoView.addEvent(new DecoEvent.Builder(34f-12f)
                .setIndex(Series1Index)
                .setDelay(1000)
                .build());
    }
    private void createBackSeries2a() {
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#DF4141"))
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(false)
                .build();

        //final TextView textActivity2 = (TextView) findViewById(R.id.textActivity2);
        //final TextView txt2 = findViewById(R.id.t2);
        final TextView textPercentage = getView().findViewById(R.id.textPercentage);
        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                //txt2.setText(String.format("%.0f%%",  (percentFilled * 100f)));
                textPercentage.setText("Spent : " + "\n" +String.format("%.0f%%",(percentFilled * 100f)));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {
//                final TextView txt1 = findViewById(R.id.t1);
//                txt1.setText(Float.toString(percentComplete*100f));
            }
        });

        Series2Index = decoView.addSeries(seriesItem);
        decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(Series2Index)
                .setDuration(1000)
                .setDelay(1000)
                .build());

        decoView.addEvent(new DecoEvent.Builder(12f)
                .setIndex(Series2Index)
                .setDelay(1000)
                .build());
    }
}
