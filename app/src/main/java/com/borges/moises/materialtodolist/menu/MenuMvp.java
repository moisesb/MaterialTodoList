package com.borges.moises.materialtodolist.menu;

import com.borges.moises.materialtodolist.mvp.MvpPresenter;
import com.borges.moises.materialtodolist.mvp.MvpView;

/**
 * Created by moises.anjos on 24/05/2016.
 */
public interface MenuMvp {
    interface View extends MvpView{
        void showLoginMenu();
        void showLogoutMenu();
        void openLogin();
        void openCreateAccount();
    }

    interface Presenter extends MvpPresenter<View>{
        void loadMenu();
        void openLoginOrCreateAccount();
        void logout();
    }
}