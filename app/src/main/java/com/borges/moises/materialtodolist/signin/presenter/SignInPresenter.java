package com.borges.moises.materialtodolist.signin.presenter;

import com.borges.moises.materialtodolist.mvp.Presenter;
import com.borges.moises.materialtodolist.signin.view.SignInView;

/**
 * Created by moises.anjos on 12/05/2016.
 */
public interface SignInPresenter extends Presenter<SignInView>{
    void signIn(String email, String password);

    void logOut();
}
