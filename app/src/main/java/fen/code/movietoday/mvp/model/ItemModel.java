package fen.code.movietoday.mvp.model;

import fen.code.movietoday.utils.StringUtils;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import okhttp3.Request;

public class ItemModel implements ItemModelRequest {

    @Override
    public Request build() {
        return new Request.Builder()
                .get()
                .url(StringUtils.ApiMovies("top_rated", 1))
                .build();
    }

    @SerializedName("results")
    private List<Results> results;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("total_results")
    private int totalResults;

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public class Results {

        @SerializedName("poster_path")
        public String posterPath;

        @SerializedName("adult")
        public boolean isAdult;

        @SerializedName("overview")
        public String overview;

        @SerializedName("release_date")
        public String release;

        @SerializedName("id")
        public String id;

        @SerializedName("original_title")
        public String titleOriginal;

        @SerializedName("original_language")
        public String language;

        @SerializedName("title")
        public String title;

        @SerializedName("backdrop_path")
        public String backdropPath;

        @SerializedName("vote_count")
        public int voteCount;

        @SerializedName("vote_average")
        public String voteAverage;

        public String getPosterPath() {
            return posterPath;
        }

        public void setPosterPath(String posterPath) {
            this.posterPath = posterPath;
        }

        public boolean isAdult() {
            return isAdult;
        }

        public void setAdult(boolean adult) {
            isAdult = adult;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getRelease() {
            return release;
        }

        public void setRelease(String release) {
            this.release = release;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitleOriginal() {
            return titleOriginal;
        }

        public void setTitleOriginal(String titleOriginal) {
            this.titleOriginal = titleOriginal;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBackdropPath() {
            return backdropPath;
        }

        public void setBackdropPath(String backdropPath) {
            this.backdropPath = backdropPath;
        }

        public int getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(int voteCount) {
            this.voteCount = voteCount;
        }

        public String getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(String voteAverage) {
            this.voteAverage = voteAverage;
        }
    }
}