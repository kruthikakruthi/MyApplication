package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewDetailActivity extends AppCompatActivity {

    private String CategoryName,Description,Dname,Address,saveCurrentDate,saveCurrentTime,Price;
    private Button Addnewdetail;
    private EditText InputName,InputAddress,InputDescription,InputPrice;
    private ImageView InputImage;
    private  static  final  int GalleryPick=1;
    private Uri ImageUri;
    private  String RandomKey,downloadImageUrl;
    private  StorageReference Imageref;
    private DatabaseReference DetailRef;
    //private ProgressDialog  loadingBar;

    final  LoadingDialog loadingDialog=new LoadingDialog(AdminAddNewDetailActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_detail);


        Toast.makeText(AdminAddNewDetailActivity.this, "welcome admin", Toast.LENGTH_SHORT).show();

        CategoryName= getIntent().getExtras().get("category").toString();
        Imageref= FirebaseStorage .getInstance().getReference().child("Trip Images");
        DetailRef= FirebaseDatabase.getInstance().getReference().child("planner");

        //loadingBar=new ProgressDialog(this);



        Addnewdetail=findViewById(R.id.add_new);
        InputImage=findViewById(R.id.d_image);
        InputDescription=findViewById(R.id.d_description);
        InputName=findViewById(R.id.d_name);
        InputAddress=findViewById(R.id.d_address);
        InputPrice=findViewById(R.id.d_price);


        //Toast.makeText(this, CategoryName, Toast.LENGTH_SHORT).show();
        
        
        
        //to add image
        
        InputImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) 
            {
                OpenGallery();
            }
        });

        Addnewdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                ValidateProductData();
            }
        });
    }

    private void OpenGallery()
    {

        Intent galleryintent=new Intent();
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null)
        {
            ImageUri=data.getData();
            InputImage.setImageURI(ImageUri);
        }
    }
    private  void ValidateProductData()
    {
        Description=InputDescription.getText().toString();
       Address =InputAddress.getText().toString();
        Dname=InputName.getText().toString();
        Price=InputPrice.getText().toString();

        if(ImageUri==null)
        {
            Toast.makeText(this, "please upload the image", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "please give the description", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Address))
        {
            Toast.makeText(this, "please give the PrevalentAddress", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Dname))
        {
            Toast.makeText(this, "please give the name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "please give the price", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreImageInformation();
        }

    }

    private void StoreImageInformation()
    {



        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
         saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());


        RandomKey=saveCurrentDate=saveCurrentTime;

        final StorageReference filepath=Imageref.child(ImageUri.getLastPathSegment()+RandomKey+"jpg");

        final UploadTask uploadTask=filepath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message=e.toString();
                loadingDialog.dismissDialog();
                Toast.makeText(AdminAddNewDetailActivity.this, "Error:"+message, Toast.LENGTH_SHORT).show();
                //loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                loadingDialog.dismissDialog();
                Toast.makeText(AdminAddNewDetailActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();


                Task<Uri>urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                       if(!task.isSuccessful())
                       {
                           throw  task.getException();

                       }

                       downloadImageUrl= filepath.getDownloadUrl().toString();
                       return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            downloadImageUrl=task.getResult().toString();
                          //  Toast.makeText(this,"image has been stored to databse successfully", Toast.LENGTH_SHORT).show();

                        SaveInfoToDatabase();
                        }



                    }
                });

            }
        });




    }

    private void SaveInfoToDatabase() {

        HashMap<String,Object>DetailMap= new HashMap<>();
        DetailMap.put("pid",RandomKey);
        DetailMap.put("date",saveCurrentDate);
        DetailMap.put("time",saveCurrentTime);
        DetailMap.put("description",Description);
        DetailMap.put("image",downloadImageUrl);
        DetailMap.put("category",CategoryName);
        DetailMap.put("address",Address);
        DetailMap.put("dname",Dname);
        DetailMap.put("price",Price);

        DetailRef.child(RandomKey).updateChildren(DetailMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {

                            Intent intent= new Intent(AdminAddNewDetailActivity.this,AdminCategoryActivity.class);
                            startActivity(intent);
                            finish();
                            loadingDialog.dismissDialog();
                              Toast.makeText(AdminAddNewDetailActivity.this,"details has been stored to databse successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            //loadingBar.dismiss();
                            String message=task.getException().toString();
                            //Intent intent= new Intent(AdminAddNewDetailActivity.this,MainActivity.class);
                            //startActivity(intent);
                            loadingDialog.dismissDialog();
                              Toast.makeText(AdminAddNewDetailActivity.this,"Error"+message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}
