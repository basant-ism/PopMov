package com.example.popmov.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.popmov.Adapters.MovieAdapter;
import com.example.popmov.Models.Movie;
import com.example.popmov.R;
import com.example.popmov.Response.MovieResponse;
import com.example.popmov.ViewModels.MovieViewModels;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "TAG";
    RecyclerView recyclerView;
    MovieViewModels viewModels;
    public  static List<Movie> movies=new ArrayList<>();
    private  static MovieAdapter adapter;
    private int page=1;
    private static Activity context;
    ProgressBar progressBar;
    LinearLayoutManager llm;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        progressBar=root.findViewById(R.id.progress_bar);

        progressBar.setVisibility(View.VISIBLE);

        recyclerView=root.findViewById(R.id.recycler_view);
        llm=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        adapter=new MovieAdapter(getActivity(),movies);
        recyclerView.setAdapter(adapter);

        viewModels=new ViewModelProvider(this).get(MovieViewModels.class);
        context=getActivity();


         recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
             @Override
             public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                 super.onScrolled(recyclerView, dx, dy);
                 int position=llm.findLastVisibleItemPosition();

                 if(position>movies.size()-3)
                 {
                     page++;

                     getMovies();
                 }
             }
         });


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        getMovies();
    }

    private void getMovies()
    {


        viewModels.getMovies(page).observe(getActivity(), new Observer<MovieResponse>() {
            @Override
            public void onChanged(MovieResponse mealResponse) {
                if(mealResponse!=null)
                {
                    if(mealResponse.getMovies()!=null)
                    {
                        movies.addAll(mealResponse.getMovies());
                        progressBar.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();

                    }
                }
            }
        });
    }

}