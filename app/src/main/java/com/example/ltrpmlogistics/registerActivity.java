package com.example.ltrpmlogistics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class registerActivity extends AppCompatActivity {
    TextView alreadyhaveaccount;
    EditText email, Password, number;
    Button btnregister;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        alreadyhaveaccount = findViewById(R.id.alreadyhaveaccount);
        email = findViewById(R.id.email);
        Password = findViewById(R.id.Password);
        number = findViewById(R.id.number);
        btnregister = findViewById(R.id.btnlogin);

        mAuth = FirebaseAuth.getInstance();


        alreadyhaveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(registerActivity.this, MainActivity.class));
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Etemail=email.getText().toString();
                String Etpassword=Password.getText().toString();
                String Etnumber=number.getText().toString();

                if(Etemail.isEmpty()){
                    email.setError("please Enter your EmailID");
                    email.requestFocus();
                } else if (Etpassword.isEmpty()){
                    Password.setError("please enter your Password");
                    Password.requestFocus();
                }else if (Etnumber.isEmpty()){
                    number.setError("confirm your password");
                    number.requestFocus();
                }else{

                    mAuth.createUserWithEmailAndPassword(Etemail,Etpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                            }else {
                                Toast.makeText(registerActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(registerActivity.this,MainActivity.class));
            finish();
        }
    }
}