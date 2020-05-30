package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminCategoryActivity extends AppCompatActivity {


    private ImageView Room;
    private ImageView Airport,Bus,Train;
    private  ImageView  trippackage;
    private  ImageView Explore;
    private ImageView resort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_category);



        //   Airport=findViewById(R.id.t_airport);

        Explore=findViewById(R.id.t_explore);



        resort=findViewById(R.id.t_resort);

















        /*Airport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryActivity.this,FlightActivity.class);
                intent.putExtra("category","Airport");
                startActivity(intent);
            }
        });*/

        Explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewDetailActivity.class);
                intent.putExtra("category","Explore");
                startActivity(intent);

            }
        });



        resort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryActivity.this,AddNewDetailHotel.class);
                intent.putExtra("category","Resort");
                startActivity(intent);

            }
        });


    }
}
