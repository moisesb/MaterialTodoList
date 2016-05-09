package com.borges.moises.materialtodolist.addtodoitem.view;

import com.borges.moises.materialtodolist.mvp.View;

/**
 * Created by moises.anjos on 06/05/2016.
 */
public interface AddTodoItemView extends View{
    void showMissingTitle();
    void showTodoItemAdded();
    void close();
}
