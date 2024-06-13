package com.example.culture_reminder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.ComponentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.culture_reminder.models.MovieModel;
import com.example.culture_reminder.request.Servicey;
import com.example.culture_reminder.response.MovieSearchResponse;
import com.example.culture_reminder.utils.Credentials;
import com.example.culture_reminder.utils.MovieApi;
import com.example.culture_reminder.viewmodels.MovieListViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends ComponentActivity {

    private MovieListViewModel movieListViewModel;



    private Button button;
    private FirebaseAuth auth;
    private TextView textView;
    private FirebaseUser user;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        textView = findViewById(R.id.userDetails);
        user = auth.getCurrentUser();

        // Uncomment the following lines if you want to handle null user case
        // if (user == null) {
        //     Intent intent = new Intent(getApplicationContext(), Login.class);
        //     startActivity(intent);
        //     finish();
        // } else {
        //     textView.setText(user.getEmail());
        // }

        btn = findViewById(R.id.button);

        movieListViewModel =new ViewModelProvider(this).get(MovieListViewModel.class);




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetRetrofitResponseAccordingToID();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void ObserveAnyChange(){

        movieListViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {

            }
        });
    }

    private void GetRetrofitResponse() {
        MovieApi movieApi = Servicey.getMovieApi();

        Call<MovieSearchResponse> responseCall = movieApi.searchMovie(
                Credentials.API_KEY,
                "Jack Reacher",
                1
        );

        responseCall.enqueue(new Callback<MovieSearchResponse>() {
            @Override
            public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
                if (response.code() == 200) {
                    //Log.v("Tag", "the response: " + response.body().toString());
                    List<MovieModel> movies = new ArrayList<>(response.body().getMovies());

                    for (MovieModel movie : movies) {
                        Log.v("Tag", "The release date: " + movie.getRelease_date());
                    }
                } else {
                    try {
                        Log.v("Tag", "Error: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieSearchResponse> call, Throwable t) {
                Log.v("Tag", "Failed to fetch movies: " + t.getMessage());
            }
        });
    }

    private void GetRetrofitResponseAccordingToID(){
        MovieApi movieApi = Servicey.getMovieApi();
        Call<MovieModel> responseCall = movieApi.getMovie(550, Credentials.API_KEY);

        responseCall.enqueue(new Callback<MovieModel>(){
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response){
                if (response.code() == 200) {
                    MovieModel movie = response.body();
                    Log.v("Tag","The Response"+movie.getTitle());
                }
                else {
                    try{
                        Log.v("Tag","Error "+ response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(Call<MovieModel>call, Throwable t){

            }
        });
    }


}
