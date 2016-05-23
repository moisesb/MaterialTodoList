package com.borges.moises.materialtodolist.signin.view;

import com.borges.moises.materialtodolist.mvp.MvpView;

/**
 * Created by moises.anjos on 12/05/2016.
 */
public interface SignInMvpView extends MvpView {

    void showProgress(boolean visible);

    void showUserSignedIn();

    void showInvalidEmailAndPassword();

    void showNoInternetConnection();

    void showEmptyEmail();

    void showEmptyPassword();

    void close();
}
