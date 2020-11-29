package com.example.date1b;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText fullName,email,password,phone;
    Button registerBtn,goToLogin;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CheckBox isAdmin,isUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        //field register from the users
        fullName = findViewById(R.id.registerName);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        phone = findViewById(R.id.registerPhone);
//        registerBtn = findViewById(R.id.registerBtn);
        goToLogin = findViewById(R.id.gotoLogin);
//        isAdmin = findViewById(R.id.isAdmin);
//        isUser = findViewById(R.id.isUser);

        // Check box fill in and change boolean
        isAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(buttonView.isChecked())isUser.setChecked(false);

            }
            });
        isUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked())isAdmin.setChecked(false);

            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField(fullName);
                checkField(email);
                checkField(password);
                checkField(phone);

                //Check if admin/user set
                if(!(isAdmin.isChecked() ||  isUser.isChecked())){
                    Toast.makeText(Register.this, "Select account type", Toast.LENGTH_SHORT).show();
                }

                if(valid){
                    //start the user registration
                    System.out.println(password.getText().toString());


                    fAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            //go to curr User in Database
                            FirebaseUser user = fAuth.getCurrentUser();
                            Toast.makeText(Register.this,"Account created",Toast.LENGTH_SHORT).show();
                            DocumentReference dr = fStore.collection("Users").document(user.getUid());

                            Map<String,Object> userInfo = new HashMap<>();
                            userInfo.put("FullName",fullName.getText().toString());
                            userInfo.put("UserEmail",email.getText().toString());
                            userInfo.put("PhoneNumber",phone.getText().toString());
                            //If choose user
                            if(isUser.isChecked())userInfo.put("isUser","1");
                            if(isAdmin.isChecked())userInfo.put("isAdmin","0");
                            dr.set(userInfo);
                            //after register go to login
                            startActivity(new Intent(getApplicationContext(),Login.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(Register.this,"Failed to create account created",Toast.LENGTH_SHORT).show();}
                    });
                }
            }
        });
        goToLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

    }

    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }


}