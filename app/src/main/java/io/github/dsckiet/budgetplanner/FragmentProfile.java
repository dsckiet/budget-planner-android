package io.github.dsckiet.budgetplanner;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfile extends Fragment {

    private FirebaseAuth mAuth;
    private CardView edit_profile_card,edit_budget_card;
    private TextView profile_name_text_view,profile_mail_text_view,weekly_challenge_text_view,monthly_challenge_text_view;
    private ImageView profile_image;

    Intent intent;

    public Intent getIntent() {
        return intent;
    }

    private String mailN;

    public void setIntent(Intent intent) {
        mailN = intent.getStringExtra("mail");
    }

    private String userName,userUID,userMail;
    String URL_POST="http://192.168.137.53:3000/api/v1/add_user/" + mailN;

    public FragmentProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile,container,false);

        mAuth = FirebaseAuth.getInstance();


        profile_name_text_view = rootView.findViewById(R.id.text_profile_name);
        profile_mail_text_view = rootView.findViewById(R.id.text_profile_mail);
        profile_image = rootView.findViewById(R.id.profile_image);

        edit_budget_card = rootView.findViewById(R.id.edit_budget_card);

        FirebaseUser user = mAuth.getCurrentUser();
        userName = user.getDisplayName();
        userMail = user.getEmail();
        userUID = user.getUid();

        profile_name_text_view.setText(userName);
        profile_mail_text_view.setText(userMail);
        fun();

        Picasso.get().load(user.getPhotoUrl()).fit().centerInside().into(profile_image);

        //for edit budget section
        edit_budget_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),EditBudgetActivity.class));
                //need to implement finish
            }
        });
        return rootView;
    }

    //POST Method
    private void fun(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST,new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
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
                String name = userName;
                String mail = userMail;
                String googleId = userUID;

                params.put("name",name);
                params.put("googleId",googleId);
                params.put("email",mail);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
