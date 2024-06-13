package com.example.culture_reminder.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.culture_reminder.models.MovieModel;
import com.example.culture_reminder.repositories.MovieRepository;

import java.util.List;

public class MovieListViewModel extends ViewModel {



    private MovieRepository movieRepository;



    public MovieListViewModel() {
        movieRepository = MovieRepository.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return movieRepository.getMovies();
    }

    public void searchMovieApi(String query, int pageNumber){

    }

}
