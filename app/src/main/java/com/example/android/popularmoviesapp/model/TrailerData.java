package com.example.android.popularmoviesapp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerData {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("results")
@Expose
private List<TrailerItem> results = null;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public List<TrailerItem> getTrailerItems() {
return results;
}

public void setTrailerItems(List<TrailerItem> results) {
this.results = results;
}

}