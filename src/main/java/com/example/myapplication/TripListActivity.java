package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Holder.TripListViewHolder;
import com.example.myapplication.Model.TripList;
import com.example.myapplication.Prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TripListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private  RecyclerView.LayoutManager layoutManager;
    private Button nextbutton;

    private  TextView txtTotalPrice;

    private String totalamount="";



    private int overTotalprice=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);






        recyclerView=findViewById(R.id.triplist);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        nextbutton=findViewById(R.id.next_btn);
        txtTotalPrice=findViewById(R.id.totalamount_triplist);


nextbutton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view)
    {


        txtTotalPrice.setText("Total price = $"+String.valueOf(overTotalprice));
        Intent intent=new Intent(TripListActivity.this,ConfirmOrderActivity.class);
        intent.putExtra("Total Price",String.valueOf(overTotalprice));
     //   Toast.makeText(TripListActivity.this,,Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }
});


    }

    @Override
    protected void onStart() {
        super.onStart();


        final DatabaseReference triplistref= FirebaseDatabase.getInstance().getReference().child("Trip List");

        FirebaseRecyclerOptions<TripList> options= new FirebaseRecyclerOptions.Builder<TripList>()
                .setQuery(triplistref.child("Users View")
                        .child(Prevalent.currentOnlineUser.getPhone()).child("planner"),TripList.class).build();


        FirebaseRecyclerAdapter<TripList, TripListViewHolder> adapter= new FirebaseRecyclerAdapter<TripList, TripListViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final TripListViewHolder tripListViewHolder, int i, @NonNull final TripList tripList)
            {
                  tripListViewHolder.txttriplistpeoples.setText("Peoples ="+tripList.getPeoples());
                  tripListViewHolder.txttriplistaddress.setText("Destination ="+tripList.getAddress());
                  tripListViewHolder.txttriplistname.setText("Place ="+tripList.getDname());
                  tripListViewHolder.txttriplistprice.setText("price="+tripList.getPrice());

                int  oneTypeTripPrice=((Integer.valueOf(tripList.getPrice())))*Integer.valueOf(tripList.getPeoples());
                overTotalprice=overTotalprice+oneTypeTripPrice;

                  tripListViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view)
                      {
                          CharSequence options[]=new CharSequence[]
                                  {
                                     "Edit",
                                     "Remove"
                                  };

                          AlertDialog.Builder builder=new AlertDialog.Builder(TripListActivity.this);
                          builder.setTitle("List Options");

                          builder.setItems(options, new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialogInterface, int i)
                              {
                                  if(i==0)
                                  {
                                      Intent intent=new Intent(TripListActivity.this,trip_details_Activity.class);
                                      intent.putExtra("pid",tripList.getPid());
                                      startActivity(intent);
                                  }

                                  if(i==1)
                                  {
                                      triplistref.child("Users View").child(Prevalent.currentOnlineUser.getPhone())
                                              .child("planner")
                                              .child(tripList.getPid())
                                              .removeValue()
                                              .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                  @Override
                                                  public void onComplete(@NonNull Task<Void> task)
                                                  {
                                                      if(task.isSuccessful())
                                                      {
                                                          Toast.makeText(TripListActivity.this,"Item removed from bucketlist",Toast.LENGTH_SHORT).show();

                                                          Intent intent=new Intent(TripListActivity.this,HomeActivity.class);

                                                          startActivity(intent);
                                                      }
                                                  }
                                              });
                                  }

                              }
                          });

                          builder.show();
                      }
                  });

            }

            @NonNull
            @Override
            public TripListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.triplist_items_laout,parent,false);
                TripListViewHolder tripListViewHolder= new TripListViewHolder(view);
                return tripListViewHolder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
