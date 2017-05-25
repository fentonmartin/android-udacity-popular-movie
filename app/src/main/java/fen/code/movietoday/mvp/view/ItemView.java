package fen.code.movietoday.mvp.view;

import fen.code.movietoday.mvp.model.ItemModel;

/**
 * Created by FEN on 24/05/2017.
 */

public interface ItemView {
    void onSuccess(ItemModel result);
    void onError(Throwable err);
}
