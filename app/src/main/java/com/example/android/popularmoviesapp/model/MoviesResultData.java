package com.example.android.popularmoviesapp.model;

import java.util.List;

public class MoviesResultData {


private Integer page;

private List<SingleMovie> results = null;

private Integer totalResults;

private Integer totalPages;

public Integer getPage() {
return page;
}

public void setPage(Integer page) {
this.page = page;
}

public List<SingleMovie> getResults() {
return results;
}

public void setResults(List<SingleMovie> results) {
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