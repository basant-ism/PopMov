package com.example.popmov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.popmov.Adapters.MovieAdapter;
import com.example.popmov.Adapters.ReviewAdapter;
import com.example.popmov.Adapters.TrailerAdapter;
import com.example.popmov.Dialogs.AuthDialog;
import com.example.popmov.Models.Movie;
import com.example.popmov.Models.Review;
import com.example.popmov.Models.Trailer;
import com.example.popmov.Models.User;
import com.example.popmov.Response.ReviewResponse;
import com.example.popmov.Response.TrailerResponse;
import com.example.popmov.ViewModels.MovieViewModels;
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

public class MovieActivity extends AppCompatActivity {
    private Movie movie;
    private static final String TAG = "TAG";
    RecyclerView trailer_rv,review_rv;
    MovieViewModels viewModels;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    List<Trailer>trailers=new ArrayList<>();
    List<Review>reviews=new ArrayList<>();

    TrailerAdapter tAdapter;
    ReviewAdapter rAdapter;

    public ImageView movieImage,favMovie;
    public TextView movieTitle,movieVote;
    public ExpandableTextView movieOverview;
    private TextView tvBudget,tvRevenue,tvAdult;
    public static List<Integer> movies_id=new ArrayList<>();

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);


         toolbar  = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar=findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        review_rv=findViewById(R.id.review_rv);
        review_rv.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));

        trailer_rv=findViewById(R.id.trailer_rv);
        trailer_rv.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));

        tAdapter=new TrailerAdapter(this,trailers);
        trailer_rv.setAdapter(tAdapter);

        rAdapter=new ReviewAdapter(this,reviews);
        review_rv.setAdapter(rAdapter);

        viewModels=new ViewModelProvider(this).get(MovieViewModels.class);

        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        inits();
        setListeners();
        getMoviesIds();

        if(getIntent().hasExtra("id"))
        {
            List<Integer>list=new ArrayList<>();
            list.add(getIntent().getIntExtra("id",0));
            getMovies(list);
            getTrailers(getIntent().getIntExtra("id",0));
            getReviews(getIntent().getIntExtra("id",0),1);
        }
    }

    private void getReviews(int id,int rpage) {
        viewModels.getReviews(id,rpage).observe(this, new Observer<ReviewResponse>() {
            @Override
            public void onChanged(ReviewResponse reviewResponse) {
                if(reviewResponse!=null)
                {
                    if(reviewResponse.getReviews()!=null)
                    {
                        reviews.addAll(reviewResponse.getReviews());
                        rAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);


                    }

                }
            }
        });
    }

    private void getTrailers(int id) {

        viewModels.getTrailers(id).observe(this, new Observer<TrailerResponse>() {
            @Override
            public void onChanged(TrailerResponse trailerResponse) {
                if(trailerResponse!=null)
                {
                    if(trailerResponse.getTrailers()!=null)
                    {
                        trailers.addAll(trailerResponse.getTrailers());
                        tAdapter.notifyDataSetChanged();

                        progressBar.setVisibility(View.GONE);
                    }

                }

            }
        });
    }

    private void setListeners() {
        favMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser()!=null) {
                    if (movies_id.contains(movie.getId())) {
                        favMovie.setImageDrawable(getDrawable(R.drawable.ic_bookmark_border));
                        movies_id.remove(movie.getId());
                    } else {
                        favMovie.setImageDrawable(getDrawable(R.drawable.ic_bookmark));
                        movies_id.add(movie.getId());
                    }


                    updateData();
                }
                else{
                    showDialog(movie.getId());
                }
            }
        });
    }

    private void inits() {
        movieImage=findViewById(R.id.img_movie);
        favMovie=findViewById(R.id.img_fav_movie);
        movieOverview= findViewById(R.id.expand_text_view);
        movieTitle= findViewById(R.id.tv_movie_title);
        movieVote= findViewById(R.id.tv_movie_voting);
        tvAdult=findViewById(R.id.tv_adult);
        tvBudget=findViewById(R.id.tv_budget);
        tvRevenue=findViewById(R.id.tv_revenue);
    }

    private void getMovies(List<Integer> ids)
    {
        viewModels.getFavoriteMovies(ids).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie mov) {
                if(mov!=null)
                {
                    movie=mov;
                    setMovieData(mov);

                }
            }
        });
    }

    private void setMovieData(Movie movie) {
        if(movie.getTagline()!=null)
          movieTitle.setText(movie.getTagline());
        else
            movieTitle.setText(movie.getTitle());
        movieVote.setText(movie.getVote_average()+"/10");
        movieOverview.setText(movie.getOverview());

        if(movie.getPoster_path()!=null)
        {
            Picasso.get()
                    .load("https://image.tmdb.org/t/p/w500"+movie.getPoster_path())
                    .into(movieImage);

        }
        setFabButton(movie.getId());
        if(movie.isAdult())
            tvAdult.setText("18+");
        else
            tvAdult.setText("12+");
        tvRevenue.setText("Revenue:$"+movie.getRevenue());
        tvBudget.setText("Budget:$"+movie.getBudget());
        toolbar.setTitle(movie.getTitle());
    }
    public void setFabButton(Integer idMovie) {
        if(movies_id!=null)
        {

            if(movies_id.contains(idMovie))
            {
                favMovie.setImageDrawable(getDrawable(R.drawable.ic_bookmark));
            }
            else{
                favMovie.setImageDrawable(getDrawable(R.drawable.ic_bookmark_border));
            }
        }
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
    private void showDialog(Integer idMovie) {
        AuthDialog dialog=new AuthDialog(this,idMovie);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void updateData() {
        HashMap<String ,Object> hashMap=new HashMap<>();
        hashMap.put("movies_id",movies_id);
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

}