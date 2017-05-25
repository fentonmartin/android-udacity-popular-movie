package fen.code.movietoday.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import fen.code.movietoday.Gitfen;
import fen.code.movietoday.adapter.MenuAdapters;
import fen.code.movietoday.entity.ObjectItem;
import fen.code.movietoday.mvp.model.ItemModel;
import fen.code.movietoday.mvp.presenter.ItemPresenter;
import fen.code.movietoday.mvp.presenter.ItemPresenterRequest;
import fen.code.movietoday.mvp.view.ItemView;
import fen.code.movietoday.utils.NetworkUtils;
import fen.code.movietoday.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MenuActivity extends Gitfen
        implements NavigationView.OnNavigationItemSelectedListener, ItemView {

    RecyclerView recyclerView;
    LinearLayout pagesLayout;
    MenuAdapters menuAdapters;
    ObjectItem itemObject;
    ProgressBar progressBar;
    String filmCategory;
    SearchView mSearchView;
    SwipeRefreshLayout refreshLayout;
    TextView menuEmpty, menuPage, menuPageNext, menuPagePrev;

    private int mPageIndex = 1;
    private int mPageMax = 40;
    private int mItemMax;
    private boolean mIsSearch = false;
    private String mSearchQuery = "";
    private ItemPresenterRequest mainPresenter;

    protected Subscription subscriber = new CompositeSubscription();

    List<ObjectItem.Results> mListMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(fen.code.movietoday.R.layout.activity_menu);

        Toolbar toolbar = (Toolbar) findViewById(fen.code.movietoday.R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(fen.code.movietoday.R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, fen.code.movietoday.R.string.navigation_drawer_open, fen.code.movietoday.R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(fen.code.movietoday.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        menuEmpty = (TextView) findViewById(fen.code.movietoday.R.id.menu_content_empty);
        menuPage = (TextView) findViewById(fen.code.movietoday.R.id.menu_content_page_text);
        menuPageNext = (TextView) findViewById(fen.code.movietoday.R.id.menu_content_page_next);
        menuPagePrev = (TextView) findViewById(fen.code.movietoday.R.id.menu_content_page_prev);

        pagesLayout = (LinearLayout) findViewById(fen.code.movietoday.R.id.menu_content_pages);
        progressBar = (ProgressBar) findViewById(fen.code.movietoday.R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(fen.code.movietoday.R.id.menu_rv_movies);
        refreshLayout = (SwipeRefreshLayout) findViewById(fen.code.movietoday.R.id.menu_content_refresh);
        refreshLayout.setColorSchemeColors(getResources().getColor(fen.code.movietoday.R.color.colorPrimary));
        refreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(fen.code.movietoday.R.color.colorAccent));

        refreshLayout.setEnabled(false);
        refreshLayout.setOnRefreshListener(() -> {
            if (!mIsSearch) {
                requestApi(getPrefMovies(), mPageIndex);
            } else {
                if (mSearchQuery != null)
                    requestSearch(mSearchQuery, mPageIndex);
            }
        });

        setRecyclerView(getPrefColumns(), true, false);
        setProgressBar(true, false, false);
        setPageNumber();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            menuPagePrev.setAlpha((float) .5);
        }
        menuPagePrev.setClickable(false);

        ViewCompat.setNestedScrollingEnabled(recyclerView, true);

        setPrefMovies(0);
        requestApi(getPrefMovies(), mPageIndex);

        Log.d("LOG MVP", "Main Presenter Initiated");
        mainPresenter = new ItemPresenter(this);
        subscriber = mainPresenter.getResult()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError);
    }

    @Override
    protected void onDestroy() {
        subscriber.unsubscribe();
        super.onDestroy();
    }

    public void menuPage(View view) {
        showSnackBarButton(view, getString(fen.code.movietoday.R.string.menu_page_text) + " " + StringUtils.FormatNumber(mPageIndex) + " " +
                getString(fen.code.movietoday.R.string.menu_page_more) + " " + StringUtils.FormatNumber(mPageMax) + " " +
                getString(fen.code.movietoday.R.string.menu_page_more_end) + "\n(" + StringUtils.FormatNumber(mItemMax) + " " +
                getString(fen.code.movietoday.R.string.menu_page_item) + ")");
    }

    public void menuPageNext(View view) {
        if (mPageIndex > 0 && mPageIndex < 1000) {
            mPageIndex++;
            setRecyclerView(getPrefColumns(), true, false);
            setProgressBar(true, false, false);
            if (mIsSearch)
                requestSearch(mSearchQuery, mPageIndex);
            else
                requestApi(getPrefMovies(), mPageIndex);
            setPageNumber();
        }
    }

    public void menuPagePrev(View view) {
        if (mPageIndex > 1 && mPageIndex <= 1000) {
            mPageIndex--;
            setRecyclerView(getPrefColumns(), true, false);
            setProgressBar(true, false, false);
            if (mIsSearch)
                requestSearch(mSearchQuery, mPageIndex);
            else
                requestApi(getPrefMovies(), mPageIndex);
            setPageNumber();
        }
    }

    private void setRecyclerView(int column, boolean isNew, boolean isColumn) {
        if (isColumn) {
            LinearLayoutManager layoutManager = new GridLayoutManager(MenuActivity.this, column);

            recyclerView.setAdapter(menuAdapters);
            recyclerView.setHasFixedSize(false);
            recyclerView.setLayoutManager(layoutManager);

        } else if (menuAdapters == null || isNew) {
            mListMovies = new ArrayList<>();
            menuAdapters = new MenuAdapters(MenuActivity.this, mListMovies);

            LinearLayoutManager layoutManager = new GridLayoutManager(MenuActivity.this, column);

            recyclerView.setAdapter(menuAdapters);
            recyclerView.setHasFixedSize(false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.clearOnScrollListeners();
        } else {
            menuAdapters.addData(mListMovies);
            menuAdapters.notifyDataSetChanged();
        }
    }

    private void setProgressBar(boolean progress, boolean search, boolean error) {
        menuEmpty.setVisibility(View.GONE);
        pagesLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.removeAllViewsInLayout();

        refreshLayout.setEnabled(true);
        if (menuAdapters != null)
            menuAdapters.clearData();
        if (!progress) {
            if (!error) {
                refreshLayout.setEnabled(false);
                pagesLayout.setVisibility(View.VISIBLE);
            }
            progressBar.setVisibility(View.GONE);
            if ((mListMovies.size() == 0 || menuAdapters == null) && error) {
                menuEmpty.setVisibility(View.VISIBLE);
                menuEmpty.setText(fen.code.movietoday.R.string.text_error_empty);
                if (search)
                    menuEmpty.setText(fen.code.movietoday.R.string.text_error_not_found);
            }
            refreshLayout.setRefreshing(false);
            if (!NetworkUtils.isNetworkConnected(getApplicationContext()))
                menuEmpty.setText(getString(fen.code.movietoday.R.string.text_error_network));
        }
        if (mIsSearch && !mSearchView.isIconified()) {
            mSearchView.setIconified(true);
        }
    }

    private void setPageNumber() {
        menuPage.setText(StringUtils.FormatNumber(mPageIndex) + " of " + StringUtils.FormatNumber(mPageMax));
        menuPageNext.setClickable(true);
        menuPagePrev.setClickable(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            menuPageNext.setAlpha(1);
            menuPagePrev.setAlpha(1);
        }

        if (mPageIndex == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                menuPagePrev.setAlpha((float) .2);
            }
            menuPagePrev.setClickable(false);
        }
        if (mPageIndex == mPageMax) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                menuPageNext.setAlpha((float) .2);
            }
            menuPageNext.setClickable(false);
        }
    }

    private void requestApi(int i, int page) {

        setSubtitle("");
        if (i == 0) {
            setTitleColor(getString(fen.code.movietoday.R.string.app_name));
//            filmCategory = "latest";
            filmCategory = "now_playing";
        } else if (i == 1) {
            setTitleColor(getString(fen.code.movietoday.R.string.button_popular));
            filmCategory = "popular";
        } else if (i == 2) {
            setTitleColor(getString(fen.code.movietoday.R.string.button_now_playing));
            filmCategory = "now_playing";
        } else if (i == 3) {
            setTitleColor(getString(fen.code.movietoday.R.string.button_top_rated));
            filmCategory = "top_rated";
        } else if (i == 4) {
            setTitleColor(getString(fen.code.movietoday.R.string.button_upcoming));
            filmCategory = "upcoming";
        }

        if (page == 1)
            if (itemObject != null)
                itemObject.results.clear();
    }

    private void requestSearch(String query, int page) {
        mSearchQuery = StringUtils.FormatStringSpace(query);
        setTitleColor(getString(fen.code.movietoday.R.string.button_search));
        setSubtitle(StringUtils.FormatString(query));
        showSnackBar(getCurrentFocus(), getString(fen.code.movietoday.R.string.text_search) + " " +
                StringUtils.FormatString(query) + "..");
        String url = StringUtils.API_SEARCH +
                filmCategory +
                "&query=" + query +
                "&api_key=" + StringUtils.API +
                "&page=" + page;
        mIsSearch = true;
        setProgressBar(true, true, false);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        mPageIndex = 1;
        mIsSearch = false;
        if (id == fen.code.movietoday.R.id.menu_nav_home) {
            setPrefMovies(0);

        } else if (id == fen.code.movietoday.R.id.menu_nav_popular) {
            setPrefMovies(1);

        } else if (id == fen.code.movietoday.R.id.menu_nav_now_playing) {
            setPrefMovies(2);

        } else if (id == fen.code.movietoday.R.id.menu_nav_top_rated) {
            setPrefMovies(3);

        } else if (id == fen.code.movietoday.R.id.menu_nav_upcoming) {
            setPrefMovies(4);

        } else if (id == fen.code.movietoday.R.id.menu_nav_about) {
            Intent intent = new Intent(MenuActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        setRecyclerView(getPrefColumns(), true, false);
        setPageNumber();

        requestApi(getPrefMovies(), mPageIndex);
        setProgressBar(true, mIsSearch, false);

        DrawerLayout drawer = (DrawerLayout) findViewById(fen.code.movietoday.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(fen.code.movietoday.R.menu.menu, menu);

        mSearchView = (SearchView) menu.findItem(fen.code.movietoday.R.id.action_search).getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mPageIndex = 1;
                mSearchQuery = StringUtils.FormatStringSpace(query);
                requestSearch(mSearchQuery, mPageIndex);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == fen.code.movietoday.R.id.menu_show_1) {
            setPrefColumns(1);
        } else if (id == fen.code.movietoday.R.id.menu_show_2) {
            setPrefColumns(2);
        } else if (id == fen.code.movietoday.R.id.menu_show_3) {
            setPrefColumns(3);
        } else if (id == fen.code.movietoday.R.id.menu_show_4) {
            setPrefColumns(4);
        }

        setRecyclerView(getPrefColumns(), true, true);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(fen.code.movietoday.R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!mSearchView.isIconified()) {
                mSearchView.setIconified(true);
                if (menuAdapters.getItemCount() == 0) {
                    mPageIndex = 1;
                    requestApi(getPrefMovies(), mPageIndex);
                    setProgressBar(true, false, false);
                }
            }
            if (mPageIndex > 1) {
                mPageIndex--;
                if (mIsSearch)
                    requestSearch(mSearchQuery, mPageIndex);
                else
                    requestApi(getPrefMovies(), mPageIndex);
            } else {
                if (mIsSearch) {
                    mIsSearch = false;
                    requestApi(getPrefMovies(), mPageIndex);
                    setProgressBar(true, false, false);
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(MenuActivity.this, fen.code.movietoday.R.style.AlertDialogTheme)
                            .setTitle(getString(fen.code.movietoday.R.string.text_exit))
                            .setMessage(getString(fen.code.movietoday.R.string.text_exit_more))
                            .setPositiveButton(getString(fen.code.movietoday.R.string.text_yes), (dialog1, which) -> finish())
                            .setNegativeButton(getString(fen.code.movietoday.R.string.text_no), null)
                            .show();
                    dialog.getButton(DialogInterface.BUTTON_NEGATIVE).
                            setTextColor(getResources().getColor(fen.code.movietoday.R.color.colorAccent));
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).
                            setTextColor(getResources().getColor(fen.code.movietoday.R.color.colorAccent));
                }
            }
        }
    }

    @Override
    public void onSuccess(ItemModel response) {
        Log.d("LOG MVP", "onSuccess");
        Log.d("LOG SUCCESS", "JSON Response " + response.toString());

        setProgressBar(false, mIsSearch, false);
        setPageNumber();

        if (mListMovies != null)
            mListMovies.addAll(itemObject.getResults());
        else mListMovies = itemObject.getResults();

        if (mListMovies.isEmpty())
            setProgressBar(false, mIsSearch, true);
    }

    @Override
    public void onError(Throwable err) {
        Log.d("LOG MVP", "onError " + err.getMessage());

        setProgressBar(false, mIsSearch, true);
        if (NetworkUtils.isNetworkConnected(getApplicationContext()))
            showSnackBar(getCurrentFocus(), getString(fen.code.movietoday.R.string.text_error_api));
    }
}
