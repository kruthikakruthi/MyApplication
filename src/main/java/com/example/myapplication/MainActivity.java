package com.example.myapplication;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Model.Users;
import com.example.myapplication.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {


    private  static  int SPLASH_SCREEN=5000;

    //private ProgressDialog  loadingBar;
    Animation topAnim,BottomAnim;
    private ImageView mimage;
   private TextView mlogo,mslogan;
    final  LoadingDialog loadingDialog=new LoadingDialog(MainActivity.this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        BottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);


        mimage=findViewById(R.id.logo_image1);
        mlogo=findViewById(R.id.textlogoname);
        mslogan=findViewById(R.id.textslogan);
        mimage.setAnimation(topAnim);
        mlogo.setAnimation(BottomAnim);
        mslogan.setAnimation(BottomAnim);

      //  Intent gosign= new Intent(MainActivity.this,loginActivity.class);
       // startActivity(gosign);


/*

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Pair[] pairs=new Pair[2];
                pairs[0]=new  Pair<View,String>(mimage,"t_img1");
                pairs[1]=new  Pair<View,String>(mlogo,"t_text1");
                Intent intent=new Intent(MainActivity.this,loginActivity.class);
                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                startActivity(intent,options.toBundle());
                finish();

            }
        },SPLASH_SCREEN);*/









        //loadingBar=new ProgressDialog(this);

        Paper.init(this);
        String UserPhoneKey=Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey=Paper.book().read(Prevalent.UserPasswordKey);



        if (UserPhoneKey != "" && UserPasswordKey != "") {
            if (!TextUtils.isEmpty(UserPhoneKey) && (!TextUtils.isEmpty(UserPasswordKey))) {

                AllowAccess(UserPhoneKey, UserPasswordKey);





            }



        }
        Intent intent= new Intent(MainActivity.this,loginActivity.class);
        startActivity(intent);







    }

    private void AllowAccess(final String phone, final String password) {

        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();



        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("Users").child(phone).exists())
                {
                    Users userData= dataSnapshot.child("Users").child(phone).getValue(Users.class);


                    if(userData.getPhone().equals(phone))
                    {
                        if(userData.getPassword().equals(password))
                        {
                             Toast.makeText(MainActivity.this, "your already logged in", Toast.LENGTH_SHORT).show();

                            Intent intent= new Intent(MainActivity.this, HomeActivity.class);
                            Prevalent.currentOnlineUser=userData;
                            startActivity(intent);
                        }
                        else
                        {

                             Toast.makeText(MainActivity.this, "wrong password", Toast.LENGTH_SHORT).show();
                           Intent gosign= new Intent(MainActivity.this,SignupActivity.class);
                            startActivity(gosign);
                        }
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "account does not exist", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Pair[] pairs=new Pair[2];
                            pairs[0]=new  Pair<View,String>(mimage,"t_img1");
                            pairs[1]=new  Pair<View,String>(mlogo,"t_text1");

                            Intent login=new Intent(MainActivity.this,loginActivity.class);
                            ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                            startActivity(login,options.toBundle());
                            finish();

                        }
                    },SPLASH_SCREEN);






                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }
}
