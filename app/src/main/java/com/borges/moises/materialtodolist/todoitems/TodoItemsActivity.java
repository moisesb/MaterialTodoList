package com.borges.moises.materialtodolist.todoitems;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.createaccount.CreateAccountActivity;
import com.borges.moises.materialtodolist.data.model.Tag;
import com.borges.moises.materialtodolist.data.repository.SqliteTagsRepository;
import com.borges.moises.materialtodolist.data.services.UserService;
import com.borges.moises.materialtodolist.login.LoginActivity;
import com.borges.moises.materialtodolist.menu.MenuMvp;
import com.borges.moises.materialtodolist.menu.MenuPresenter;
import com.borges.moises.materialtodolist.settings.SettingsActivity;
import com.borges.moises.materialtodolist.tags.TagsActivity;
import com.borges.moises.materialtodolist.utils.FirebaseAnalyticsHelper;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;


import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Moisés on 11/04/2016.
 */
public class TodoItemsActivity extends AppCompatActivity implements MenuMvp.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    private FirebaseAnalytics mFirebaseAnalytics;

    private SubMenu mTagsSubmenu;
    private MenuItem mPreviousMenuItemSelected = null;

    private TextView mUserNameTextView;
    private CircleImageView mProfilePictureImageView;

    private MenuMvp.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_items);
        ButterKnife.bind(this);
        setupWindowAnimations();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mPresenter = new MenuPresenter(new UserService(this), SqliteTagsRepository.getInstance());
        mPresenter.bindView(this);
        setupToolbar();
        setupDrawer();
        showTodoItems(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment != null) {
                fragment.onActivityResult(requestCode,resultCode,data);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadMenu();
        Log.d("Menu", "loadMenu");
    }

    @Override
    protected void onDestroy() {
        mPresenter.unbindView();
        super.onDestroy();
    }

    private void setupDrawer() {
        if (mNavigationView.getHeaderCount() > 0) {
            View headerView = mNavigationView.getHeaderView(0);
            mUserNameTextView = (TextView) headerView.findViewById(R.id.user_name);
            mProfilePictureImageView = (CircleImageView) headerView.findViewById(R.id.user_profile_image);
        }

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setCheckable(true);
                item.setChecked(true);
                if (mPreviousMenuItemSelected != null) {
                    mPreviousMenuItemSelected.setChecked(false);
                }
                mPreviousMenuItemSelected = item;

                switch (item.getItemId()) {
                    case R.id.sign_in_menu:
                        mPresenter.openLoginOrCreateAccount();
                        FirebaseAnalyticsHelper.notifyClickEvent(mFirebaseAnalytics,"sign_in_menu");
                        break;
                    case R.id.sign_out_menu:
                        mPresenter.logout();
                        FirebaseAnalyticsHelper.notifyClickEvent(mFirebaseAnalytics,"sign_out_menu");
                        break;
                    /*
                    case R.id.settings_menu:
                        //mPresenter.openSettings(); // TODO: 19/06/2016 Enable after implement changing notification time
                        break;
                    */
                    case R.id.tags_menu:
                        mPresenter.openTags();
                        FirebaseAnalyticsHelper.notifyClickEvent(mFirebaseAnalytics,"tags_menu");
                        break;
                    default:
                        //onTagClick(item);
                }

                mDrawerLayout.closeDrawers();
                return true;

            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.openDrawer, R.string.closeDrawer) {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                    }
                };

        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void showTodoItems(Long tagId) {
        Fragment contentFragment = TodoItemsFragment.newInstace(tagId);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout, contentFragment)
                .commit();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
    }

    private void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide();
            slide.setDuration(1000);
            getWindow().setExitTransition(slide);
        }
    }

    @Override
    public void showLoginMenu() {
        mDrawerLayout.closeDrawers();
        mNavigationView.getMenu().clear();
        mNavigationView.inflateMenu(R.menu.menu_drawer_no_logged_user);
    }

    @Override
    public void showLogoutMenu() {
        mDrawerLayout.closeDrawers();
        mNavigationView.getMenu().clear();
        mNavigationView.inflateMenu(R.menu.menu_drawer_logged_user);
    }

    @Override
    public void openLogin() {
        LoginActivity.start(this);
    }

    @Override
    public void openCreateAccount() {
        CreateAccountActivity.start(this);
    }

    @Override
    public void showUserName(String userName) {
        mUserNameTextView.setText(userName);
    }

    @Override
    public void showUserPicture(String imageUrl) {
        Picasso.with(this)
                .load(imageUrl)
                .resize(56, 56)
                .into(mProfilePictureImageView);
    }

    @Override
    public void openSettings() {
        SettingsActivity.start(this);
    }

    @Override
    public void openTags() {
        TagsActivity.start(this);
    }

    @Override
    public void addTag(final Tag tag) {
        createTagsSubmenu();
        final MenuItem menuItem = mTagsSubmenu.add(0, Menu.FIRST, Menu.NONE, tag.getName());
        menuItem.setIcon(R.drawable.ic_label_black_24px)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        mPresenter.openFilterTodoItemsByTag(tag);
                        FirebaseAnalyticsHelper.notifyClickEvent(mFirebaseAnalytics,"open_tag_" + tag.getName());
                        return false;
                    }
                });
    }

    private void createTagsSubmenu() {
        if (mTagsSubmenu == null) {
            mTagsSubmenu = mNavigationView.getMenu().addSubMenu(R.string.tags);
            mTagsSubmenu.setGroupCheckable(0,true,true);
        }
    }

    @Override
    public void filterTodoItemsByTag(Tag tag) {
        showTodoItems(tag.getId());
    }

    @Override
    public void showTagTitle(String tagName) {
        if (tagName == null || tagName.isEmpty()) {
            mToolbar.setTitle(R.string.all_tasks);
        }else {
            mToolbar.setTitle(tagName);
        }
    }

    @Override
    public void addAllTasksTag(Tag tag) {
        final String allTasksTitle = getResources().getString(R.string.all_tasks);
        tag.setName(allTasksTitle);
        addTag(tag);
    }

    @Override
    public void clearMenuTags() {
        if (mTagsSubmenu != null) {
            mTagsSubmenu = null;
            supportInvalidateOptionsMenu();
        }

    }
}
