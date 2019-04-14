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


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfile extends Fragment {

    private CardView edit_profile_card,edit_budget_card;
    private TextView profile_name_text_view,profile_mail_text_view,weekly_challenge_text_view,monthly_challenge_text_view;
    private ImageView profile_image;

    public FragmentProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile,container,false);

//        edit_profile_card = rootView.findViewById(R.id.edit_profile_card);
        edit_budget_card = rootView.findViewById(R.id.edit_budget_card);


        //for edit profile section
//        edit_profile_card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getContext(),EditProfileActivity.class));
//                //need to implement finish
//            }
//        });

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

}
