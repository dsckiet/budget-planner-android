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
import android.widget.TextView;




/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSettings extends Fragment {

    private TextView tv_feedback,tv_about;
    private ImageView iv_cross;

    public FragmentSettings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings,container,false);

        tv_feedback = rootView.findViewById(R.id.text_setting_2);
        tv_feedback.setOnClickListener(new View.OnClickListener() {
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

        View view = getLayoutInflater().inflate(R.layout.activity_about,null);
        final Dialog dialog = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(view);

        tv_about = rootView.findViewById(R.id.text_setting_3);
        tv_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
//        iv_cross = rootView.findViewById(R.id.image_cross);
//        iv_cross.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
        return rootView;
    }


}
