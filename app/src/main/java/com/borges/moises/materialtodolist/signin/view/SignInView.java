package com.borges.moises.materialtodolist.signin.view;

import com.borges.moises.materialtodolist.mvp.View;

/**
 * Created by moises.anjos on 12/05/2016.
 */
public interface SignInView extends View {

    void showProgress(boolean visible);

    void showUserSignedIn();

    void showInvalidEmailAndPassword();

    void showNoInternetConnection();

    void showEmptyEmail();

    void showEmptyPassword();

    void close();
}
