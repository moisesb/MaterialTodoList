package com.borges.moises.materialtodolist.signup.presenter;

import com.borges.moises.materialtodolist.signup.view.LoginView;
import com.borges.moises.materialtodolist.mvp.Presenter;

/**
 * Created by moises.anjos on 11/05/2016.
 */
public interface SignUpPresenter extends Presenter<LoginView> {

    void signUp(String email, String password, String userName);
}
