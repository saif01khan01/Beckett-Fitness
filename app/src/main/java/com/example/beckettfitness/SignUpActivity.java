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

public class SignUpActivity extends AppCompatActivity {

    Button signUpRegister;

    private EditText regEmail;
    private EditText regPass;
    private EditText regPassConfirm;

    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpRegister = findViewById(R.id.sign_up_validate);

        regEmail = (EditText) findViewById(R.id.sign_up_email);
        regPass = (EditText) findViewById(R.id.sign_up_password);
        regPassConfirm = (EditText) findViewById(R.id.sign_up_password_confirm);

        mAuth = FirebaseAuth.getInstance();

        signUpRegister.setOnClickListener(view -> {
            createUser();
        });
    }
    private void createUser(){

        String email = regEmail.getText().toString();
        String pass = regPass.getText().toString();
        String passConf = regPassConfirm.getText().toString();

        if(TextUtils.isEmpty(email)){

            regEmail.setError("Email cannot be empty");
            regEmail.requestFocus();
        }
        else if (TextUtils.isEmpty(pass)) {

            regPass.setError("Password field cannot be empty");
            regPass.requestFocus();
        }else if(!passConf.equals(pass)){
            regPassConfirm.setError("Passwords do not match");
            regPassConfirm.requestFocus();
        }
        else {
            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SignUpActivity.this, "User successfully registered", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(SignUpActivity.this, "Registration error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }
}