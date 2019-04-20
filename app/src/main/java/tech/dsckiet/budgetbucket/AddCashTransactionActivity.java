package tech.dsckiet.budgetbucket;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

    private FirebaseAuth mAuth;
    private CoordinatorLayout coordinatorLayout;

    private String mail() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String mail = user.getEmail();
        return mail;
    }

    String type = "offline";
    String URL_POST="https://tranquil-coast-71727.herokuapp.com/api/v1/add_transaction/" +mail();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cash_transaction);

        backArrow = findViewById(R.id.back_arrow_button_add_transaction);
        etAddCash = findViewById(R.id.edittext_add_transaction_amount);
        btnAddCash = findViewById(R.id.button_add_transaction);
        coordinatorLayout = findViewById(R.id.add_transaction_coordinator_layout);


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAddCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int zero = etAddCash.getText().toString()
                if(etAddCash.getText().toString().equals("") || Integer.parseInt(etAddCash.getText().toString()) == 0){
                    Snackbar.make(coordinatorLayout, "Transaction can't be empty", Snackbar.LENGTH_LONG)
                            .setActionTextColor(getResources().getColor(R.color.colorGoogleRed))
                            .setDuration(1000)
                            .show();
                }else{
                    fun();
                }
            }
        });
        }


    private void fun(){
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
    }
}
