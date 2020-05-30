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
import com.example.myapplication.Holder.RetrieveDataOfHotelHolder;
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

public class Hotel_details_Activity extends AppCompatActivity {
    private Button Nextbtn;
    private ImageView HotelImage;
    private ElegantNumberButton HotelnumberButton;
    private TextView HotelName,HotelAddress,HotelDescription,HotelPrice;
    private String plaannerID="";

    private String totalamount="";



    private int overTotalprice=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_details_);


        plaannerID=getIntent().getStringExtra("pid");

        Nextbtn=findViewById(R.id.hotel_continue_btn_details);
   HotelImage=findViewById(R.id.hotel_image_details);
        HotelName=findViewById(R.id.hotel_name_details);
       HotelDescription=findViewById(R.id.hotel_description_details);
        HotelAddress=findViewById(R.id.hotel_address_details);
     HotelPrice=findViewById(R.id.hotel_price_details);
        HotelnumberButton=findViewById(R.id.hotel_elegant_number_btn);



        getplannerDetails(plaannerID);



        Nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddtoHotelList();

            }
        });









    }

    private void AddtoHotelList()
    {
        String savecurrenttime,savecurrntdate;
        Calendar hoteldate= Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("MMM dd, YYYY");
        savecurrntdate=currentDate.format(hoteldate.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime=currentDate.format(hoteldate.getTime());

        final DatabaseReference hotellistref= FirebaseDatabase.getInstance().getReference().child(" Book List");

        final HashMap<String,Object> hotelmap=new HashMap<>();
       hotelmap.put("pid",plaannerID);
       hotelmap.put("hname",HotelName.getText().toString());
        hotelmap.put("haddress",HotelAddress.getText().toString());
        hotelmap.put("description",HotelDescription.getText().toString());
        hotelmap.put("date",savecurrntdate);
      hotelmap.put("time",savecurrenttime);
        hotelmap.put("hpeoples",HotelnumberButton.getNumber());
        hotelmap.put("hprice",HotelPrice.getText().toString());
        hotelmap.put("discount","");




        hotellistref.child("Users View").child(Prevalent.currentOnlineUser.getPhone())
                .child("stay").child(plaannerID).updateChildren(hotelmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                  hotellistref.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                            .child("stay").child(plaannerID).updateChildren(hotelmap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    Toast.makeText(Hotel_details_Activity.this,"successfully booked your  hotel",Toast.LENGTH_SHORT).show();





                                    Intent intent=new Intent(Hotel_details_Activity.this,ConfirmOrderActivity.class);
                                    startActivity(intent);

                                }
                            });
                }
            }
        });


       /* final DatabaseReference triplistref= FirebaseDatabase.getInstance().getReference().child("Book List");

        FirebaseRecyclerOptions<Address> options= new FirebaseRecyclerOptions.Builder<Address>()
                .setQuery(triplistref.child("Users View")
                        .child(PrevalentHotelAddress.currenthotelAddress.getHprice()).child("stay"),Address.class).build();*/


    }



    private void getplannerDetails(String plaannerID) {

        DatabaseReference planneref= FirebaseDatabase.getInstance().getReference().child("stay");
        planneref.child(plaannerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    RetrieveDataOfHotelHolder retrieveDataOfHotelHolder=dataSnapshot.getValue(RetrieveDataOfHotelHolder.class);

                   HotelName.setText(retrieveDataOfHotelHolder.getHname());
                   HotelDescription.setText(retrieveDataOfHotelHolder.getdescription());
                    HotelAddress.setText(retrieveDataOfHotelHolder.getHaddress());
                    HotelPrice.setText(retrieveDataOfHotelHolder.getHprice());


                    Picasso.get().load(retrieveDataOfHotelHolder.getHimage()).into(HotelImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
