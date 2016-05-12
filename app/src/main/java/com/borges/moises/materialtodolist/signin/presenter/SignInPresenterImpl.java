package com.borges.moises.materialtodolist.signin.presenter;

import android.content.Context;

import com.borges.moises.materialtodolist.data.services.UserService;
import com.borges.moises.materialtodolist.signin.view.SignInView;

/**
 * Created by moises.anjos on 12/05/2016.
 */
public class SignInPresenterImpl implements SignInPresenter {

    private UserService mService;
    private SignInView mView;

    public SignInPresenterImpl(Context context) {
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

        mService.signIn(email, password, new UserService.SignInListener() {
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
    public void bindView(SignInView view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }
}
