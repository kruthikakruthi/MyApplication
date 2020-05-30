package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Holder.RetrieveDataHolder;
import com.example.myapplication.Holder.TripViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    private DatabaseReference reference;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private ImageView searchbutton;
    public EditText Searchtext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);





        Toolbar toolbar = findViewById(R.id.toolbar);


        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,TripListActivity.class);

                startActivity(intent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.drawerOpen,R.string.drawerCloser);

                drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);






        //for accessing data from firebase of username and image

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = findViewById(R.id.user_profile_image);


//    userNameTextView.setText(Prevalent.currentOnlineUser.getUsername());

     //   Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);




        recyclerView = (RecyclerView) findViewById(R.id.featured_recycler1);

        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        reference = FirebaseDatabase.getInstance().getReference().child("planner");




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<RetrieveDataHolder>options=
                new FirebaseRecyclerOptions.Builder<RetrieveDataHolder>()
                .setQuery(reference,RetrieveDataHolder.class)
                .build();



        FirebaseRecyclerAdapter<RetrieveDataHolder, TripViewHolder> adapter= new FirebaseRecyclerAdapter<RetrieveDataHolder, TripViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TripViewHolder holder, int position, @NonNull final RetrieveDataHolder model) {


                holder.txtName.setText(model.getDname());
                holder.price.setText(model.getPrice());

                Picasso.get().load(model.getImage()).into(holder.imageView);


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent= new Intent(HomeActivity.this,trip_details_Activity.class);
                        intent.putExtra("pid",model.getPid());
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




        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id=menuItem.getItemId();
        if(id==R.id.nav_yourtrip)
        {
            Intent intent=new Intent(HomeActivity.this,TripListActivity.class);

            startActivity(intent);
        }
         else if(id==R.id.nav_search)
        {
            Intent intent=new Intent(HomeActivity.this,SearchActivity.class);

            startActivity(intent);
        }

         else if(id==R.id.nav_settings)
        {
            Intent intent=new Intent(HomeActivity.this,SettingsActvity.class);

            startActivity(intent);
        }
         else if(id==R.id.nav_logout)
        {
            Paper.book().destroy();
            Intent intent=new Intent(HomeActivity.this,loginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }


         DrawerLayout drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
         drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}
