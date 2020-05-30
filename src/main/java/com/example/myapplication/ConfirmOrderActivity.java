package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Holder.HotelViewHolder;
import com.example.myapplication.Holder.RetrieveDataOfHotelHolder;
import com.example.myapplication.Prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ConfirmOrderActivity extends AppCompatActivity
{

    private EditText ConfirmName,ConfirmPhn,ConfirmEmail,ConfirmLocation,Confirmprice;
    private Button Confirmbtn;
    private ImageView searchimage;


    private DatabaseReference reference;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private  String searchinput;


    private TextView people;

    private String totalamount="";
    private String totalamounthotel="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        totalamount=getIntent().getStringExtra("Total Price");

        Confirmprice=findViewById(R.id.confirm_price);
        Confirmprice.setText(totalamount);

        Confirmbtn=findViewById(R.id.confirm_btn);
        ConfirmName=findViewById(R.id.confirm_name);
        ConfirmPhn=findViewById(R.id.confirm_phn);
        ConfirmEmail=findViewById(R.id.confirm_email);
       ConfirmLocation=findViewById(R.id.confirm_present_location);
        searchimage=findViewById(R.id.searchimage);
        recyclerView = (RecyclerView) findViewById(R.id.featured_recycler2);





        InfoDisplay(ConfirmName,ConfirmPhn,ConfirmEmail);


        Confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfoBooking();
            }
        });


        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);

        reference = FirebaseDatabase.getInstance().getReference().child("stay");


        searchimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchinput=ConfirmLocation.getText().toString();

                onStart();

            }
        });

    }

    private void UserInfoBooking() {


        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Book List");
        HashMap<String,Object> usermap=new HashMap<>();
        usermap.put("name",ConfirmName.getText().toString());
        usermap.put("phone",ConfirmPhn.getText().toString());
        usermap.put("email",ConfirmEmail.getText().toString());


        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(usermap);
        //loadingDialog.dismissDialog();


            startActivity(new Intent(ConfirmOrderActivity.this, paymentActivity.class));
            Toast.makeText(ConfirmOrderActivity.this, "successfully booked your details", Toast.LENGTH_SHORT).show();
            finish();


    }

    private void InfoDisplay( final EditText ConfirmName, final EditText ConfirmPhn, final EditText ConfirmEmail)
    {
        DatabaseReference UserRef= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());



        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String name = dataSnapshot.child("name").getValue().toString();
                    String phn = dataSnapshot.child("phone").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();
                 //   String address=dataSnapshot.child("address").getValue().toString();

                  ConfirmName.setText(name);
                ConfirmPhn.setText(phn);
                    ConfirmEmail.setText(email);
                   // ConfirmLocation.setText(address);






            }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }



    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference refer= FirebaseDatabase.getInstance().getReference().child("stay");



        FirebaseRecyclerOptions<RetrieveDataOfHotelHolder>options=
                new FirebaseRecyclerOptions.Builder<RetrieveDataOfHotelHolder>()
                        .setQuery(refer.orderByChild("haddress").startAt(searchinput),RetrieveDataOfHotelHolder.class)
                        .build();



        FirebaseRecyclerAdapter<RetrieveDataOfHotelHolder, HotelViewHolder> adapter= new FirebaseRecyclerAdapter<RetrieveDataOfHotelHolder, HotelViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HotelViewHolder holder, int position, @NonNull final RetrieveDataOfHotelHolder model) {





                holder.txtName.setText(model.getHname());
                holder.price.setText(model.getHprice());
                holder.txtaddress.setText(model.getHaddress());

                Picasso.get().load(model.getHimage()).into(holder.imageView);

//                Confirmprice.setText(PrevalentHotelAddress.currenthotelAddress.getHprice());



                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent= new Intent(ConfirmOrderActivity.this,Hotel_details_Activity.class);
                        intent.putExtra("pid",model.getPid());
                        startActivity(intent);

                    }
                });

            }

            @NonNull
            @Override
            public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_card_design,parent,false);
              HotelViewHolder holder=new HotelViewHolder(view);

                return holder;
            }
        };




        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }




}
