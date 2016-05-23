package com.borges.moises.materialtodolist.addtodoitem.view;

import com.borges.moises.materialtodolist.mvp.MvpView;

/**
 * Created by moises.anjos on 06/05/2016.
 */
public interface AddTodoItemMvpView extends MvpView {
    void showMissingTitle();
    void showTodoItemAdded();
    void close();
}
