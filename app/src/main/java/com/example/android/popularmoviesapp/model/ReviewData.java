package com.example.android.popularmoviesapp.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewData {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("page")
@Expose
private Integer page;
@SerializedName("results")
@Expose
private List<ReviewItem> results = null;
@SerializedName("total_pages")
@Expose
private Integer totalPages;
@SerializedName("total_results")
@Expose
private Integer totalReviewItem;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public Integer getPage() {
return page;
}

public void setPage(Integer page) {
this.page = page;
}

public List<ReviewItem> getReviewItem() {
return results;
}

public void setReviewItem(List<ReviewItem> results) {
this.results = results;
}

public Integer getTotalPages() {
return totalPages;
}

public void setTotalPages(Integer totalPages) {
this.totalPages = totalPages;
}

public Integer getTotalReviewItem() {
return totalReviewItem;
}

public void setTotalReviewItem(Integer totalReviewItem) {
this.totalReviewItem = totalReviewItem;
}

}