package com.example.beckettfitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText email;
    EditText pass;
    Button signIn;
    Button signUp;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.sign_in_email);
        pass = findViewById(R.id.sign_in_pass);
        signIn = (Button) findViewById(R.id.login);
        signUp = (Button) findViewById(R.id.sign_up);

        mAuth = FirebaseAuth.getInstance();

        signIn.setOnClickListener(view -> {
            loginUser();
                });

        signUp.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });
    }

    private void loginUser() {
        String email = this.email.getText().toString();
        String pass = this.pass.getText().toString();

        if(TextUtils.isEmpty(email)){

            this.email.setError("Email cannot be empty");
            this.email.requestFocus();
        } else if (TextUtils.isEmpty(pass)) {

            this.pass.setError("Password field cannot be empty");
            this.pass.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "User successfully Logged in", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainScreen.class));
                    } else {
                        Toast.makeText(LoginActivity.this, "Login error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}