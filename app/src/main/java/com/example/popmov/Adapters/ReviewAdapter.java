package com.example.popmov.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popmov.Dialogs.AuthDialog;
import com.example.popmov.Models.Movie;
import com.example.popmov.Models.Review;
import com.example.popmov.Models.User;
import com.example.popmov.MovieActivity;
import com.example.popmov.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    Context context;
    List<Review>reviews;



    public ReviewAdapter(Context context, List<Review>reviews)
    {
        this.context=context;
        this.reviews=reviews;

    }



    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.review_layout,parent,false);

        return new ReviewViewHolder(context,view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {

         if(reviews.get(position).getAuthor_details().getAvatar_path()!=null)
         {
             Picasso.get()
                     .load("https://image.tmdb.org/t/p/w500"+reviews.get(position).getAuthor_details().getAvatar_path())
                     .into(holder.imgUser);
         }
         if(reviews.get(position).getAuthor_details()!=null)
         {
             holder.tvUserName.setText(reviews.get(position).getAuthor_details().getUsername());
             if(reviews.get(position).getAuthor_details().getRating()!=null)
                 holder.tvRating.setText(reviews.get(position).getAuthor_details().getRating() +"/10");

         }
        if(reviews.get(position).getCreated_at()!=null)
        {
            String date_time=reviews.get(position).getCreated_at();

            holder.tvDate.setText(date_time.substring(0,10)+","+date_time.substring(12,19));
        }
         holder.userReview.setText(reviews.get(position).getContent());




    }




    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder
    {

        public CircleImageView imgUser;
        public TextView tvUserName,tvDate,tvRating;
        public ExpandableTextView userReview;
        Context context;

        public ReviewViewHolder(Context context,@NonNull View itemView) {
            super(itemView);
            this.context=context;
            imgUser=itemView.findViewById(R.id.img_user);
            tvDate=itemView.findViewById(R.id.tv_date);
            userReview=itemView.findViewById(R.id.expand_text_view);
            tvRating=itemView.findViewById(R.id.tv_rating);
            tvUserName=itemView.findViewById(R.id.tv_user_name);

        }


    }
}
