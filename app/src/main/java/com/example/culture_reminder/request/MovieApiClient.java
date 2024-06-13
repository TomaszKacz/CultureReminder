package com.example.culture_reminder.request;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.culture_reminder.AppExecutors;
import com.example.culture_reminder.models.MovieModel;
import com.example.culture_reminder.response.MovieSearchResponse;
import com.example.culture_reminder.utils.Credentials;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {
    private MutableLiveData<List<MovieModel>> mMovies;

    private static MovieApiClient instance;



    private RetriveMoviesRunnable retriveMoviesRunnable;







    public static MovieApiClient getInstance() {
        if (instance == null) {
            instance = new MovieApiClient();
        }
        return instance;
    }

    private MovieApiClient() {
        mMovies = new MutableLiveData<>();
    }


    public MutableLiveData<List<MovieModel>> getMovies() {
        return mMovies;
    }


    public void searchMoviesApi(String query,int pageNumber) {
        if(retriveMoviesRunnable != null){
            retriveMoviesRunnable = null;
        }
        retriveMoviesRunnable = new RetriveMoviesRunnable(query,pageNumber);
        final Future myHandler = AppExecutors.getInstance().networkIO().submit(retriveMoviesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                myHandler.cancel(true);

            }
        }, 3000, TimeUnit.MILLISECONDS);

    }

    private class RetriveMoviesRunnable implements Runnable {

        private String query;
        private int pageNumber;
        boolean cancelRequest;


        public RetriveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {

            try{
                Response response = getMovies(query,pageNumber).execute();
                if(cancelRequest){
                    return;
                }
                if(response.code()==200){
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());

                if (pageNumber == 1){
                    mMovies.postValue(list);
                }else
                {
                    List<MovieModel> currentMovies = mMovies.getValue();
                    currentMovies.addAll(list);
                }

                }else {
                    String error = response.errorBody().string();
                    Log.v("Tag","Error "+error);
                    mMovies.postValue(null);
                }



            }catch (IOException e){
                e.printStackTrace();
                mMovies.postValue(null);
            }



            if (cancelRequest) {
                return;
            }
        }
        private Call<MovieSearchResponse> getMovies(String query, int pageNumber) {
            return Servicey.getMovieApi().searchMovie(
                    Credentials.API_KEY,
                    query,
                    pageNumber
            );
        }
        private void cancelRequest(){
            Log.v("Tag", "Canceling Search Request");
            cancelRequest = true;
        }
    }

}
