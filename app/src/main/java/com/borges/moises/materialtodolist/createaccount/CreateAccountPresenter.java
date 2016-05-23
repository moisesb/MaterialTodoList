package com.borges.moises.materialtodolist.createaccount;

import android.content.Context;

import com.borges.moises.materialtodolist.data.services.UserService;

/**
 * Created by moises.anjos on 11/05/2016.
 */
public class CreateAccountPresenter implements CreateAccountMvp.Presenter {

    private CreateAccountMvp.View mView;
    private UserService mUserService;

    public CreateAccountPresenter(Context context){
        mUserService = new UserService(context);
    }

    @Override
    public void createAccount(String email, String password, String userName) {

        checkView();

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

    private void checkView() {
        if (mView == null) {
            throw new IllegalStateException("Should bind view first");
        }
    }

    @Override
    public void openLogin() {
        checkView();
        mView.openLogin();
    }

    @Override
    public void bindView(CreateAccountMvp.View view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }
}
