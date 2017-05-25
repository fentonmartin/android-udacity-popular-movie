package fen.code.movietoday.adapter.holder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fen.code.movietoday.R;
import fen.code.movietoday.activity.MovieActivity;
import fen.code.movietoday.utils.StringUtils;

public class MenuHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView poster;
    public TextView title;

    public String posterPath;
    public boolean isAdult;
    public String overview;
    public String release;
    public String id;
    public String titleOriginal;
    public String language;
    public String backdropPath;
    public int voteCount;
    public String voteAverage;

    public MenuHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        poster = (ImageView) itemView.findViewById(R.id.list_avatar);
        title = (TextView) itemView.findViewById(R.id.list_title);

        poster.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(view.getContext(), MovieActivity.class);
        i.putExtra(StringUtils.EXTRA_TITLE, title.getText().toString());
        i.putExtra(StringUtils.EXTRA_TITLE_ORIGINAL, titleOriginal);
        i.putExtra(StringUtils.EXTRA_OVERVIEW, overview);
        i.putExtra(StringUtils.EXTRA_RELEASE, release);
        i.putExtra(StringUtils.EXTRA_BACKDROP, backdropPath);
        i.putExtra(StringUtils.EXTRA_ADULT, isAdult);
        i.putExtra(StringUtils.EXTRA_LANGUAGE, language);
        i.putExtra(StringUtils.EXTRA_POSTER, posterPath);
        i.putExtra(StringUtils.EXTRA_VOTE, voteAverage);
        i.putExtra(StringUtils.EXTRA_VOTE_COUNT, voteCount);
        i.putExtra(StringUtils.EXTRA_ID, id);

        view.getContext().startActivity(i);
    }

}