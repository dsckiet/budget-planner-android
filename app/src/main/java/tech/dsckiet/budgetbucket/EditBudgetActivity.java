package tech.dsckiet.budgetbucket;

import android.content.Intent;
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

public class EditBudgetActivity extends AppCompatActivity {

    private ImageView backArrow;
    private EditText editTextBudget;
    private CardView changeBudgetBtn;
    private FirebaseAuth mAuth;
    private String amount;
    private CoordinatorLayout coordinatorLayout;

    public String mail() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String mail = user.getEmail();
        return mail;
    }


    String type = "online";
    String URL_POST="https://tranquil-coast-71727.herokuapp.com/api/v1/edit_user_budget/" +mail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_budget);
        backArrow = findViewById(R.id.back_arrow_button_edit_budget);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editTextBudget = findViewById(R.id.editText_budget);
        changeBudgetBtn = findViewById(R.id.change_budget_button);
        coordinatorLayout = findViewById(R.id.edit_budget_coordinator_layout);


        changeBudgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextBudget.getText().toString().equals("")|| Integer.parseInt(editTextBudget.getText().toString()) == 0){
                    Snackbar.make(coordinatorLayout, "Budget can't be null", Snackbar.LENGTH_LONG)
                            .setActionTextColor(getResources().getColor(R.color.colorGoogleRed))
                            .setDuration(1000)
                            .show();
                }else{
                        post();

                }
//                Intent intent = new Intent(getApplicationContext(),FragmentProfile.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("monthlyBudget", "120");

//                FragmentProfile fragobj = new FragmentProfile();
//                fragobj.setArguments(bundle);
//                Toast.makeText(EditBudgetActivity.this, "Budget Updated", Toast.LENGTH_SHORT).show();
//                startActivity(intent);
//                finish();

            }
        });


    }
    private void post(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST,new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Snackbar.make(coordinatorLayout, "Budget updated.", Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(R.color.colorGoogleRed))
                        .setDuration(1000)
                        .show();
                editTextBudget.setText("");
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
                amount = editTextBudget.getText().toString();

                params.put("budget",amount);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}