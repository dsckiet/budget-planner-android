package tech.dsckiet.budgetbucket;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FragmentProfile extends Fragment {

    private FirebaseAuth mAuth;
    private CardView edit_budget_card;
    private TextView profile_name_text_view, profile_mail_text_view, monthly_challenge_text_view;
    private ImageView profile_image;
    private RequestQueue mQueue;
    private CoordinatorLayout coordinatorLayout;
    private SwipeRefreshLayout profileSwipeRefresh;

    private String userName, userUID, userMail;
    String URL_POST = "https://tranquil-coast-71727.herokuapp.com/api/v1/add_user/" + mail();
    private String URL_GET = "https://tranquil-coast-71727.herokuapp.com/api/v1/dashboard/" + mail();


    public String mail() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String mail = user.getEmail();
        return mail;
    }

    public FragmentProfile() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        profile_name_text_view = rootView.findViewById(R.id.text_profile_name);
        profile_mail_text_view = rootView.findViewById(R.id.text_profile_mail);
        profile_image = rootView.findViewById(R.id.profile_image);
        monthly_challenge_text_view = rootView.findViewById(R.id.text_monthly_challenge);
        edit_budget_card = rootView.findViewById(R.id.edit_budget_card);
        profileSwipeRefresh = rootView.findViewById(R.id.swipe_refresh_bar_profile);
        coordinatorLayout = rootView.findViewById(R.id.profile_coordinator_layout);

        profileSwipeRefresh.setRefreshing(true);

        mAuth = FirebaseAuth.getInstance();
        post();
        loadData();
        profileSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        profileSwipeRefresh.setColorSchemeColors(
                getResources().getColor(R.color.colorAccent),getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimaryDark)
        );
        edit_budget_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditBudgetActivity.class));
            }
        });

        return rootView;
    }

    //POST Method
    private void post() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String name = userName;
                String mail = userMail;
                String googleId = userUID;

                params.put("name", name);
                params.put("googleId", googleId);
                params.put("email", mail);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    //GET Method
    private void loadData() {
        if (isConnected(getActivity())) {
            FirebaseUser user = mAuth.getCurrentUser();
            userName = user.getDisplayName();
            userMail = user.getEmail();
            userUID = user.getUid();

            profile_name_text_view.setText(userName);
            profile_mail_text_view.setText(userMail);

            Picasso.get().load(user.getPhotoUrl()).fit().centerInside().into(profile_image);

            final ArrayList<TransactionRecyclerView> mList = new ArrayList<>();
            //TODO: VOLLEY
            mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_GET, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject data = response.getJSONObject("data");
                                //TODO:MAIN DATA

                                String budgetAmt = data.getString("budget");
                                monthly_challenge_text_view.setText("Rs. " + budgetAmt);
                                profileSwipeRefresh.setRefreshing(false);

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
            Snackbar.make(coordinatorLayout, "Check your connection", Snackbar.LENGTH_LONG)
                    .setActionTextColor(getResources().getColor(R.color.colorGoogleRed))
                    .setDuration(1000)
                    .show();
        }
    }

}