package fen.code.movietoday.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import fen.code.movietoday.Gitfen;
import fen.code.movietoday.adapter.MenuAdapters;
import fen.code.movietoday.adapter.MovieAdapters;
import fen.code.movietoday.entity.ObjectDetail;
import fen.code.movietoday.entity.ObjectItem;
import fen.code.movietoday.entity.ObjectTrailer;
import fen.code.movietoday.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MovieActivity extends Gitfen {

    ObjectDetail objectDetail;
    MenuAdapters menuAdapters;

    ImageView imgMovie, imgPoster;
    MovieAdapters adapter;
    RecyclerView movieTrailer, movieRvSimilar;
    SwipeRefreshLayout refreshLayout;
    TextView txtId, txtOverview, txtLanguage, txtAdult, txtRelease, txtVote,
            txtTitle, txtTrailer, txtBudget, txtRevenue, txtStatus, txtHomepage,
            txtRuntime, txtGenres, txtProductions, txtSimilar;

    int mIdMovie = 0;
    boolean mIsError;
    String overview;
    List<ObjectItem.Results> mListMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(fen.code.movietoday.R.layout.activity_movie);

        imgMovie = (ImageView) findViewById(fen.code.movietoday.R.id.movie_backdrop);
        imgPoster = (ImageView) findViewById(fen.code.movietoday.R.id.movie_poster);

        txtId = (TextView) findViewById(fen.code.movietoday.R.id.movie_id);
        txtOverview = (TextView) findViewById(fen.code.movietoday.R.id.movie_overview);
        txtLanguage = (TextView) findViewById(fen.code.movietoday.R.id.movie_language);
        txtAdult = (TextView) findViewById(fen.code.movietoday.R.id.movie_adult);
        txtRelease = (TextView) findViewById(fen.code.movietoday.R.id.movie_release);
        txtVote = (TextView) findViewById(fen.code.movietoday.R.id.movie_vote);
        txtTitle = (TextView) findViewById(fen.code.movietoday.R.id.movie_title);
        txtBudget = (TextView) findViewById(fen.code.movietoday.R.id.movie_budget);
        txtRevenue = (TextView) findViewById(fen.code.movietoday.R.id.movie_revenue);
        txtStatus = (TextView) findViewById(fen.code.movietoday.R.id.movie_status);
        txtHomepage = (TextView) findViewById(fen.code.movietoday.R.id.movie_homepage);
        txtRuntime = (TextView) findViewById(fen.code.movietoday.R.id.movie_runtime);
        txtGenres = (TextView) findViewById(fen.code.movietoday.R.id.movie_genres);
        txtProductions = (TextView) findViewById(fen.code.movietoday.R.id.movie_productions);
        txtTrailer = (TextView) findViewById(fen.code.movietoday.R.id.movie_trailer);
        txtSimilar = (TextView) findViewById(fen.code.movietoday.R.id.movie_similar);

        movieRvSimilar = (RecyclerView) findViewById(fen.code.movietoday.R.id.movie_rv_similar);
        movieTrailer = (RecyclerView) findViewById(fen.code.movietoday.R.id.movie_rv_trailer);
        refreshLayout = (SwipeRefreshLayout) findViewById(fen.code.movietoday.R.id.movie_content_refresh);
        refreshLayout.setColorSchemeColors(getResources().getColor(fen.code.movietoday.R.color.colorPrimary));
        refreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(fen.code.movietoday.R.color.colorAccent));

        refreshLayout.setEnabled(false);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                if (mIsError) {
                    if (mIdMovie != 0)
                        SubRequestData(String.valueOf(mIdMovie));
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(MovieActivity.this);
        movieTrailer.setHasFixedSize(true);
        movieTrailer.setLayoutManager(layoutManager);

        loadData();
        setRecyclerView(true);
    }

    public void homepageClick(View view) {
        try {
            if (objectDetail.getHomepage() != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(objectDetail.getHomepage()));
                startActivity(intent);
            }
        } catch (ActivityNotFoundException | NullPointerException ignored) {
        }
    }

    private void loadData() {
        try {
            String id = getIntent().getStringExtra(StringUtils.EXTRA_ID);
            boolean isAdult = getIntent().getBooleanExtra(StringUtils.EXTRA_ADULT, false);
            String backdrop = getIntent().getStringExtra(StringUtils.EXTRA_BACKDROP);
            String language = getIntent().getStringExtra(StringUtils.EXTRA_LANGUAGE);
            overview = getIntent().getStringExtra(StringUtils.EXTRA_OVERVIEW);
            String poster = getIntent().getStringExtra(StringUtils.EXTRA_POSTER);
            String release = getIntent().getStringExtra(StringUtils.EXTRA_RELEASE);
            String title = getIntent().getStringExtra(StringUtils.EXTRA_TITLE);
            String titleOriginal = getIntent().getStringExtra(StringUtils.EXTRA_TITLE_ORIGINAL);
            String vote = getIntent().getStringExtra(StringUtils.EXTRA_VOTE);
            int voteCount = getIntent().getIntExtra(StringUtils.EXTRA_VOTE_COUNT, 0);

            setTitleColor(title);
            mIdMovie = Integer.parseInt(id);

            txtId.setText(getString(fen.code.movietoday.R.string.movie_id) + " " + id);

            try {
                setSubtitle(release.substring(0, 4));
                if (titleOriginal != null)
                    txtTitle.setText(titleOriginal + " (" + release.substring(0, 4) + ")");
                else
                    txtTitle.setText(title + " (" + release.substring(0, 4) + ")");
            } catch (StringIndexOutOfBoundsException e) {
                setSubtitle(release);
                if (titleOriginal != null)
                    txtTitle.setText(titleOriginal);
                else
                    txtTitle.setText(title);
            }
            if (vote != null) {
                txtVote.setVisibility(View.VISIBLE);
                if (voteCount > 0)
                    txtVote.setText(getString(fen.code.movietoday.R.string.movie_rate) + " " +
                            vote + " " + getString(fen.code.movietoday.R.string.movie_vote) + " " +
                            getString(fen.code.movietoday.R.string.movie_vote_more) + " " + voteCount + " " +
                            getString(fen.code.movietoday.R.string.movie_vote_more_end));
                else
                    txtVote.setText(getString(fen.code.movietoday.R.string.movie_rate) + " " +
                            vote + " " + getString(fen.code.movietoday.R.string.movie_vote));
            }
            if (backdrop != null)
                Picasso.with(getApplicationContext())
                        .load(backdrop)
                        .placeholder(fen.code.movietoday.R.drawable.logo_item_lans)
                        .into(imgMovie);
            if (poster != null)
                imgPoster.setVisibility(View.VISIBLE);
            Picasso.with(getApplicationContext())
                    .load(poster)
                    .placeholder(fen.code.movietoday.R.drawable.logo_item)
                    .into(imgPoster);
            if (overview != null) {
                txtOverview.setVisibility(View.VISIBLE);
                txtOverview.setText(getString(fen.code.movietoday.R.string.movie_overview) + "\n\n" + overview);
            }
            if (release != null) {
                txtRelease.setVisibility(View.VISIBLE);
                txtRelease.setText(getString(fen.code.movietoday.R.string.movie_release) + " " + StringUtils.FormatDate(release));
            }
            if (language != null) {
                txtLanguage.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (Objects.equals(language, "en"))
                        language = "English";
                }
                txtLanguage.setText(getString(fen.code.movietoday.R.string.movie_language) + " " + language);
            }
            if (isAdult) {
                txtAdult.setVisibility(View.VISIBLE);
                txtAdult.setText(getString(fen.code.movietoday.R.string.movie_adult_true));
            }
            SubRequestData(id);
        } catch (NullPointerException e) {
            showSnackBar(getCurrentFocus(), getString(fen.code.movietoday.R.string.text_error_api));
        }
    }

    private void loadDetail() {
        try {
            String genres = "";
            String productions = "";
            if (!objectDetail.getHomepage().isEmpty()) {
                txtHomepage.setVisibility(View.VISIBLE);
                txtHomepage.setText(getString(fen.code.movietoday.R.string.movie_homepage) + " " +
                        objectDetail.getHomepage());
            }
            if (!objectDetail.getStatus().isEmpty()) {
                txtStatus.setVisibility(View.VISIBLE);
                txtStatus.setText(getString(fen.code.movietoday.R.string.movie_status) + " " +
                        objectDetail.getStatus());
            }
            if (objectDetail.getBudget() > 0) {
                txtBudget.setVisibility(View.VISIBLE);
                txtBudget.setText(getString(fen.code.movietoday.R.string.movie_budget) + " " +
                        StringUtils.FormatMoney(objectDetail.getBudget()));
            }
            if (objectDetail.getRevenue() > 0) {
                txtRevenue.setVisibility(View.VISIBLE);
                txtRevenue.setText(getString(fen.code.movietoday.R.string.movie_revenue) + " " +
                        StringUtils.FormatMoney(objectDetail.getRevenue()));
            }
            if (objectDetail.getRuntime() > 0) {
                txtRuntime.setVisibility(View.VISIBLE);
                txtRuntime.setText(getString(fen.code.movietoday.R.string.movie_runtime) + " " +
                        StringUtils.FormatRuntime(objectDetail.getRuntime()));
            }
            if (!objectDetail.genres.isEmpty()) {
                for (int i = 0; i < objectDetail.genres.size(); i++) {
                    if (i == 0)
                        genres = objectDetail.genres.get(i).getName();
                    else genres = genres + ", " + objectDetail.genres.get(i).getName();
                }
                txtGenres.setVisibility(View.VISIBLE);
                txtGenres.setText(getString(fen.code.movietoday.R.string.movie_genres) + " " + genres);
            }
            if (!objectDetail.productions.isEmpty()) {
                for (int i = 0; i < objectDetail.productions.size(); i++) {
                    if (i == 0)
                        productions = objectDetail.productions.get(i).getName();
                    else
                        productions = productions + ", " + objectDetail.productions.get(i).getName();
                }
                txtProductions.setVisibility(View.VISIBLE);
                txtProductions.setText(getString(fen.code.movietoday.R.string.movie_productions) + " " + productions);
            }
        } catch (NullPointerException e) {
            showSnackBar(getCurrentFocus(), getString(fen.code.movietoday.R.string.text_error_api));
        }
    }

    private void SubRequestData(String id) {
        mIsError = false;
        refreshLayout.setRefreshing(false);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String movieDetailUrl = StringUtils.API_MOVIES + id + "?api_key=" + StringUtils.API;
        String movieTrailerUrl = StringUtils.API_MOVIES + id + "/videos?api_key=" + StringUtils.API;
        String movieSimilarUrl = StringUtils.API_MOVIES + id + "/similar?api_key=" + StringUtils.API;

        StringRequest movieDetails = new StringRequest(Request.Method.GET, movieDetailUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("LOG : ", "JSON Response " + response);

                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                objectDetail = mGson.fromJson(response, ObjectDetail.class);
                loadDetail();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("LOG : ", "JSON Response " + error);
                mIsError = true;
                refreshLayout.setEnabled(true);
                showSnackBar(getCurrentFocus(), getString(fen.code.movietoday.R.string.text_error_api));
            }
        });

        StringRequest movieTrailers = new StringRequest(Request.Method.GET, movieTrailerUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("LOG : ", "JSON Response " + response);

                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                ObjectTrailer objectTrailer = mGson.fromJson(response, ObjectTrailer.class);

                if (!objectTrailer.results.isEmpty()) {
                    txtTrailer.setVisibility(View.VISIBLE);
                    adapter = new MovieAdapters(MovieActivity.this, objectTrailer.results);
                    movieTrailer.setAdapter(adapter);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("LOG : ", "JSON Response " + error);
                mIsError = true;
                refreshLayout.setEnabled(true);
                showSnackBar(getCurrentFocus(), getString(fen.code.movietoday.R.string.text_error_api));
            }
        });

        StringRequest movieSimilar = new StringRequest(Request.Method.GET, movieSimilarUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("LOG : ", "JSON Response " + response);

                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                ObjectItem objectItem = mGson.fromJson(response, ObjectItem.class);

                if (objectItem.results.size() > 0) {
                    txtSimilar.setVisibility(View.VISIBLE);
                    movieRvSimilar.setVisibility(View.VISIBLE);
                    if (mListMovies != null)
                        mListMovies.addAll(objectItem.getResults());
                    else mListMovies = objectItem.getResults();
                    setRecyclerView(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("LOG : ", "JSON Response " + error);
                mIsError = true;
                refreshLayout.setEnabled(true);
                showSnackBar(getCurrentFocus(), getString(fen.code.movietoday.R.string.text_error_api));
            }
        });

        queue.add(movieDetails);
        queue.add(movieTrailers);
        queue.add(movieSimilar);
    }

    private void setRecyclerView(boolean isNew) {
        if (menuAdapters == null || isNew) {
            mListMovies = new ArrayList<>();
            menuAdapters = new MenuAdapters(this, mListMovies);

            movieRvSimilar.setAdapter(menuAdapters);
            movieRvSimilar.setHasFixedSize(true);
            movieRvSimilar.setLayoutManager(new StaggeredGridLayoutManager(3,
                    StaggeredGridLayoutManager.VERTICAL));
            movieRvSimilar.clearOnScrollListeners();
            movieRvSimilar.setHorizontalScrollBarEnabled(true);
        } else {
            menuAdapters.addData(mListMovies);
            menuAdapters.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case fen.code.movietoday.R.id.movie_share:
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "[Movie Today] " +
                        txtTitle.getText().toString());
                intent.putExtra(android.content.Intent.EXTRA_TEXT,
                        txtTitle.getText().toString() + "\n\n" + overview + "\n\n" +
                                txtVote.getText().toString() + "\n" +
                                txtRelease.getText().toString() + "\n" +
                                txtHomepage.getText().toString() + "\n\n" +
                                getString(fen.code.movietoday.R.string.app_share));
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(fen.code.movietoday.R.menu.menu_movie, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
