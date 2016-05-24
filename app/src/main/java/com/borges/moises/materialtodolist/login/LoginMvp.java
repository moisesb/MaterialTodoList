package com.borges.moises.materialtodolist.login;

import com.borges.moises.materialtodolist.mvp.MvpPresenter;
import com.borges.moises.materialtodolist.mvp.MvpView;

/**
 * Created by moises.anjos on 24/05/2016.
 */
public interface LoginMvp {

    interface View extends MvpView {

        void showProgress(boolean visible);

        void showUserSignedIn();

        void showInvalidEmailAndPassword();

        void showNoInternetConnection();

        void showEmptyEmail();

        void showEmptyPassword();

        void close();

        void openCreateAccount();
    }


    interface Presenter extends MvpPresenter<View> {
        void login(String email, String password);

        void loginWithFacebook(String authToken);

        void openCreateAccount();
    }
}
