package com.example.android.popularmoviesapp.model;

import java.util.ArrayList;
import java.util.List;

public class MoviesResultData  {


private Integer page;

private ArrayList<SingleMovie> results = null;

private Integer totalResults;

private Integer totalPages;

public Integer getPage() {
return page;
}

public void setPage(Integer page) {
this.page = page;
}

public ArrayList<SingleMovie> getResults() {
return results;
}

public void setResults(ArrayList<SingleMovie> results) {
this.results = results;
}

public Integer getTotalResults() {
return totalResults;
}

public void setTotalResults(Integer totalResults) {
this.totalResults = totalResults;
}

public Integer getTotalPages() {
return totalPages;
}

public void setTotalPages(Integer totalPages) {
this.totalPages = totalPages;
}

}