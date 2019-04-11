package io.github.dsckiet.budgetplanner;


import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSettings extends Fragment {

    private LinearLayout setting_share,setting_feedback,setting_about,setting_rate_us,setting_sign_out;

    public FragmentSettings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings,container,false);

        setting_share = rootView.findViewById(R.id.setting_layout_1);
        setting_feedback = rootView.findViewById(R.id.setting_layout_2);
        setting_about = rootView.findViewById(R.id.setting_layout_3);
        setting_rate_us = rootView.findViewById(R.id.setting_layout_4);
        setting_sign_out = rootView.findViewById(R.id.setting_layout_5);

        //for share section
        setting_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Share", Toast.LENGTH_SHORT).show();
            }
        });

        //for feedback section
        setting_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto: ag1507anshul@gmail.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Send Your Valuable Feedback :)");
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        //for about section
        //creating view||layout for dialog box
        View view = getLayoutInflater().inflate(R.layout.activity_about,null);
        final Dialog dialog = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(view);
        setting_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        //for rate us section
        setting_rate_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Rate us", Toast.LENGTH_SHORT).show();
            }
        });

        //for sign out section
        setting_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Sign out", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

}
