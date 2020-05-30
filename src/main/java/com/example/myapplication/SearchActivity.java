package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Holder.RetrieveDataHolder;
import com.example.myapplication.Holder.TripViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView    Searchlist;
 private EditText searchtext;
 private ImageView serchimage;
private String searchinput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        searchtext=findViewById(R.id.home_text);
        serchimage=findViewById(R.id.home_search);
        Searchlist=findViewById(R.id.search_list);
        Searchlist.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        serchimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchinput=searchtext.getText().toString();
                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("planner");

        FirebaseRecyclerOptions<RetrieveDataHolder>options=
                new  FirebaseRecyclerOptions.Builder<RetrieveDataHolder>()
                .setQuery(reference.orderByChild("dname").startAt(searchinput),RetrieveDataHolder.class)
                .build();


        FirebaseRecyclerAdapter<RetrieveDataHolder, TripViewHolder>adapter=
                 new FirebaseRecyclerAdapter<RetrieveDataHolder, TripViewHolder>(options) {
                     @Override
                     protected void onBindViewHolder(@NonNull TripViewHolder holder, int i, @NonNull final RetrieveDataHolder triplist) {

                         holder.txtName.setText(triplist.getDname());
                         holder.price.setText(triplist.getPrice());

                         Picasso.get().load(triplist.getImage()).into(holder.imageView);


                         holder.itemView.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View view) {
                                 Intent intent= new Intent(SearchActivity.this,trip_details_Activity.class);
                                 intent.putExtra("pid",triplist.getPid());
                                 startActivity(intent);
                             }
                         });

                     }

                     @NonNull
                     @Override
                     public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feauterd_card_design,parent,false);
                         TripViewHolder holder=new TripViewHolder(view);

                         return holder;
                     }
                 };
        Searchlist.setAdapter(adapter);
        adapter.startListening();
    }
}
