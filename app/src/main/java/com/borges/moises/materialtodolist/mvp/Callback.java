package com.borges.moises.materialtodolist.mvp;

/**
 * Created by moises.anjos on 23/05/2016.
 */
public interface Callback<T> {
    void onSuccess(T data);

    void onError();
}
