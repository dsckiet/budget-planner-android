package tech.dsckiet.budgetbucket;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FragmentDashboard extends Fragment {

    private RecyclerView recyclerViewTransaction;
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
    private android.support.v4.widget.NestedScrollView layout, layoutDashboard;
    private CoordinatorLayout coordinatorLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private BottomNavigationViewEx navigationView;
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

    //checking whether phone is connected to INTERNET
    public static boolean isConnected(Context context) {
        boolean connected = false;

        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            connected = true;
        }
        return connected;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        budgetTV = rootView.findViewById(R.id.text_view_budget);

        //findviewByIDs
        recyclerViewTransaction = rootView.findViewById(R.id.recycler_view_transaction);
        decoView = rootView.findViewById(R.id.dynamic_arc_view);
        cash_card = rootView.findViewById(R.id.cash_card_dashboard);
        online_card = rootView.findViewById(R.id.online_card_dashboard);
        savings_card = rootView.findViewById(R.id.savings_card_dashboard);
        fab = rootView.findViewById(R.id.fab);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_bar_dashboard);
        layout = rootView.findViewById(R.id.offline_layout);
        layoutDashboard = rootView.findViewById(R.id.dashboardLayout);
        coordinatorLayout = rootView.findViewById(R.id.dashboard_coordinator_layout);
