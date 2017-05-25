package fen.code.movietoday.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ObjectTrailer {

    @SerializedName("results")
    public List<DataTrailer> results;

    public class DataTrailer {
        @SerializedName("name")
        public String name;

        @SerializedName("key")
        public String key;

        @SerializedName("type")
        public String type;
    }
}
