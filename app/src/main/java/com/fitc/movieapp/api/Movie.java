package com.fitc.movieapp.api;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    public String posterPath;
    public boolean adult;
    public String overview;
    public String releaseDate;
    public List<Integer> genreIds = new ArrayList<Integer>();
    public Integer id;
    public String originalTitle;
    public String originalLanguage;
    public String title;
    public String backdropPath;
    public Double popularity;
    public Integer voteCount;
    public Boolean video;
    public Double voteAverage;
}
