package com.ck.zusha;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.ck.zusha.models.Post;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class NewPost extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 100 ;
    private static final String TAG = NewPost.class.getSimpleName();
    private EditText editTextDescription;
    private Button btnUpload;
    private Button btnChooseImage;
    private ImageView imageView;

    private Uri imageUri;
   private StorageReference storageReference;
   private DatabaseReference databaseReference;

   ImageButton back;
    FirebaseUser mUser;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        editTextDescription = (EditText) findViewById(R.id.editText_description);
        btnUpload = (Button) findViewById(R.id.btn_Upload);
        imageView = (ImageView) findViewById(R.id.imageView);
        btnChooseImage = (Button) findViewById(R.id.btn_chooseImage);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference=FirebaseDatabase.getInstance().getReference("uploads");
        back=(ImageButton)findViewById(R.id.backArrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(NewPost.this, HomeActivity.class);
                startActivity(i);

            }
        });

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    uploadfile();
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            try {
                imageUri = data.getData();
                final InputStream imageStream;
                imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }


    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_PICK_IMAGE);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void uploadfile(){

        if(imageUri!=null){
            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("uploading");
            progressDialog.show();
            final StorageReference filereference=storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));
            filereference.putFile(imageUri)
                    .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful()){
                                throw task.getException();
                            }
                            return filereference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(NewPost.this, "file uploaded successfully", Toast.LENGTH_SHORT).show();
                        Uri downloadUri=task.getResult();
                        //Log.e(TAG,"then"+downloadUri.toString());
                        String postId= databaseReference.push().getKey();
                        post=new Post(postId,downloadUri.toString(),editTextDescription.getText().toString());
                        databaseReference.child(postId).setValue(post);
                        finish();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(NewPost.this, "upload failed: "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }






}