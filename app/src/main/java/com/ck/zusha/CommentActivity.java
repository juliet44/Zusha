package com.ck.zusha;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class CommentActivity extends AppCompatActivity {

    private ImageButton postCommentButton;
    private EditText commentInputText;
    private RecyclerView recyclerViewList;
    private String postId,currentuserID;
    private DatabaseReference usersref,databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        postId=getIntent().getExtras().get("postId").toString();
        mAuth=FirebaseAuth.getInstance();
        currentuserID=mAuth.getCurrentUser().getUid();
        usersref=FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference=FirebaseDatabase.getInstance().getReference("uploads").child(postId).child("comments");

        recyclerViewList=(RecyclerView)findViewById(R.id.commentList);
        recyclerViewList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewList.setLayoutManager(linearLayoutManager);

        commentInputText=(EditText)findViewById(R.id.inputComment);
        postCommentButton=(ImageButton)findViewById(R.id.sendComment);
        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersref.child(currentuserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){
                            String name=dataSnapshot.child("name").getValue().toString();
                            validateComment(name);

                            commentInputText.setText("");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }


    private void validateComment(String name) {

        String commentText=commentInputText.getText().toString();
        if(TextUtils.isEmpty(commentText)){
            Toast.makeText(this, "please write text to comment...", Toast.LENGTH_SHORT).show();
        }else {
            Calendar calendar=Calendar.getInstance();
            SimpleDateFormat currentDate=new SimpleDateFormat("yyyy-MM-dd");
            final String saveCurrentDate=currentDate.format(calendar.getTime());
            Calendar calendar1=Calendar.getInstance();
            SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss");
            final String saveCurrentTime=currentTime.format(calendar1.getTime());


//            DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//            Date date=new Date();
//            String strDate=dateFormat.format(date).toString();

           final String randomKey=currentuserID+saveCurrentDate+saveCurrentTime;

            HashMap commentMap=new HashMap();
            commentMap.put("uid",currentuserID);
            commentMap.put("comment",commentText);
            commentMap.put("date",saveCurrentDate);
            commentMap.put("time",saveCurrentTime);
            commentMap.put("name",name);

            databaseReference.child(randomKey).updateChildren(commentMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Toast.makeText(CommentActivity.this, "you have commented successfully", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(CommentActivity.this, "Error occurred, please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
