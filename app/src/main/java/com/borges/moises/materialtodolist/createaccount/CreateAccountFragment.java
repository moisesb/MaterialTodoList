package com.borges.moises.materialtodolist.createaccount;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.login.LoginActivity;
import com.borges.moises.materialtodolist.utils.FirebaseAnalyticsHelper;
import com.borges.moises.materialtodolist.utils.LogHelper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by moises.anjos on 23/05/2016.
 */
public class CreateAccountFragment extends Fragment implements CreateAccountMvp.View{

    @BindString(R.string.creating_account)
    String mCreatingAccountStr;

    @BindString(R.string.invalid_email)
    String mInvalidEmailStr;

    @BindString(R.string.invalid_password)
    String mInvalidPasswordStr;

    @BindString(R.string.invalid_username)
    String mInvalidUserNameStr;

    @BindView(R.id.password_edit_text)
    EditText mPasswordEditText;

    @BindView(R.id.email_edit_text)
    EditText mEmailEditText;

    @BindView(R.id.name_edit_text)
    EditText mNameEditText;

    @BindView(R.id.email_account_linear_layout)
    LinearLayout mLinearLayout;

    @BindView(R.id.sign_up_button)
    AppCompatButton mCreateAccountButton;

    @BindView(R.id.facebook_login_button)
    LoginButton mFacebookLoginButton;

    private FirebaseAnalytics mFirebaseAnalytics;

    private CallbackManager mCallbackManager = CallbackManager.Factory.create();

    private ProgressDialog mProgressDialog;

    private CreateAccountMvp.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO: 31/05/2016 add an icon and change the background color
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);
        ButterKnife.bind(this,view);

        initFacebookButton();
        initProgressDialog();
        mPresenter = new CreateAccountPresenter(getContext());
        mPresenter.bindView(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            mCallbackManager.onActivityResult(requestCode,resultCode,data);
        }
    }

    private void initFacebookButton() {
        mFacebookLoginButton.setReadPermissions("email");
        mFacebookLoginButton.setFragment(this);
        mFacebookLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(loginResult.getAccessToken() != null){
                    final String authToken = loginResult.getAccessToken().getToken();
                    mPresenter.createAccountWithFacebook(authToken);
                    FirebaseAnalyticsHelper.notifyActionPerformed(mFirebaseAnalytics, "account_created_with_facebook");
                }else {
                    mPresenter.createAccountWithFacebook(null);
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    public void onDestroy() {
        mPresenter.unbindView();
        super.onDestroy();
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(mCreatingAccountStr);
        mProgressDialog.setCancelable(false);
    }

    @OnClick(R.id.sign_up_button) void onCreateAccountClick(){
        final String email = mEmailEditText.getText().toString();
        final String password = mPasswordEditText.getText().toString();
        final String userName = mNameEditText.getText().toString();
        mPresenter.createAccount(email, password, userName);
        FirebaseAnalyticsHelper.notifyActionPerformed(mFirebaseAnalytics,"account_created_with_email");
    }

    @OnClick(R.id.sign_up_link_text_view) void onSignInLinkClick(){
        mPresenter.openLogin();
    }

    @Override
    public void showAccountCreated() {
        Toast.makeText(getContext(),R.string.account_created, Toast.LENGTH_LONG)
            .show();
    }

    @Override
    public void showInvalidEmail() {
        mEmailEditText.setError(mInvalidEmailStr);
    }

    @Override
    public void showInvalidPassword() {
        mPasswordEditText.setError(mInvalidPasswordStr);
    }

    @Override
    public void showInvalidUserName() {
        mNameEditText.setError(mInvalidUserNameStr);
    }

    @Override
    public void showError() {
        Toast.makeText(getContext(),R.string.count_not_create_account, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void showProgress(boolean visible) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (visible) {
                enableChangeData(false);
                showProgressDialog(true);
                mLinearLayout.animate()
                        .alpha(0.2f)
                        .setDuration(300)
                        .start();
            }else {
                enableChangeData(true);
                showProgressDialog(false);
                mLinearLayout.animate()
                        .alpha(1f)
                        .setDuration(300)
                        .start();
            }

        }else {
            enableChangeData(!visible);
            showProgressDialog(visible);
        }
    }

    private void showProgressDialog(boolean visible) {
        if (visible) {
            mProgressDialog.show();
        }else {
            mProgressDialog.dismiss();
        }
    }

    private void enableChangeData(boolean enable) {
        mCreateAccountButton.setEnabled(enable);
        mNameEditText.setEnabled(enable);
        mEmailEditText.setEnabled(enable);
        mPasswordEditText.setEnabled(enable);
    }

    @Override
    public void close() {
        getActivity().finish();
    }

    @Override
    public void showNoInternet() {
        Toast.makeText(getContext(),R.string.no_internet_connection, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void openLogin() {
        LoginActivity.start(getContext());
        close();
    }

    @NonNull
    public static Fragment newInstance() {
        return new CreateAccountFragment();
    }
}
