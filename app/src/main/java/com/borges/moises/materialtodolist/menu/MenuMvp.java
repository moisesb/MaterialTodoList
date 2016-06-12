package com.borges.moises.materialtodolist.menu;

import com.borges.moises.materialtodolist.data.model.Tag;
import com.borges.moises.materialtodolist.mvp.MvpPresenter;
import com.borges.moises.materialtodolist.mvp.MvpView;

/**
 * Created by moises.anjos on 24/05/2016.
 */
public interface MenuMvp {
    interface View extends MvpView {
        void showLoginMenu();

        void showLogoutMenu();

        void openLogin();

        void openCreateAccount();

        void showUserName(String userName);

        void showUserPicture(String imageUrl);

        void openSettings();

        void addTag(Tag tag);

        void filterTodoItemsByTag(Tag tag);
    }

    interface Presenter extends MvpPresenter<View> {
        void loadMenu();

        void openLoginOrCreateAccount();

        void logout();

        void openSettings();

        void openFilterTodoItemsByTag(Tag tag);
    }
}
