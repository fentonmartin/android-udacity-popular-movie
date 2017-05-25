package fen.code.movietoday.mvp.presenter;

import fen.code.movietoday.mvp.model.ItemModel;

import rx.Observable;

/**
 * Created by FEN on 24/05/2017.
 */

public interface ItemPresenterRequest {
    Observable<ItemModel> getResult();
}
