package fen.code.movietoday.entity;

/**
 * Created by FEN on 05/12/2016.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ObjectDetail {

    @SerializedName("budget")
    public int budget;

    @SerializedName("homepage")
    public String homepage;

    @SerializedName("revenue")
    public int revenue;

    @SerializedName("runtime")
    public int runtime;

    @SerializedName("status")
    public String status;

    @SerializedName("genres")
    public List<Genres> genres;

    @SerializedName("production_companies")
    public List<Productions> productions;

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Genres> getGenres() {
        return genres;
    }

    public void setGenres(List<Genres> genres) {
        this.genres = genres;
    }

    public List<Productions> getProductions() {
        return productions;
    }

    public void setProductions(List<Productions> productions) {
        this.productions = productions;
    }

    public class Genres {

        @SerializedName("id")
        public int id;

        @SerializedName("name")
        public String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public class Productions {

        @SerializedName("id")
        public int id;

        @SerializedName("name")
        public String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
