package com.ck.zusha.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ck.zusha.R;
import com.ck.zusha.models.Comments;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>{

    private Context mContext;
    private List<Comments>mcommentsList;

    public CommentsAdapter(Context context,List<Comments>commentsList){
        mContext=context;
        mcommentsList=commentsList;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v= LayoutInflater.from(mContext).inflate(R.layout.all_comments_layout,parent,false);
       return new CommentsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {

        Comments comments=mcommentsList.get(position);
        holder.username.setText(comments.getName());
        holder.date.setText(comments.getDate());
        holder.comment.setText(comments.getComment());
        holder.time.setText(comments.getTime());
    }

    @Override
    public int getItemCount() {
        return mcommentsList.size();
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder{

        public TextView username,date,comment,time;

        public CommentsViewHolder(View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.commentusername);
            date=itemView.findViewById(R.id.commentdate);
            comment=itemView.findViewById(R.id.commentText);
            time=itemView.findViewById(R.id.commentTime);
        }
    }
}
