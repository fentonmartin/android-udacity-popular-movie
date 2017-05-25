package fen.code.movietoday.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fen.code.movietoday.entity.ObjectItem;
import fen.code.movietoday.adapter.holder.MenuHolders;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapters extends RecyclerView.Adapter<MenuHolders> {

private List<ObjectItem.Results> itemList;
private Context context;

public MenuAdapters(Context context, List<ObjectItem.Results> itemList) {
        this.itemList = itemList;
        this.context = context;
        }

@Override
public MenuHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(fen.code.movietoday.R.layout.item_movie, null);
        return new MenuHolders(layoutView);
        }

@Override
public void onBindViewHolder(MenuHolders holder, int position) {
        Picasso.with(context).load
        ("https://image.tmdb.org/t/p/w185" + itemList.get(position).posterPath)
        .placeholder(fen.code.movietoday.R.drawable.logo_item)
        .into(holder.poster);

        holder.id = itemList.get(position).id;
        holder.language = itemList.get(position).language;
        holder.title.setText(itemList.get(position).title);
        holder.titleOriginal = itemList.get(position).titleOriginal;
        holder.overview = itemList.get(position).overview;
        holder.release = itemList.get(position).release;
        holder.backdropPath = "https://image.tmdb.org/t/p/w600" + itemList.get(position).backdropPath;
        holder.voteAverage = itemList.get(position).voteAverage;
        holder.posterPath = "https://image.tmdb.org/t/p/w185" + itemList.get(position).posterPath;
        holder.isAdult = itemList.get(position).isAdult;
        holder.voteCount = itemList.get(position).voteCount;
        }

@Override
public int getItemCount() {
        return this.itemList.size();
        }

public void clearData() {
        itemList.clear();
        notifyDataSetChanged();
        }

public void addData(List<ObjectItem.Results> data) {
        if (itemList != null)
        itemList.addAll(data);
        }

public void setFilter(List<ObjectItem.Results> countryModels) {
        itemList = new ArrayList<>();
        itemList.addAll(countryModels);
        notifyDataSetChanged();
        }
        }