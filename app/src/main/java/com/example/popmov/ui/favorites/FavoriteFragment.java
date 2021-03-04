package com.example.popmov.ui.favorites;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.popmov.Adapters.MovieAdapter;
import com.example.popmov.Models.Movie;
import com.example.popmov.Models.User;
import com.example.popmov.R;
import com.example.popmov.ViewModels.MovieViewModels;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    private static final String TAG = "TAG";
    RecyclerView recyclerView;
    MovieViewModels viewModels;
    public  static List<Movie>movies=new ArrayList<>();
    private  static MovieAdapter adapter;
    private static Activity context;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_favorite, container, false);
        progressBar=root.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView=root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter=new MovieAdapter(getActivity(),movies);
        recyclerView.setAdapter(adapter);

        viewModels=new ViewModelProvider(this).get(MovieViewModels.class);
        context=getActivity();
        textView=root.findViewById(R.id.tv_exist);
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        if(mAuth.getCurrentUser()!=null)
        {
            textView.setVisibility(View.GONE);
            db.collection("users").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists())
                    {
                        User user=documentSnapshot.toObject(User.class);
                        if(user!=null&&user.getMovies_id()!=null&&user.getMovies_id().size()>0)
                        {
                            getMovies(user.getMovies_id());
                        }
                        else
                        {
                            movies.clear();
                            textView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    else {
                        movies.clear();
                        textView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    movies.clear();
                    textView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
            });
        }
        else {
            movies.clear();
            textView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void getMovies(List<Integer>ids)
    {

        movies.clear();


            viewModels.getFavoriteMovies(ids).observe(getActivity(), new Observer<Movie>() {
                @Override
                public void onChanged(Movie movie) {
                    if(movie!=null)
                    {


                            movies.add(movie);
                            progressBar.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();



                    }

                }
            });
        }


}