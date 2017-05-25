package fen.code.movietoday.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import fen.code.movietoday.Gitfen;

public class Splashscreen extends Gitfen {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(fen.code.movietoday.R.layout.splashscreen);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        splashLoad();
    }

    private void splashLoad() {
        final ProgressBar progressBar = (ProgressBar) findViewById(fen.code.movietoday.R.id.splash_load);
        CountDownTimer countDownTimer = new CountDownTimer(1600, 10) {
            public void onTick(long millis) {
                progressBar.setProgress(100 - (int) (millis / 20));
            }

            public void onFinish() {
                splashFinished();
            }
        };
        countDownTimer.start();
    }

    private void splashFinished() {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }

    public void splashSkip(View view) {
        splashFinished();
    }
}
