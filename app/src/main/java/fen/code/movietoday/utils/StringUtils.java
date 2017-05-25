package fen.code.movietoday.utils;

import android.annotation.SuppressLint;

import fen.code.movietoday.BuildConfig;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.PatternSyntaxException;

/**
 * Created by FEN on 23/11/2016.
 */

public class StringUtils {

    public static String API = BuildConfig.API;
    public static String API_BASE = BuildConfig.BASE;
    public static String API_MOVIES = API_BASE + "movie/";
    public static String API_SEARCH = API_BASE + "movie?";

    public static String EXTRA_ID = "id";
    public static String EXTRA_ADULT = "adult";
    public static String EXTRA_BACKDROP = "backdrop";
    public static String EXTRA_LANGUAGE = "language";
    public static String EXTRA_OVERVIEW = "overview";
    public static String EXTRA_POSTER = "poster";
    public static String EXTRA_RELEASE = "release";
    public static String EXTRA_TITLE = "title";
    public static String EXTRA_TITLE_ORIGINAL = "title_original";
    public static String EXTRA_VOTE = "vote";
    public static String EXTRA_VOTE_COUNT = "vote_count";

    public static String PREF_COLUMN = "column";
    public static String PREF_MOVIES = "movies";

    public static String ApiMovies(String category, int page) {
        return API_MOVIES + category + "?api_key=" + API + "&page=" + page;
    }

    public static String FormatDate(String date) {
        @SuppressLint("SimpleDateFormat")
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date simpleDate;
        try {
            simpleDate = simpleDateFormat.parse(date);
            DateFormat df2 = new SimpleDateFormat("EEE, MMMM dd, yyyy", Locale.US);
            return df2.format(simpleDate);
        } catch (ParseException e) {
            return date;
        }
    }

    public static String FormatRuntime(int minutes) {
        int hours;
        String runtime = minutes + "minutes";
        if (minutes > 60) {
            hours = minutes / 60;
            minutes = minutes % 60;
            runtime = hours + " hours, " + minutes + " minutes";
        }
        return runtime;
    }

    public static String FormatMoney(int money) {
        DecimalFormat moneyFormat = new DecimalFormat("$000,000.00");
        return moneyFormat.format(money);
    }

    public static String FormatNumber(int number) {
        if (number < 1000)
            return String.valueOf(number);
        else {
            DecimalFormat moneyFormat = new DecimalFormat("0,000");
            return moneyFormat.format(number);
        }
    }

    public static String FormatString(String text)
            throws PatternSyntaxException {
        return text.replaceAll("%20", " ");
    }

    public static String FormatStringSpace(String text)
            throws PatternSyntaxException {
        return text.replaceAll("\\s+", "%20");
    }
}
