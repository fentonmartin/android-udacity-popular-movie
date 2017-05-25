package fen.code.movietoday.listener;

/**
 * Created by FEN on 04/08/2016 on KUDO.
 */

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class RecyclerEndlessScrollListener extends RecyclerView.OnScrollListener {

    // The total number of items in the data set after the last load
    private int previousTotal = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;
    // The minimum amount of items to have below your current scroll position before loading more.
    private int visibleThreshold = 1;

    private int currentPage = 1;
    private LinearLayoutManager mLinearLayoutManager;
    private CustomScrollListener mCustomScrollListener;

    public RecyclerEndlessScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    public RecyclerEndlessScrollListener(LinearLayoutManager linearLayoutManager,
                                         CustomScrollListener listener) {
        this.mLinearLayoutManager = linearLayoutManager;
        this.mCustomScrollListener = listener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = mLinearLayoutManager.getItemCount();
        int firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)) {

            // End has been reached
            currentPage++;

            onLoadMore(currentPage);

            loading = true;
        }
        if (mCustomScrollListener != null) {
            mCustomScrollListener.onScrollListener(dx, dy);
        }
    }

    public abstract void onLoadMore(int current_page);

    public interface CustomScrollListener {
        void onScrollListener(int dx, int dy);
    }

    public void reset(int currentPageStart) {
        this.currentPage = currentPageStart;
        this.loading = true;
        this.previousTotal = 0;
    }

    public void setCurrentPage(int current_page) {
        this.currentPage = current_page;
    }
}