package tech.dsckiet.budgetbucket;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;


public class FragmentSettings extends Fragment {

    private LinearLayout setting_share,setting_feedback,setting_about,
            setting_rate_us,setting_sign_out, setting_oss;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleSignInClient mGoogleSignInClient;
    //For linking
    private CustomTabsServiceConnection customTabsServiceConnection;
    private CustomTabsClient mClient;
    CustomTabsSession customTabsSession;

    public FragmentSettings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings,container,false);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        setting_share = rootView.findViewById(R.id.setting_layout_1);
        setting_feedback = rootView.findViewById(R.id.setting_layout_2);
        setting_about = rootView.findViewById(R.id.setting_layout_3);
        setting_rate_us = rootView.findViewById(R.id.setting_layout_4);
        setting_sign_out = rootView.findViewById(R.id.setting_layout_5);
        setting_oss = rootView.findViewById(R.id.setting_layout_6);

        //for share section
        setting_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Here is the share content body";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
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
        setting_oss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OssLicensesMenuActivity.class));
            }
        });
        OssLicensesMenuActivity.setActivityTitle("Open Source Licenses");
        //for about section
        //creating view||layout for dialog box
        View view = getLayoutInflater().inflate(R.layout.activity_about,null);
        final Dialog dialog = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(view);
        TextView websiteLink = view.findViewById(R.id.website_link);
        websiteLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customTabLinking("https://dsckiet.tech/");
            }
        });
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
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                    // Link @http://play.google.com/store/apps/details?id=
                    // Add playstore app link @Uri.parse() method
                }
            }
        });

        //for sign out section
        setting_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setTitle("Sign out");
                        alert.setMessage("Are you sure you want to sign out?");
                        alert.setPositiveButton("Sign out", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                mAuth.signOut();
                                Intent intent = new Intent(getContext(), AuthActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        });
                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int whichButton) {
                                return;
                            }
                        });
                        alert.show();

                    }
                });
            }
        });

        return rootView;
    }
    public void customTabLinking(String url){
        customTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                //pre-warning means to fast the surfing
                mClient = customTabsClient;
                mClient.warmup(0L);
                customTabsSession = mClient.newSession(null);
            }


            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mClient = null;
            }
        };
        CustomTabsClient.bindCustomTabsService(getContext(),"com.android.chrome",customTabsServiceConnection);
        Uri uri = Uri.parse(url);

        //Create an Intent Builder
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
        //Begin Customizing
        //Set Toolbar Colors
        intentBuilder.setToolbarColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));

        //Set Start and Exit Animations
        intentBuilder.setStartAnimations(getContext(),R.anim.slide_in_right,R.anim.slide_out_left);
        intentBuilder.setExitAnimations(getContext(),android.R.anim.slide_in_left,android.R.anim.slide_out_right);

        //build custom tabs intent
        CustomTabsIntent customTabsIntent =  intentBuilder.build();
        customTabsIntent.intent.setPackage("com.android.chrome");
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intentBuilder.setShowTitle(true);
        intentBuilder.enableUrlBarHiding();

        //Setting a custom back button


        //launch the url
        customTabsIntent.launchUrl(getContext(),uri);

    }
}