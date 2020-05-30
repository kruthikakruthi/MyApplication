package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {


    private TextInputLayout InputName, InputUsername, InputEmail, InputPhoneNumber, InputPassword;
   private  Button CreateAccountButton, rlogin;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
   // private ProgressDialog loadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        InputName = findViewById(R.id.register_name_input);
       InputUsername = findViewById(R.id.register_username_input);
        InputEmail = findViewById(R.id.register_email_input);
        InputPhoneNumber = findViewById(R.id.register_phone_number_input);
        InputPassword = findViewById(R.id.register_password_input);
        CreateAccountButton = findViewById(R.id.register_btn);
        rlogin = findViewById(R.id.reg_login);
      //  loadingBar=new ProgressDialog(this);





        rlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, loginActivity.class);
                startActivity(intent);
            }
        });

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });

    }

    private void CreateAccount() {
        String name = InputName.getEditText().getText().toString();
        String username = InputUsername.getEditText().getText().toString();
        String email = InputEmail.getEditText().getText().toString();
        String phone = InputPhoneNumber.getEditText().getText().toString();
        String password = InputPassword.getEditText().getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "enter your name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "username cannot be empty", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "email cannot be empty", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "enter your phoneno", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "password cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else{
           // loadingBar.setTitle("Create Account");
            //loadingBar.setMessage("please wait,while we are checking the credentials");
            //loadingBar.setCanceledOnTouchOutside(false);
            //loadingBar.show();

            ValidatephoneNumber(name,username,email,phone,password);
        }

    }

    private void ValidatephoneNumber(final String name, final String username, final String email, final String phone, final String password) {


        final DatabaseReference RootRef;
        RootRef=FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String,Object> userdatamap=new HashMap<>();
                    userdatamap.put("phone",phone);
                    userdatamap.put("name",name);
                    userdatamap.put("username",username);
                    userdatamap.put("email",email);
                    userdatamap.put("password",password);


                    RootRef.child("Users").child(phone).updateChildren(userdatamap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                       Toast.makeText(SignupActivity.this,"your account as been created",Toast.LENGTH_SHORT).show();
                                        // loadingBar.dismiss();
                                        Intent intent=new Intent(SignupActivity.this,loginActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        // loadingBar.dismiss();
                                        Toast.makeText(SignupActivity.this,"network error",Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(SignupActivity.this,"this "+phone+"  already exists",Toast.LENGTH_SHORT).show();
                   // loadingBar.dismiss();
                    Toast.makeText(SignupActivity.this,"please try again using another account",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
