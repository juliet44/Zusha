package com.ck.zusha.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.ck.zusha.CommentActivity;
import com.ck.zusha.GlideApp;
import com.ck.zusha.R;
import com.ck.zusha.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class postAdapter extends RecyclerView.Adapter<postAdapter.postviewHolder> {
    Boolean likeChecker=false;
    private DatabaseReference postref,likesref;
    String currentUserId;


    private Context mContext;
    private List<Post> postList;

    public postAdapter(Context context, List<Post>uploads) {
        mContext= context;
        postList = uploads;
    }
    @NonNull
    @Override
    public postviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.post_item,parent,false);
        return new postviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final postviewHolder holder, int position) {
             Post post=postList.get(position);
            postref= FirebaseDatabase.getInstance().getReference("uploads");
            final String postId= post.getPostId();

            holder.textViewDescription.setText(post.getDescription());

            if(post.getImagedownloadURI()!=null) {
                GlideApp.with(mContext)
                        .load(post.getImagedownloadURI())
                        .into(holder.imageDisplay);
            }

            holder.setButtonStatus(postId);

            holder.likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    likesref=FirebaseDatabase.getInstance().getReference().child("likes");
                    currentUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();
                    likeChecker=true;
                likesref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                       if(likeChecker.equals(true)){
                           if(dataSnapshot.child(postId).hasChild(currentUserId)){
                               likesref.child(postId).child(currentUserId).removeValue();
                               likeChecker=false;
                           }else {
                               likesref.child(postId).child(currentUserId).setValue(true);
                               likeChecker=false;
                             }
                       }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                }
            });


            holder.commentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(mContext, CommentActivity.class);

                    intent.putExtra("postId",postId);
                    mContext.startActivity(intent);




                }
            });

    }


    @Override
    public int getItemCount() {
        return postList.size();
    }


    public class postviewHolder extends RecyclerView.ViewHolder{

        public TextView textViewDescription;
        public ImageView imageDisplay;
        ImageButton likeButton,commentButton;
        TextView numberOfLikes;
        int countLikes;
        String currentUserId;
        DatabaseReference likesRef;

        public postviewHolder(View itemView) {
            super(itemView);

            textViewDescription=itemView.findViewById(R.id.textView_description);
            imageDisplay=itemView.findViewById(R.id.imageDisplay);
            likeButton=itemView.findViewById(R.id.likeButton);
            commentButton=itemView.findViewById(R.id.commentButton);
            numberOfLikes=itemView.findViewById(R.id.noOfLikes);
            likesref=FirebaseDatabase.getInstance().getReference().child("likes");
            currentUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        public void setButtonStatus(final String postId) {
            likesref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(postId).hasChild(currentUserId)){
                        countLikes=(int)dataSnapshot.child(postId).getChildrenCount();
                        likeButton.setImageResource(R.drawable.liked);
                        numberOfLikes.setText(Integer.toString(countLikes));
                    }else {

                        countLikes=(int)dataSnapshot.child(postId).getChildrenCount();
                        likeButton.setImageResource(R.drawable.ic_star_like);
                        numberOfLikes.setText(Integer.toString(countLikes));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