//        navigationView = (BottomNavigationViewEx) rootView.findViewById(R.id.bottom_nav);
//        navigationView.setClickable(false);
        mSwipeRefreshLayout.setRefreshing(true);

        loadData();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        mSwipeRefreshLayout.setColorSchemeColors(
                R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark
        );
        more_transactions_card = rootView.findViewById(R.id.more_transaction);
        more_transactions_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TransactionsActivity.class);
                startActivity(intent);
            }
        });
        return rootView;

    }


    private void loadData() {
        if (isConnected(getActivity())) {
            final ArrayList<TransactionRecyclerView> mList = new ArrayList<>();
            //TODO: VOLLEY
            mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject data = response.getJSONObject("data");
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

                                int savingInt = Integer.parseInt(savingsAmt);
                                int leftAmountInt = Integer.parseInt(leftAmt);

                                //left amount and savings status sent to bottom sheet dialog
                                SharedPreferences prefs = getContext().getSharedPreferences("BottomSheet", 0);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putInt("savings", savingInt);
                                editor.putInt("leftAmount", leftAmountInt);

                                editor.commit();


                                JSONArray transactions = data.getJSONArray("transactions");
                                for (int i = 0; i < transactions.length(); i++) {
                                    JSONObject sample = transactions.getJSONObject(i);
                                    String type = sample.getString("type");
                                    String amount = sample.getString("amount");
                                    mList.add(new TransactionRecyclerView(type, amount));

                                }
                                mAdapter = new TransactionAdapter(mList);
                                recyclerViewTransaction.setHasFixedSize(true);
                                final LinearLayoutManager layoutManager = new LinearLayoutManager(FragmentDashboard.this.getActivity());
                                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerViewTransaction.setLayoutManager(layoutManager);

                                recyclerViewTransaction.setItemAnimator(new DefaultItemAnimator());
                                recyclerViewTransaction.setAdapter(mAdapter);
                                mSwipeRefreshLayout.setRefreshing(false);


                                if (budgetAmt != null && savings == 1) {

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
                                    fab.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (budgetAmount != 0f) {
                                                startActivity(new Intent(getContext(), AddCashTransactionActivity.class));
                                            } else {
                                                Snackbar.make(coordinatorLayout, "Please enter your budget.", Snackbar.LENGTH_LONG)
                                                        .setActionTextColor(getResources().getColor(R.color.colorGoogleRed))
                                                        .setDuration(1000)
                                                        .show();
                                            }
                                        }
                                    });
                                } else {
                                    Snackbar.make(coordinatorLayout, "Please enter your budget.", Snackbar.LENGTH_LONG)
                                            .setActionTextColor(getResources().getColor(R.color.colorGoogleRed))
                                            .setDuration(1000)
                                            .show();
                                }

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
            //End of JSON Volley}
        } else if (!isConnected(getActivity())) {
            mSwipeRefreshLayout.setRefreshing(false);

            Snackbar.make(coordinatorLayout, "Check your connection", Snackbar.LENGTH_LONG)
                    .setActionTextColor(getResources().getColor(R.color.colorGoogleRed))
                    .setDuration(1000)
                    .show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mQueue != null) {
            mQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
        }
    }

    private void createBackSeries() {
        SeriesItem seriesItem = new SeriesItem.Builder(getResources().getColor(R.color.colorGoogleGreen))
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(true)
                .build();
        BackIndex = decoView.addSeries(seriesItem);
    }

    private void createBackSeries1() {

        if (budgetAmount != 0f) {
            final float cashValue = (offlineAmount / budgetAmount) * 100;
            final float onlineValue = (onlineAmount / budgetAmount) * 100;
            final SeriesItem seriesItem = new SeriesItem.Builder(getResources().getColor(R.color.colorGoogleYellow))
                    .setRange(0, seriesMax, 0)
                    .setInitialVisibility(false)
                    .build();
            final TextView textPercentage = getView().findViewById(R.id.textPercentage);
            seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
                @Override
                public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                    //float percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                    if (savings == 1f) {
                        textPercentage.setText("Savings : " + "\n \n" + "Rs. " + String.format("%.01f", leftAmount));
                    } else if (savings == 0f) {
                        textPercentage.setText("\n" + " No savings.");
                    }

                }

                @Override
                public void onSeriesItemDisplayProgress(float percentComplete) {
                }
            });
            Series1Index = decoView.addSeries(seriesItem);
        }

    }

    private void createBackSeries2() {
        final SeriesItem seriesItem = new SeriesItem.Builder(getResources().getColor(R.color.colorGoogleRed))
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(false)
                .build();

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
        if (budgetAmount != 0f) {
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
        } else {
            Snackbar.make(coordinatorLayout, "Please enter your budget.", Snackbar.LENGTH_LONG)
                    .setActionTextColor(getResources().getColor(R.color.colorGoogleRed))
                    .setDuration(1000)
                    .show();
        }
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
        if (budgetAmount != 0f) {
            final float cashValue = (offlineAmount / budgetAmount) * 100;
//            final float onlineValue = (onlineAmount / budgetAmount) * 100;
            final SeriesItem seriesItem = new SeriesItem.Builder(getResources().getColor(R.color.colorGoogleYellow))
                    .setRange(0, seriesMax, 0)
                    .setInitialVisibility(false)
                    .build();
            final TextView textPercentage = getView().findViewById(R.id.textPercentage);
            //final TextView txt1 = findViewById(R.id.t1);
            seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
                @Override
                public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
//                    float percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                    textPercentage.setText("Cash : " + "\n \n" + "Rs. " + String.format("%.01f", offlineAmount));
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
        } else {
            Snackbar.make(coordinatorLayout, "Please enter your budget 1.", Snackbar.LENGTH_LONG)
                    .setActionTextColor(getResources().getColor(R.color.colorGoogleRed))
                    .setDuration(1000)
                    .show();

        }
    }

    private void createBackSeries2a() {
        if (budgetAmount != 0f) {
//            final float cashValue = (offlineAmount / budgetAmount) * 100;
            final float onlineValue = (onlineAmount / budgetAmount) * 100;
            final SeriesItem seriesItem = new SeriesItem.Builder(getResources().getColor(R.color.colorGoogleRed))
                    .setRange(0, seriesMax, 0)
                    .setInitialVisibility(false)
                    .build();

            //final TextView textActivity2 = (TextView) findViewById(R.id.textActivity2);
            //final TextView txt2 = findViewById(R.id.t2);
            final TextView textPercentage = getView().findViewById(R.id.textPercentage);
            seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
                @Override
                public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
//                    float percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                    //txt2.setText(String.format("%.0f%%",  (percentFilled * 100f)));
                    textPercentage.setText("Online : " + "\n \n" + "Rs. " + String.format("%.01f", onlineAmount));
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
        } else {
            Snackbar.make(coordinatorLayout, "Please enter your budget.", Snackbar.LENGTH_LONG)
                    .setActionTextColor(getResources().getColor(R.color.colorGoogleRed))
                    .setDuration(1000)
                    .show();
        }
    }

}