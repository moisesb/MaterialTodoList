package com.borges.moises.materialtodolist.signin.presenter;

import com.borges.moises.materialtodolist.mvp.MvpPresenter;
import com.borges.moises.materialtodolist.signin.view.SignInMvpView;

/**
 * Created by moises.anjos on 12/05/2016.
 */
public interface SignInMvpPresenter extends MvpPresenter<SignInMvpView> {
    void signIn(String email, String password);

    void logOut();
}
