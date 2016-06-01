package com.borges.moises.materialtodolist.login;

import android.content.Context;

import com.borges.moises.materialtodolist.data.services.UserService;

/**
 * Created by moises.anjos on 12/05/2016.
 */
public class LoginPresenter implements LoginMvp.Presenter {

    private UserService mService;
    private LoginMvp.View mView;

    private final UserService.LoginListener mListener = new UserService.LoginListener() {
        @Override
        public void onSuccess() {
            mView.showProgress(false);
            mView.showUserSignedIn();
            mView.close();
        }

        @Override
        public void onError() {
            mView.showProgress(false);
            mView.showInvalidEmailAndPassword();
        }

        @Override
        public void onNetworkError() {
            mView.showProgress(false);
            mView.showNoInternetConnection();
        }
    };


    public LoginPresenter(Context context) {
        mService = new UserService(context);
    }

    @Override
    public void login(String email, String password) {

        checkView();

        if (email == null || email.isEmpty()) {
            mView.showEmptyEmail();
            return;
        }

        if (password == null || password.isEmpty()) {
            mView.showEmptyPassword();
            return;
        }

        mView.showProgress(true);

        mService.login(email, password, mListener);
    }

    @Override
    public void loginWithFacebook(String authToken) {
        checkView();

        mView.showProgress(true);

        mService.loginWithFacebook(authToken,mListener);
    }

    @Override
    public void openCreateAccount() {
        checkView();
        mView.openCreateAccount();
    }

    private void checkView() {
        if (mView == null) {
            throw new IllegalStateException("Should bind view first");
        }
    }

    @Override
    public void bindView(LoginMvp.View view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }
}
