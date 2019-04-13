package io.github.dsckiet.budgetplanner;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEt;
    private EditText passwordEt;
    CardView signUpBtn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        emailEt = findViewById(R.id.edittext_email_signup);
        passwordEt = findViewById(R.id.edittext_password_signup);
        signUpBtn = findViewById(R.id.signup_btn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registeruser();
            }
        });

    }

//    private boolean validateEmail() {
//        String emailInput = textLayoutEmail.getEditText().getText().toString().trim();
//        if (emailInput.isEmpty()) {
//            textLayoutEmail.setError("  Field can't be Empty");
//            return false;
//
//        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
//            textLayoutEmail.setError("   Please enter a valid email address");
//            return false;
//        } else {
//            textLayoutEmail.setError(null);
//            return true;
//        }
//
//    }
//
//    private boolean validatePassword() {
//        String passInput = textLayoutPass.getEditText().getText().toString().trim();
//        if (passInput.isEmpty()) {
//            textLayoutPass.setError("   Field can't be Empty");
//            return false;
//        } else {
//            textLayoutPass.setError(null);
//            return true;
//        }
//
//    }

    private void registeruser() {
        String email = emailEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();

//        if(!validateUsername() | !validateEmail() | !validatePassword()){
//            return;
//        } else {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Registered successfully ", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Can't be Registered", Toast.LENGTH_LONG).show();
                        }
                    }
                });
//        }
    }
}
