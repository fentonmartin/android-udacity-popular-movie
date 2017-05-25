package fen.code.movietoday.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fen.code.movietoday.R;
import fen.code.movietoday.entity.ObjectTrailer;
import fen.code.movietoday.adapter.holder.MovieHolders;

import java.util.List;

public class MovieAdapters extends RecyclerView.Adapter<MovieHolders> {

    private List<ObjectTrailer.DataTrailer> itemList;
    private Context context;

    public MovieAdapters(Context context, List<ObjectTrailer.DataTrailer> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public MovieHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trailer, null);
        MovieHolders rcv = new MovieHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(MovieHolders holder, int position) {
        holder.txtName.setText(itemList.get(position).name);
        holder.txtType.setText(itemList.get(position).type);
        holder.key = itemList.get(position).key;
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

}