package fen.code.movietoday.mvp.presenter;

import fen.code.movietoday.mvp.model.ItemModel;
import fen.code.movietoday.mvp.model.ItemModelRequest;
import fen.code.movietoday.mvp.view.ItemView;
import fen.code.movietoday.utils.OkhttpUtils;
import com.google.gson.Gson;

import okhttp3.Request;
import rx.Observable;

/**
 * Created by FEN on 24/05/2017.
 */

public class ItemPresenter implements ItemPresenterRequest{

    ItemModelRequest model;
    ItemView view;

    public ItemPresenter(ItemView view) {
        model = new ItemModel();
        this.view = view;
    }

    @Override
    public Observable<ItemModel> getResult() {
        Request request = model.build();
        return OkhttpUtils.streamStrings(OkhttpUtils.client, request).map(s ->
                new Gson().fromJson(s, ItemModel.class)
        );
    }
}
