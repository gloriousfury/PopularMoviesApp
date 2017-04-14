package com.example.android.popularmoviesapp.model;

import java.util.ArrayList;
import java.util.List;


public class SingleMovie {


private String poster_path;

private Boolean adult;

private String overview;

private String release_date;

private ArrayList<Integer> genre_ids = null;

private Integer id;

private String original_title;

private String original_language;

private String title;

private String backdrop_path;

private Double popularity;

private Integer voteCount;

private Boolean video;

private Double vote_average;

public String getPosterPath() {
return poster_path;
}

public void setPosterPath(String poster_path) {
this.poster_path = poster_path;
}

public Boolean getAdult() {
return adult;
}

public void setAdult(Boolean adult) {
this.adult = adult;
}

public String getOverview() {
return overview;
}

public void setOverview(String overview) {
this.overview = overview;
}

public String getReleaseDate() {
return release_date;
}

public void setReleaseDate(String release_date) {
this.release_date = release_date;
}

public ArrayList<Integer> getGenreIds() {
return genre_ids;
}

public void setGenreIds(ArrayList<Integer> genre_ids) {
this.genre_ids = genre_ids;
}

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getOriginalTitle() {
return original_title;
}

public void setOriginalTitle(String original_title) {
this.original_title = original_title;
}

public String getOriginalLanguage() {
return original_language;
}

public void setOriginalLanguage(String original_language) {
this.original_language = original_language;
}

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public String getBackdropPath() {
return backdrop_path;
}

public void setBackdropPath(String backdrop_path) {
this.backdrop_path = backdrop_path;
}

public Double getPopularity() {
return popularity;
}

public void setPopularity(Double popularity) {
this.popularity = popularity;
}

public Integer getVoteCount() {
return voteCount;
}

public void setVoteCount(Integer voteCount) {
this.voteCount = voteCount;
}

public Boolean getVideo() {
return video;
}

public void setVideo(Boolean video) {
this.video = video;
}

public Double getVoteAverage() {
return vote_average;
}

public void setVoteAverage(Double vote_average) {
this.vote_average = vote_average;
}

}