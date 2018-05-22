package com.fitc.movieapp.repo;

import com.fitc.movieapp.BuildConfig;
import com.fitc.movieapp.api.Client;
import com.fitc.movieapp.api.MovieApi;
import com.fitc.movieapp.model.Movie;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class MovieRepo {
	/**
	 * Expiry time of 5 minutes
	 */
	private static final int EXPIRY_TIME = 300000;
	public static final MovieRepo INSTANCE = new MovieRepo();

	private List<Movie> movies = new ArrayList<>();
	private long lastRequestTime = 0;

	public Observable<List<Movie>> getTopRatedMovies() {
		MovieApi movieApi = Client.getClient().create(MovieApi.class);
		return callTopRatedApi(movieApi);
	}

	private boolean isExpired() {
		return System.currentTimeMillis() > (lastRequestTime + EXPIRY_TIME);
	}

	private Observable<List<Movie>> callTopRatedApi(MovieApi movieApi) {
		return Observable.just(movies)
				.flatMap((Function<List<Movie>, ObservableSource<List<Movie>>>) movies -> {
					if (isExpired()) {
						return movieApi.getTopRated(BuildConfig.MOVIES_API_KEY)
								.map(movieResponseResponse -> {
									List<Movie> results = movieResponseResponse.body().results;
									movies.clear();
									movies.addAll(results);
									lastRequestTime = System.currentTimeMillis();
									return movies;
								});
					}
					return Observable.just(movies);
				});
	}
}