package tech.dsckiet.budgetbucket;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

public class BottomSheetDialog extends BottomSheetDialogFragment {
    private FirebaseAuth mAuth;
    String URL_POST = "https://tranquil-coast-71727.herokuapp.com/api/v1/edit_user_budget/" + mail();
    private CoordinatorLayout coordinatorLayout;
    private EditText editTextUpdateAmt;

    public String mail() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String mail = user.getEmail();
        return mail;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_budget_bottom_sheet_layout, container, false);

        CardView btnUpdate = view.findViewById(R.id.update_budget_button);
        editTextUpdateAmt = view.findViewById(R.id.edittext_update_transaction_amount);
        coordinatorLayout = view.findViewById(R.id.update_budget_coordinator_layout);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextUpdateAmt.getText().toString().equals("") || Integer.parseInt(editTextUpdateAmt.getText().toString()) == 0) {
                    Snackbar.make(coordinatorLayout, "Budget can't be null", Snackbar.LENGTH_LONG)
                            .setActionTextColor(getResources().getColor(R.color.colorGoogleRed))
                            .setDuration(1000)
                            .show();
                } else {
                    post();
                }
            }
        });
        return view;
    }

    private void post() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Snackbar.make(coordinatorLayout, "Budget updated.", Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(R.color.colorGoogleRed))
                        .setDuration(1000)
                        .show();

                dismiss();
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
                String amount = editTextUpdateAmt.getText().toString();

                params.put("budget", amount);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


}
