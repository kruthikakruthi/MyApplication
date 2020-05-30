package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActvity extends AppCompatActivity {
   private EditText Fullnameedittext,emailedittext,phonenumberedittext;
    private  TextView profilechangetextbtn,closetextbbtn,savetextbtn;
    private CircleImageView profileImageView;

    private Uri imageuri;
    private  String myurl="";
    private StorageTask uploadTask;
    private StorageReference storageprofilepictureref;
    private  String checker="";
    final  LoadingDialog loadingDialog=new LoadingDialog(SettingsActvity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_actvity);

      storageprofilepictureref= FirebaseStorage.getInstance().getReference().child("profile pictures");

        profileImageView=findViewById(R.id.settings_profile_image);
        Fullnameedittext=findViewById(R.id.settings_full_name);
        phonenumberedittext=findViewById(R.id.settings_phone_number);
        emailedittext=findViewById(R.id.settings_email);
        profilechangetextbtn=findViewById(R.id.profile_image_change_btn);
        savetextbtn=findViewById(R.id.update_account_settings_button);
       closetextbbtn=findViewById(R.id.close_settings_btn);




       userInfoDisplay(profileImageView,Fullnameedittext,phonenumberedittext,emailedittext);



       closetextbbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });


       savetextbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(checker.equals("clicked"))
               {
                   useInfoSaved();
               }
               else
               {

               }
           }
       });

      profilechangetextbtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              checker="clicked";

              CropImage.activity(imageuri)
                      .setAspectRatio(1,1)
              .start(SettingsActvity.this);

          }
      });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageuri= result.getUri();

            profileImageView.setImageURI(imageuri);
        }
        else

        {
            Toast.makeText(SettingsActvity.this,"Error Try Again",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActvity.this,SettingsActvity.class));
            finish();
        }
    }

    private void updateOnlyUserInfo()
    {


        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String,Object> usermap=new HashMap<>();
        usermap.put("name",Fullnameedittext.getText().toString());
        usermap.put("phone",phonenumberedittext.getText().toString());
        usermap.put("email",emailedittext.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(usermap);


        startActivity(new Intent(SettingsActvity.this,HomeActivity.class));
        Toast.makeText(SettingsActvity.this,"updated successfully",Toast.LENGTH_SHORT).show();
        finish();




    }

    private void useInfoSaved() {

        if(TextUtils.isEmpty(Fullnameedittext.getText().toString()))
        {
            Toast.makeText(SettingsActvity.this,"Name is mandatory",Toast.LENGTH_SHORT);
        }
        else  if(TextUtils.isEmpty(emailedittext.getText().toString()))
        {
            Toast.makeText(SettingsActvity.this,"email is mandatory",Toast.LENGTH_SHORT);
        }
        else  if(TextUtils.isEmpty(phonenumberedittext.getText().toString()))
        {
            Toast.makeText(SettingsActvity.this,"phone number  is mandatory",Toast.LENGTH_SHORT);
        }
        else if(checker.equals("clicked"))
        {
            uploadImage();
        }
    }
    public void uploadImage()
    {
      loadingDialog.startLoadingDialog();
      if(imageuri!=null)
      {
          final  StorageReference fileref=storageprofilepictureref.child(Prevalent.currentOnlineUser.getPhone()+".jpg");
          uploadTask= fileref.putFile(imageuri);
          uploadTask.continueWithTask(new Continuation()
          {
              @Override
              public Object then(@NonNull Task task) throws Exception
              {
                  if(!task.isSuccessful())
                  {
                      throw  task.getException();
                  }
                  return fileref.getDownloadUrl();
              }
          }).addOnCompleteListener(new OnCompleteListener<Uri>() {
              @Override
              public void onComplete(@NonNull Task<Uri> task) {
                  if(task.isSuccessful())
                  {
                      Uri downloaduri= task.getResult();
                      myurl=downloaduri.toString();
                      DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users");
                      HashMap<String,Object> usermap=new HashMap<>();
                      usermap.put("name",Fullnameedittext.getText().toString());
                      usermap.put("phone",phonenumberedittext.getText().toString());
                      usermap.put("email",emailedittext.getText().toString());
                      usermap.put("image",myurl);

                      ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(usermap);
                      loadingDialog.dismissDialog();
                      startActivity(new Intent(SettingsActvity.this,HomeActivity.class));
                      Toast.makeText(SettingsActvity.this,"updated successfully",Toast.LENGTH_SHORT).show();
                      finish();

                  }
                  else
                  {
                      loadingDialog.dismissDialog();
                      Toast.makeText(SettingsActvity.this,"error ",Toast.LENGTH_SHORT).show();
                  }
              }
          });
      }
      else
      {
          Toast.makeText(SettingsActvity.this,"Image is not selected",Toast.LENGTH_SHORT);
      }


    }

    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullnameedittext, final EditText phonenumberedittext, final EditText emailedittext)
    {
        DatabaseReference UserRef= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.child("image").exists())
                    {
                        String image =dataSnapshot.child("image").getValue().toString();
                        String name =dataSnapshot.child("name").getValue().toString();
                        String phn =dataSnapshot.child("phone").getValue().toString();
                        String email =dataSnapshot.child("email").getValue().toString();


                        Picasso.get().load(image).into(profileImageView);
                        fullnameedittext.setText(name);
                       phonenumberedittext.setText(phn);
                       emailedittext.setText(email);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
