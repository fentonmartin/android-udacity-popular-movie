package fen.code.movietoday;

/**
 * Created by FEN on 23/11/2016.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;

import fen.code.movietoday.utils.StringUtils;

public class Gitfen extends AppCompatActivity {

    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setElevation(0);
        }

        mPref = getSharedPreferences(StringUtils.PREF_COLUMN, MODE_PRIVATE);
        mPref = getSharedPreferences(StringUtils.PREF_MOVIES, MODE_PRIVATE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    public void setTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    public void setTitleColor(String title) {
        setTitle(title, R.color.colorAccent);
    }

    public void setPrefColumns(int data) {
        mPref.edit()
                .putInt(StringUtils.PREF_COLUMN, data)
                .apply();
    }

    public int getPrefColumns() {
        return mPref.getInt(StringUtils.PREF_COLUMN, 3);
    }

    public void setPrefMovies(int data) {
        mPref.edit()
                .putInt(StringUtils.PREF_MOVIES, data)
                .apply();
    }

    public int getPrefMovies() {
        return mPref.getInt(StringUtils.PREF_MOVIES, 1);
    }

    public void setTitle(String title, int color) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(Html.fromHtml("<font color=\"" +
                    getResources().getColor(color) + "\">" + title + "</font>"));
        }
    }

    public void setSubtitle(String subtitle) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(subtitle);
        }
    }

    public void showSnackBar(View view, String message) {
        try {
            final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
            snackbar.show();
        } catch (NullPointerException | IllegalArgumentException ignored) {
        }
    }

    public void showSnackBarButton(View view, String message) {
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setAction(getString(R.string.button_ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }
}
