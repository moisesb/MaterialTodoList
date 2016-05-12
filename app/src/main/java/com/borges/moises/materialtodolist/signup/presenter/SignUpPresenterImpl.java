package com.borges.moises.materialtodolist.signup.presenter;

import android.content.Context;

import com.borges.moises.materialtodolist.data.services.UserService;
import com.borges.moises.materialtodolist.signup.view.LoginView;

/**
 * Created by moises.anjos on 11/05/2016.
 */
public class SignUpPresenterImpl implements SignUpPresenter {

    private LoginView mView;
    private UserService mUserService;

    public SignUpPresenterImpl(Context context){
        mUserService = new UserService(context);
    }

    @Override
    public void signUp(String email, String password, String userName) {

        if (mView == null) {
            throw new IllegalStateException("Should bind view first");
        }

        if (!mUserService.isUserNameValid(userName)) {
            mView.showInvalidUserName();
            return;
        }

        if (!mUserService.isEmailValid(email)){
            mView.showInvalidEmail();
            return;
        }

        if (!mUserService.isPasswordValid(password)) {
            mView.showInvalidPassword();
            return;
        }

        mView.showProgress(true);

        mUserService.createUser(email, password, userName, new UserService.SignUpListener() {
            @Override
            public void onSuccess() {
                mView.showProgress(false);
                mView.showAccountCreated();
                mView.close();
            }

            @Override
            public void onEmailTaken() {
                mView.showProgress(false);
                mView.showError();
            }

            @Override
            public void onEmailInvalid() {
                mView.showProgress(false);
                mView.showInvalidEmail();
            }

            @Override
            public void onNetworkError() {
                mView.showProgress(false);
                mView.showNoInternet();
            }
        });
    }

    @Override
    public void bindView(LoginView view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }
}
