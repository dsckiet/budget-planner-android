package tech.dsckiet.budgetbucket;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfile extends Fragment {

    private FirebaseAuth mAuth;
    private CardView edit_budget_card;
    private TextView profile_name_text_view, profile_mail_text_view, monthly_challenge_text_view;
    private ImageView profile_image;
    private boolean clicked = false;

    private String userName, userUID, userMail;


    public FragmentProfile() {
        // Required empty public constructor
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
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userName = user.getDisplayName();
        userMail = user.getEmail();
        userUID = user.getUid();

        profile_name_text_view.setText(userName);
        profile_mail_text_view.setText(userMail);

        Picasso.get().load(user.getPhotoUrl()).fit().centerInside().into(profile_image);

        //for edit budget section
        edit_budget_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditBudgetActivity.class));
                clicked = true;
                //need to implement finish
            }
        });

////        if(clicked){
//            String newBudget = getArguments().getString("monthlyBudget");
//            monthly_challenge_text_view.setText(newBudget);
////        }
        return rootView;
    }

}