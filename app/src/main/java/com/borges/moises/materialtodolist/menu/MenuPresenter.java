package com.borges.moises.materialtodolist.menu;

import android.support.annotation.NonNull;

import com.borges.moises.materialtodolist.data.model.Tag;
import com.borges.moises.materialtodolist.data.model.User;
import com.borges.moises.materialtodolist.data.repository.TagsRepository;
import com.borges.moises.materialtodolist.data.services.UserService;

import java.util.List;

/**
 * Created by moises.anjos on 24/05/2016.
 */
public class MenuPresenter implements MenuMvp.Presenter {

    private MenuMvp.View mView;
    private UserService mService;
    private TagsRepository mTagsRepository;

    public MenuPresenter(@NonNull UserService service, @NonNull TagsRepository tagsRepository){
        mService = service;
        mTagsRepository = tagsRepository;
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

        mView.showTagTitle(null);

        Tag allTasksTag = new Tag();
        allTasksTag.setId(-1);
        mView.addAllTasksTag(allTasksTag);

        List<Tag> tags = mTagsRepository.getTags();
        for (Tag tag : tags) {
            mView.addTag(tag);
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
    public void openSettings() {
        mView.openSettings();
    }

    @Override
    public void openFilterTodoItemsByTag(Tag tag) {
        checkView();
        mView.showTagTitle(tag.getName());
        mView.filterTodoItemsByTag(tag);
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
