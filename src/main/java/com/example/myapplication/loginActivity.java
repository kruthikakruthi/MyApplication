package com.example.myapplication;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Model.Users;
import com.example.myapplication.Prevalent.Prevalent;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class loginActivity extends AppCompatActivity {

    Button callsignup;
    ImageView image;
    TextView logo;
    private Button LoginButton;
    private TextInputLayout InputNumber,InputPassword;
    private CheckBox chkBoxRememberMe;

    private  TextView AdminLink,NotAdminLink;
    //private ProgressDialog  loadingBar;

    private  String parentDbname="Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);


        callsignup = findViewById(R.id.signinloginscreen);

        //transition from login into signin variables

        image = findViewById(R.id.imagelogin);
        logo = findViewById(R.id.textlogin);



       InputNumber = (TextInputLayout) findViewById(R.id.login_phone_number_input);
       InputPassword= (TextInputLayout) findViewById(R.id.login_password_input);
       LoginButton =(Button) findViewById(R.id.login_btn);
       AdminLink = findViewById(R.id.admin_panel_link);
       NotAdminLink = findViewById(R.id.not_admin_panel_link);


        chkBoxRememberMe = findViewById(R.id.remember_me_chkb);
        Paper.init(this);




        callsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(loginActivity.this, SignupActivity.class);
                //startActivity(intent1);

                //transition from login into signin passing parameters

                Pair[] pairs = new Pair[5];
                pairs[0] = new Pair<View, String>(image, "t_img");
                pairs[1] = new Pair<View, String>(logo, "t_text");
                pairs[2] = new Pair<View, String>(InputNumber, "t_name");
                pairs[3] = new Pair<View, String>(InputPassword, "t_pass");
                pairs[4] = new Pair<View, String>(LoginButton, "t_login");


                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(loginActivity.this, pairs);
                startActivity(intent1, options.toBundle());


            }
        });


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LoginUser();
            }
        });
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbname="Admins";
            }
        });
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("Login ");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbname="Users";
            }
        });
    }

    private void LoginUser() {
        String phone = InputNumber.getEditText().getText().toString();
        String password = InputPassword.getEditText().getText().toString();


        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "username cannot be empty", Toast.LENGTH_SHORT).show();
        }
       else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "password cannot be empty", Toast.LENGTH_SHORT).show();
        }
       else
        {


            AllowAccessToAccount(phone,password);
        }
    }

    private void AllowAccessToAccount(final String  phone, final String password)
    {

        if(chkBoxRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey,phone);
            Paper.book().write(Prevalent.UserPasswordKey,password);

        }

        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();



        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(parentDbname).child(phone).exists())
                {
                    Users userData= dataSnapshot.child(parentDbname).child(phone).getValue(Users.class);


                    if(userData.getPhone().equals(phone))
                    {
                        if(userData.getPassword().equals(password))
                        {

                            if(parentDbname.equals("Admins"))
                            {
                              Toast.makeText(loginActivity.this, "welcome admin login successfully", Toast.LENGTH_LONG).show();

                                Intent intent55= new Intent(loginActivity.this,AdminCategoryActivity.class);
                                 startActivity(intent55);
                            }
                            else if(parentDbname.equals("Users"))
                            {
                                 Toast.makeText(loginActivity.this, "login successfully", Toast.LENGTH_SHORT).show();
                                //loadingBar.dismiss();
                                Intent intent= new Intent(loginActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser=userData;
                                 startActivity(intent);
                            }

                        }
                        else
                        {

                             Toast.makeText(loginActivity.this, "wrong password", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
                else
                {
                    Toast.makeText(loginActivity.this, "account does not exist", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}