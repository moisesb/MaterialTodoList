package com.borges.moises.materialtodolist.mvp;

import java.io.Serializable;

/**
 * Created by moises.anjos on 06/05/2016.
 */
public interface MvpPresenter<T extends MvpView> extends Serializable{
    void bindView(T view);
    void unbindView();
}
