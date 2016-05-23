package com.borges.moises.materialtodolist.createaccount;

import com.borges.moises.materialtodolist.mvp.MvpPresenter;
import com.borges.moises.materialtodolist.mvp.MvpView;

/**
 * Created by moises.anjos on 23/05/2016.
 */
public interface CreateAccountMvp {

    interface View extends MvpView {
        void showAccountCreated();

        void showInvalidEmail();

        void showInvalidPassword();

        void showInvalidUserName();

        void showError();

        void showProgress(boolean visible);

        void close();

        void showNoInternet();

        void openLogin();
    }

    interface Presenter extends MvpPresenter<View> {

        void createAccount(String email, String password, String userName);

        void openLogin();
    }
}
