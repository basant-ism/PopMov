package com.example.popmov.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popmov.Dialogs.AuthDialog;
import com.example.popmov.Models.Movie;
import com.example.popmov.Models.Trailer;
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

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    Context context;
    List<Trailer>trailers;



    public TrailerAdapter(Context context, List<Trailer>trailers)
    {
        this.context=context;
        this.trailers=trailers;

    }



    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_layout,parent,false);

        return new TrailerViewHolder(context,view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
         holder.movieTitle.setText(trailers.get(position).getName());
         String url="<iframe width=\"100%\" height=\"100%\" src=\"https://www" +".youtube.com/embed/" +
                 trailers.get(position).getKey() +
                 "\" frameborder=\"0\" allowfullscreen></iframe>";
        holder.webView.loadData( url, "text/html" , "utf-8");






    }



    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder
    {


        public TextView movieTitle;
        public WebView webView;
        Context context;

        public TrailerViewHolder(Context context,@NonNull View itemView) {
            super(itemView);
            this.context=context;

            movieTitle=itemView.findViewById(R.id.tv_movie_title);
            webView=itemView.findViewById(R.id.webView);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient() {
            } );
        }


    }
}
