package tech.dsckiet.budgetbucket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class AddCashTransactionActivity extends AppCompatActivity {

    private ImageView backArrow;
    private EditText etAddCash;
    private CardView btnAddCash;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButton1,mRadioButton2;

    private FirebaseAuth mAuth;
    private CoordinatorLayout coordinatorLayout;

    private String mail() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String mail = user.getEmail();
        return mail;
    }


    String type,notType,amountOnline;
    String URL_POST="https://tranquil-coast-71727.herokuapp.com/api/v1/add_transaction/" +mail();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cash_transaction);

        backArrow = findViewById(R.id.back_arrow_button_add_transaction);
        etAddCash = findViewById(R.id.edittext_add_transaction_amount);

        btnAddCash = findViewById(R.id.button_add_transaction);
        coordinatorLayout = findViewById(R.id.add_transaction_coordinator_layout);
        mRadioGroup = findViewById(R.id.radio_group_type);
        mRadioButton1 = findViewById(R.id.radio_button_online);

        mRadioButton2 = findViewById(R.id.radio_button_offline);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //TODO:: getIntent from Notification and if type notification is clicked setChecked online radio button else offline radio button
        SharedPreferences prefs = getSharedPreferences("DATA",0);
        String data = prefs.getString("OnlineAmount", " 0.0");
        float f=Float.parseFloat(data);
        int a = (int) Math.round(f);
        String str1 = Integer.toString(a);

        if(a != 0){
            etAddCash.setText(str1);
            mRadioButton1.setChecked(true);

        }


        btnAddCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent serviceIntent = new Intent(getApplicationContext(),BackgroundService.class);
                stopService(serviceIntent);
                SharedPreferences mPrefs = getSharedPreferences("DATA",0);
                SharedPreferences.Editor editor = mPrefs.edit();
                editor.clear();
                editor.commit();
//                int zero = etAddCash.getText().toString()
                if(etAddCash.getText().toString().equals("") || Integer.parseInt(etAddCash.getText().toString()) == 0){
                    Snackbar.make(coordinatorLayout, "Transaction can't be empty", Snackbar.LENGTH_LONG)
                            .setActionTextColor(getResources().getColor(R.color.colorGoogleRed))
                            .setDuration(1000)
                            .show();
                }else{
                    //TODO:: Separate the type of transactions Online/Offline
                    if(mRadioButton1.isChecked()){
                        fun_online();

                    }else if(mRadioButton2.isChecked()){
                        fun_offline();
                    }else{
                        Snackbar.make(coordinatorLayout, "Select the type of transaction", Snackbar.LENGTH_LONG)
                                .setActionTextColor(getResources().getColor(R.color.colorGoogleRed))
                                .setDuration(1000)
                                .show();
                    }

                }
            }
        });
        }


    private void fun_offline(){
        type = "offline";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST,new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Snackbar.make(coordinatorLayout, "Offline transaction added.", Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(R.color.colorGoogleRed))
                        .setDuration(1000)
                        .show();
                etAddCash.setText("");
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                String amount = etAddCash.getText().toString();
                params.put("amount",amount);
                params.put("type",type);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        // for late finish the activity
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);
    }

    private void fun_online(){
        type = "online";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST,new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Snackbar.make(coordinatorLayout, "Online transaction recorded.", Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(R.color.colorGoogleRed))
                        .setDuration(1000)
                        .show();
                etAddCash.setText("");
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                String amount = etAddCash.getText().toString();
                params.put("amount",amount);
                params.put("type",type);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        // for late finish the activity
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                finish();
            }
        }, 1000);

    }
}
