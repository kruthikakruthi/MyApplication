package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.myapplication.Holder.RetrieveDataHolder;
import com.example.myapplication.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class trip_details_Activity extends AppCompatActivity {

    private Button Nextbtn;
    private ImageView TripImage;
    private ElegantNumberButton numberButton;
    private TextView  TripName,TripAddress,TripDescription,TripPrice;
    private String PlaannerID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details_);

        PlaannerID=getIntent().getStringExtra("pid");

        Nextbtn=findViewById(R.id.trip_continue_btn_details);
        TripImage=findViewById(R.id.trip_image_details);
        TripName=findViewById(R.id.trip_name_details);
        TripDescription=findViewById(R.id.trip_description_details);
        TripAddress=findViewById(R.id.trip_address_details);
        TripPrice=findViewById(R.id.trip_price_details);
        numberButton=findViewById(R.id.elegant_number_btn);



        getplannerDetails(PlaannerID);



        Nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddtoTripList();

            }
        });









    }

    private void AddtoTripList()
    {
 String savecurrenttime,savecurrntdate;
        Calendar tripdate= Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("MMM dd, YYYY");
        savecurrntdate=currentDate.format(tripdate.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
       savecurrenttime=currentDate.format(tripdate.getTime());

     final DatabaseReference triplistref= FirebaseDatabase.getInstance().getReference().child("Trip List");

       final HashMap<String,Object> triplistmap=new HashMap<>();
       triplistmap.put("pid",PlaannerID);
       triplistmap.put("dname",TripName.getText().toString());
       triplistmap.put("address",TripAddress.getText().toString());
       triplistmap.put("description",TripAddress.getText().toString());


       triplistmap.put("date",savecurrntdate);
       triplistmap.put("time",savecurrenttime);
       triplistmap.put("peoples",numberButton.getNumber());
       triplistmap.put("price",TripPrice.getText().toString());
       triplistmap.put("discount","");



       triplistref.child("Users View").child(Prevalent.currentOnlineUser.getPhone())
               .child("planner").child(PlaannerID).updateChildren(triplistmap).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task)
           {
               if(task.isSuccessful())
               {
                   triplistref.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                           .child("planner").child(PlaannerID).updateChildren(triplistmap)
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task)
                               {
                                   Toast.makeText(trip_details_Activity.this,"added to trip list",Toast.LENGTH_SHORT).show();





                                   Intent intent=new Intent(trip_details_Activity.this,ConfirmOrderActivity.class);
                                   startActivity(intent);
                               }
                           });
               }
           }
       });
    }

    private void getplannerDetails(String plaannerID) {

        DatabaseReference planneref= FirebaseDatabase.getInstance().getReference().child("planner");
        planneref.child(plaannerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    RetrieveDataHolder retrieveDataHolder=dataSnapshot.getValue(RetrieveDataHolder.class);

                    TripName.setText(retrieveDataHolder.getDname());
                    TripDescription.setText(retrieveDataHolder.getDescription());
                    TripAddress.setText(retrieveDataHolder.getAddress());
                    TripPrice.setText(retrieveDataHolder.getPrice());


                    Picasso.get().load(retrieveDataHolder.getImage()).into(TripImage);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
