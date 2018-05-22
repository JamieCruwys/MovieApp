package com.fitc.movieapp.api;

import com.fitc.movieapp.model.MovieResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApi {
    @GET("movie/top_rated")
    Observable<Response<MovieResponse>> getTopRated(@Query("api_key") String apiKey);
}