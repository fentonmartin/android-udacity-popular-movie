package fen.code.movietoday.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import fen.code.movietoday.BuildConfig;
import fen.code.movietoday.Gitfen;

public class AboutActivity extends Gitfen {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(fen.code.movietoday.R.layout.activity_about);

        setTitleColor(getString(fen.code.movietoday.R.string.button_about));
        setAppVersion();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(fen.code.movietoday.R.menu.menu_about, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == fen.code.movietoday.R.id.menu_about_feedback) {

        } else if (id == fen.code.movietoday.R.id.menu_about_rate) {

        }
        return super.onOptionsItemSelected(item);
    }

    public void setAppVersion() {
        TextView appVersion = (TextView) findViewById(fen.code.movietoday.R.id.about_version);
        if (appVersion != null)
            appVersion.setText(BuildConfig.VERSION_NAME);
    }

    public void aboutApp(View view) {

    }
}
