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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FragmentDashboard extends Fragment {

    private RecyclerView recyclerViewTransaction;
    //    private ArrayList<TransactionRecyclerView> mList;
    private TransactionAdapter mAdapter;
    private int BackIndex;
    private int Series1Index;
    private int Series2Index;
    private DecoView decoView;
    private final float seriesMax = 100f;
    private CardView cash_card;
    private CardView online_card;
    private CardView savings_card;
    private FloatingActionButton fab;
    private CardView more_transactions_card;

    private TextView budgetTV;
    private RequestQueue mQueue;
    private FirebaseAuth mAuth;

    private String mail() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String mail = user.getEmail();
        return mail;
    }

    private String URL = "https://tranquil-coast-71727.herokuapp.com/api/v1/dashboard/" + mail();


    static float onlineAmount, offlineAmount, budgetAmount, leftAmount, savings;

    public FragmentDashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        budgetTV = rootView.findViewById(R.id.text_view_budget);


        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        final ArrayList<TransactionRecyclerView> mList = new ArrayList<>();

//        transactionList.add(new TransactionRecyclerView("Online", "Rs. 110"));
//        transactionList.add(new TransactionRecyclerView("Offline", "Rs. 20"));
//        transactionList.add(new TransactionRecyclerView("Online", "Rs. 80"));
//        transactionList.add(new TransactionRecyclerView("Offline", "Rs. 80"));
//        transactionList.add(new TransactionRecyclerView("Offlinee", "Rs. 600"));
//
//        mAdapter = new TransactionAdapter(transactionList);
//        final LinearLayoutManager layoutManager = new LinearLayoutManager(FragmentDashboard.this.getActivity());
//        recyclerViewTransaction.setLayoutManager(layoutManager);
//
//        recyclerViewTransaction.setItemAnimator(new DefaultItemAnimator());
//        // adding the divider between the elements
////        recyclerViewTransaction.addItemDecoration(new DividerItemDecoration(HomeFragment.this.getActivity(),LinearLayoutManager.VERTICAL));
//        recyclerViewTransaction.setAdapter(mAdapter);

        recyclerViewTransaction = view.findViewById(R.id.recycler_view_transaction);
        decoView = view.findViewById(R.id.dynamic_arc_view);
        cash_card = view.findViewById(R.id.cash_card_dashboard);
        online_card = view.findViewById(R.id.online_card_dashboard);
        savings_card = view.findViewById(R.id.savings_card_dashboard);
        fab = view.findViewById(R.id.fab);

        more_transactions_card = view.findViewById(R.id.more_transaction);
        more_transactions_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TransactionsActivity.class);
                startActivity(intent);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddCashTransactionActivity.class));
            }
        });

        //TODO: VOLLEY
        mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");

                            JSONArray transactions = data.getJSONArray("transactions");
                            for (int i = 0; i < transactions.length(); i++) {
                                JSONObject sample = transactions.getJSONObject(i);
                                String type = sample.getString("type");
                                String amount = sample.getString("amount");
                                mList.add(new TransactionRecyclerView(type, amount));
//                                Toast.makeText(getActivity(), amt, Toast.LENGTH_SHORT).show();

                            }
                            mAdapter = new TransactionAdapter(mList);
                            recyclerViewTransaction.setHasFixedSize(true);
                            final LinearLayoutManager layoutManager = new LinearLayoutManager(FragmentDashboard.this.getActivity());
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerViewTransaction.setLayoutManager(layoutManager);

                            recyclerViewTransaction.setItemAnimator(new DefaultItemAnimator());
                            // adding the divider between the elements
//        recyclerViewTransaction.addItemDecoration(new DividerItemDecoration(HomeFragment.this.getActivity(),LinearLayoutManager.VERTICAL));
                            recyclerViewTransaction.setAdapter(mAdapter);

//                            mAdapter.notifyDataSetChanged();


                            //TODO:MAIN DATA
                            String onlineAmt = data.getString("online");
                            onlineAmount = Float.parseFloat(onlineAmt);
                            String offlineAmt = data.getString("offline");
                            offlineAmount = Float.parseFloat(offlineAmt);
                            String budgetAmt = data.getString("budget");
                            budgetAmount = Float.parseFloat(budgetAmt);
                            String leftAmt = data.getString("left_amount");
                            leftAmount = Float.parseFloat(leftAmt);
                            String savingsAmt = data.getString("savings");
                            savings = Float.parseFloat(savingsAmt);


//                            budgetTV.setText((int)budgetAmount);
                            createBackSeries();
                            createBackSeries1();
                            createBackSeries2();
                            createEvents();


                            budgetTV.setText("Rs. " + budgetAmt);

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

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
        //End of JSON Volley

    }


    private void createBackSeries() {
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#55D880"))
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(true)
                .build();
        BackIndex = decoView.addSeries(seriesItem);
    }

    private void createBackSeries1() {

        final float cashValue = (offlineAmount / budgetAmount) * 100;
        final float onlineValue = (onlineAmount / budgetAmount) * 100;
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFAA00"))
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(false)
                .build();
        final TextView textPercentage = getView().findViewById(R.id.textPercentage);
        //final TextView txt1 = findViewById(R.id.t1);
        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                //float percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                textPercentage.setText("Savings : " + "\n" + String.format("%.0f%%", 100 - onlineValue - cashValue));
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
        final float cashValue = (offlineAmount / budgetAmount) * 100;
        final float onlineValue = (onlineAmount / budgetAmount) * 100;
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

        decoView.addEvent(new DecoEvent.Builder(cashValue + onlineValue)
                .setIndex(Series1Index)
                .setDelay(1000)
                .build());
        decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(Series2Index)
                .setDuration(1000)
                .setDelay(1000)
                .build());

        decoView.addEvent(new DecoEvent.Builder(onlineValue)
                .setIndex(Series2Index)
                .setDelay(1000)
                .build());
    }

    private void createEventCash() {
        decoView.executeReset();
        createBackSeries1a();
    }

    private void createEventOnline() {
        decoView.executeReset();
        createBackSeries2a();
    }

    private void createBackSeries1a() {
        final float cashValue = (offlineAmount / budgetAmount) * 100;
        final float onlineValue = (onlineAmount / budgetAmount) * 100;
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
                textPercentage.setText("Spent : " + "\n" + String.format("%.0f%%", (percentFilled * 100f)));
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

        decoView.addEvent(new DecoEvent.Builder(cashValue)
                .setIndex(Series1Index)
                .setDelay(1000)
                .build());
    }

    private void createBackSeries2a() {
        final float cashValue = (offlineAmount / budgetAmount) * 100;
        final float onlineValue = (onlineAmount / budgetAmount) * 100;
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
                textPercentage.setText("Spent : " + "\n" + String.format("%.0f%%", (percentFilled * 100f)));
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

        decoView.addEvent(new DecoEvent.Builder(onlineValue)
                .setIndex(Series2Index)
                .setDelay(1000)
                .build());
    }
}
