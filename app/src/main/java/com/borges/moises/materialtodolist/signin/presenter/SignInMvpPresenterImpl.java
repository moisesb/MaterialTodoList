package com.borges.moises.materialtodolist.signin.presenter;

import android.content.Context;

import com.borges.moises.materialtodolist.data.services.UserService;
import com.borges.moises.materialtodolist.signin.view.SignInMvpView;

/**
 * Created by moises.anjos on 12/05/2016.
 */
public class SignInMvpPresenterImpl implements SignInMvpPresenter {

    private UserService mService;
    private SignInMvpView mView;

    public SignInMvpPresenterImpl(Context context) {
        mService = new UserService(context);
    }

    @Override
    public void signIn(String email, String password) {

        if (mView == null) {
            throw new IllegalStateException("Should bind view first");
        }

        if (email == null || email.isEmpty()) {
            mView.showEmptyEmail();
            return;
        }

        if (password == null || password.isEmpty()){
            mView.showEmptyPassword();
            return;
        }

        mView.showProgress(true);

        mService.login(email, password, new UserService.SignInListener() {
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
        });
    }

    @Override
    public void logOut() {
        mService.logout();
    }

    @Override
    public void bindView(SignInMvpView view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }
}
