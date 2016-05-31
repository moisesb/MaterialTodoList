package com.borges.moises.materialtodolist.menu;

import android.support.annotation.NonNull;

import com.borges.moises.materialtodolist.data.model.User;
import com.borges.moises.materialtodolist.data.services.UserService;

/**
 * Created by moises.anjos on 24/05/2016.
 */
public class MenuPresenter implements MenuMvp.Presenter {

    private MenuMvp.View mView;
    private UserService mService;

    public MenuPresenter(@NonNull UserService service){
        mService = service;
    }

    @Override
    public void loadMenu() {
        checkView();

        final User user = mService.getSignedInUser();
        if (user == null){
            mView.showUserName("");
            mView.showUserPicture(null);
            mView.showLoginMenu();
        }else{
            mView.showUserName(user.getUserName());
            mView.showUserPicture(user.getImageUrl());
            mView.showLogoutMenu();
        }
    }

    private void checkView() {
        if (mView == null){
            throw new IllegalStateException("Should bind view first");
        }
    }

    @Override
    public void openLoginOrCreateAccount() {
        checkView();

        if (mService.hasCreatedAccount()){
            mView.openLogin();
        }else {
            mView.openCreateAccount();
        }
    }

    @Override
    public void logout() {
        checkView();
        mService.logout();
        mView.showLoginMenu();
    }

    @Override
    public void bindView(@NonNull MenuMvp.View view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }
}
