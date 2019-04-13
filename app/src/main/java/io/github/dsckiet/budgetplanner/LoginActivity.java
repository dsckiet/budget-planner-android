package io.github.dsckiet.budgetplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private com.google.android.gms.common.SignInButton googleSignBtn;
    TextView newAccBtn;

    private static final int RC_SIGN_IN = 1001;
    private EditText emailEt;
    private EditText passwordEt;

    CardView signInBtn;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();

        emailEt = findViewById(R.id.edittext_email_login);
        passwordEt = findViewById(R.id.edittext_password_login);

        signInBtn = findViewById(R.id.signin_btn);
        googleSignBtn = findViewById(R.id.google_signin_btn);

//        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken("default web client id")
//                .requestEmail()
//                .build();
//        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(),gso);

        googleSignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//                startActivityForResult(signInIntent,RC_SIGN_IN);

            }
        });

//        mAuthListener=new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//
//                if(firebaseAuth.getCurrentUser()!=null){
//                    //Toast.makeText(MainActivity.this, "User already signed In", Toast.LENGTH_SHORT).show();
//                    //startActivity(new Intent(MainActivity.this,dashboard.class));
//                }
//
//            }
//        };
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
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                firebaseAuthWithGoogle(account);
//            } catch (ApiException e) {
//                Log.w("Google Activity", "Google sign in failed", e);
//                Toast.makeText(LoginActivity.this, "Google sign in failed ", Toast.LENGTH_SHORT).show();
//
//            }
//        }
//    }
//
//    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        Log.d("Google Activity", "firebaseAuthWithGoogle:" + acct.getId());
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//
//                            Log.d("Google Activity", "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            Toast.makeText(LoginActivity.this, "signed in successfully ", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Log.w("Google Activity", "signInWithCredential:failure", task.getException());
//                            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
//    }

    private void startSignIn(){
        String email=emailEt.getText().toString();
        String password=passwordEt.getText().toString();

//
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//             if(!validateEmail() | !validatePassword() ){
//            return;
//        }
//        else {   @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
//                        Snackbar.make(findViewById(R.id.snackbar), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        Toast.makeText(LoginActivity.this,"SignIn failed",Toast.LENGTH_LONG).show();
                    }else {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                        Toast.makeText(LoginActivity.this,"SignIn Successfully",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }


    }

