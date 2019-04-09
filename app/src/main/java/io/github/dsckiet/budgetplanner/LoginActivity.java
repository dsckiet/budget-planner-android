package io.github.dsckiet.budgetplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private com.google.android.gms.common.SignInButton googleSignBtn;
    TextView newAccBtn;

    private EditText emailEt;
    private EditText passwordEt;

    CardView signInBtn;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();
        emailEt = findViewById(R.id.et_email);
        passwordEt = findViewById(R.id.et_password);

        signInBtn = findViewById(R.id.signin_btn);

        googleSignBtn = findViewById(R.id.google_signin_btn);

        googleSignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        newAccBtn = findViewById(R.id.new_acc_btn);

        newAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });
    }

    private void startSignIn(){
//        String email=emailEt.getText().toString();
//        String password=passwordEt.getText().toString();
//
////
//            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
////             if(!validateEmail() | !validatePassword() ){
////            return;
////        }
////        else {   @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if(!task.isSuccessful()){
////                        Snackbar.make(findViewById(R.id.snackbar), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                        Toast.makeText(LoginActivity.this,"SignIn failed",Toast.LENGTH_LONG).show();
//                    }else {
//                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
//                        finish();
//                        Toast.makeText(LoginActivity.this,"SignIn Successfully",Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
//        }
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();

    }
}
