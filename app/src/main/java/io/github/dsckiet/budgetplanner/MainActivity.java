package io.github.dsckiet.budgetplanner;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationViewEx navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bottom navigation
        navigationView = (BottomNavigationViewEx) findViewById(R.id.bottom_nav);
        navigationView.enableAnimation(false);
        navigationView.enableShiftingMode(true);
        navigationView.enableItemShiftingMode(false);
//        navigationView.setItemBackground(0,R.color.colorPrimary);
//        navigationView.setItemBackground(2,R.color.colorPrimary);
        navigationView.setOnNavigationItemSelectedListener(navListener);
        replaceFragment(new FragmentDashboard());
        getSupportFragmentManager()
                .addOnBackStackChangedListener(
                        new FragmentManager.OnBackStackChangedListener() {
                            @Override
                            public void onBackStackChanged() {
                                MainActivity.this.updateBottomNavigationTitle(MainActivity.this.getSupportFragmentManager().findFragmentById(R.id.container));
                            }
                        }
                );

    }



    //Bottom Navigation
    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    replaceFragment(new FragmentDashboard());
                    break;
                case R.id.nav_account:
                    replaceFragment(new FragmentProfile());
                    break;
                case R.id.nav_settings:
                    replaceFragment(new FragmentSettings());
                    break;
            }
            return true;
        }

    };

    private void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null) {
            // fragment not in back stack, create it
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment, backStateName);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.addToBackStack(backStateName);
            fragmentTransaction.commit();
        }
    }

    public void updateBottomNavigationTitle(Fragment f){
        String className = f.getClass().getName();

        if(className.equals(FragmentDashboard.class.getName()))
            navigationView.getMenu().getItem(1).setChecked(true);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        Log.e("qwer", count + "");
        if (count == 1) {
            this.finishAffinity();
        } else {
            super.onBackPressed();
        }
    }

}
