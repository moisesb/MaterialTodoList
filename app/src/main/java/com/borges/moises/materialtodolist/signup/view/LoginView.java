package com.borges.moises.materialtodolist.signup.view;

import com.borges.moises.materialtodolist.mvp.View;

/**
 * Created by moises.anjos on 11/05/2016.
 */
public interface LoginView extends View {
    void showAccountCreated();

    void showInvalidEmail();

    void showInvalidPassword();

    void showInvalidUserName();

    void showError();

    void showProgress(boolean visible);

    void close();

    void showNoInternet();
}
