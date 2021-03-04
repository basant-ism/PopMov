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

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    Context context;
    List<Movie>movies;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    public static List<Integer> movies_id=new ArrayList<>();

    public MovieAdapter(Context context, List<Movie>movies)
    {
        this.context=context;
        this.movies=movies;
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

       getMoviesIds();

    }

    private void getMoviesIds() {
        if(mAuth.getCurrentUser()!=null)
        {
            db.collection("users").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user.getMovies_id() != null)
                            movies_id = user.getMovies_id();

                    }
                }
            });
        }
        else{
            movies_id=new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_layout,parent,false);

        return new MovieViewHolder(context,view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
         holder.movieTitle.setText(movies.get(position).getTitle());
         holder.movieVote.setText(movies.get(position).getVote_average()+"/10");
         holder.movieOverview.setText(movies.get(position).getOverview());

         if(movies.get(position).getPoster_path()!=null)
         {
             Picasso.get()
                     .load("https://image.tmdb.org/t/p/w500"+movies.get(position).getPoster_path())
                     .into(holder.movieImage);

         }
         holder.setFabButton(movies.get(position).getId());
        holder.favMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser()!=null) {
                    if (movies_id.contains(movies.get(position).getId())) {
                        holder.favMovie.setImageDrawable(context.getDrawable(R.drawable.ic_bookmark_border));
                        movies_id.remove(movies.get(position).getId());
                    } else {
                        holder.favMovie.setImageDrawable(context.getDrawable(R.drawable.ic_bookmark));
                        movies_id.add(movies.get(position).getId());
                    }


                   updateData();
                }
                else{
                   showDialog(movies.get(position).getId());
                }
            }
        });
        holder.movieImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, MovieActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id",movies.get(position).getId());
                context.startActivity(intent);
            }
        });



    }

    private void showDialog(Integer idMovie) {
        AuthDialog dialog=new AuthDialog(context,idMovie);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void updateData() {
        HashMap<String ,Object>hashMap=new HashMap<>();
        hashMap.put("movies_id",movies_id);
        Log.d("TAG","update"+movies_id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.collection("users").document(mAuth.getUid()).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                     return;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        return;
                    }
                });
                return;
            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder
    {

        public ImageView movieImage,favMovie;
        public TextView movieTitle,movieVote;
        public ExpandableTextView movieOverview;
        Context context;

        public MovieViewHolder(Context context,@NonNull View itemView) {
            super(itemView);
            this.context=context;
            movieImage=itemView.findViewById(R.id.img_movie);
            favMovie=itemView.findViewById(R.id.img_fav_movie);
            movieOverview=itemView.findViewById(R.id.expand_text_view);
            movieTitle=itemView.findViewById(R.id.tv_movie_title);
            movieVote=itemView.findViewById(R.id.tv_movie_voting);

        }

        public void setFabButton(Integer idMovie) {
            if(movies_id!=null)
            {

                if(movies_id.contains(idMovie))
                {
                    favMovie.setImageDrawable(context.getDrawable(R.drawable.ic_bookmark));
                }
                else{
                    favMovie.setImageDrawable(context.getDrawable(R.drawable.ic_bookmark_border));
                }
            }
        }
    }
}
