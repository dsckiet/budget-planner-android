package io.github.dsckiet.budgetplanner;

import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

public class AddCashTransactionActivity extends AppCompatActivity {

    private ImageView backArrow;
    private EditText etAddCash;
    private CardView btnAddCash;

    String type = "offline";
    String URL_POST="http://192.168.137.168:3000/api/v1/add_transaction/goelaakash79@gmail.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cash_transaction);

        backArrow = findViewById(R.id.back_arrow_button_add_transaction);
        etAddCash = findViewById(R.id.edittext_add_transaction_amount);
        btnAddCash = findViewById(R.id.button_add_transaction);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnAddCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fun();
            }
        });
    }

    private void fun(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST,new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplication(), response, Toast.LENGTH_SHORT).show();
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
