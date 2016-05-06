package com.borges.moises.materialtodolist.mvp;

/**
 * Created by moises.anjos on 06/05/2016.
 */
public interface Presenter<T extends View> {
    void bindView(T view);
    void unbindView();
}
