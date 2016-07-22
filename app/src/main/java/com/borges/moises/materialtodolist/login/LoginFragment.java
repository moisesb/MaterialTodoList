package com.borges.moises.materialtodolist.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.createaccount.CreateAccountActivity;
import com.borges.moises.materialtodolist.utils.FirebaseAnalyticsHelper;
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
 * Created by moises.anjos on 24/05/2016.
 */
public class LoginFragment extends Fragment implements LoginMvp.View{

    @BindString(R.string.log_in)
    String mLogInStr;

    @BindString(R.string.empty_email)
    String mEmptyEmailStr;

    @BindString(R.string.empty_password)
    String mEmptyPasswordStr;

    @BindView(R.id.email_edit_text)
    EditText mEmailEditText;

    @BindView(R.id.password_edit_text)
    EditText mPasswordEditText;

    @BindView(R.id.login_linear_layout)
    LinearLayout mLinearLayout;

    @BindView(R.id.sign_in_button)
    Button mSignInButton;

    @BindView(R.id.facebook_login_button)
    LoginButton mFacebookLoginButton;

    private FirebaseAnalytics mFirebaseAnalytics;

    private ProgressDialog mProgressDialog;

    private LoginMvp.Presenter mPresenter;

    private CallbackManager mCallbackManager = CallbackManager.Factory.create();

    public LoginFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this,view);

        initProgressDialog();
        initFacebookButton();

        mPresenter = new LoginPresenter(getContext());
        mPresenter.bindView(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (FacebookSdk.isFacebookRequestCode(requestCode)){
            mCallbackManager.onActivityResult(requestCode,resultCode,data);
        }
    }

    @Override
    public void onDestroy() {
        mPresenter.unbindView();
        super.onDestroy();
    }

    private void initFacebookButton() {
        mFacebookLoginButton.setReadPermissions("email");
        mFacebookLoginButton.setFragment(this);
        mFacebookLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(loginResult.getAccessToken() != null){
                    final String authToken = loginResult.getAccessToken().getToken();
                    mPresenter.loginWithFacebook(authToken);
                    FirebaseAnalyticsHelper.notifyActionPerformed(mFirebaseAnalytics,"login_with_facebook");
                }else {
                    mPresenter.loginWithFacebook(null);
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

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(mLogInStr);
        mProgressDialog.setCancelable(false);
    }

    @OnClick(R.id.sign_in_button) void onSignInClick() {
        final String email = mEmailEditText.getText().toString();
        final String password = mPasswordEditText.getText().toString();
        mPresenter.login(email, password);
        FirebaseAnalyticsHelper.notifyActionPerformed(mFirebaseAnalytics, "login_with_email");
    }

    @OnClick(R.id.sign_up_link_text_view) void onSignUpLinkClick(){
        mPresenter.openCreateAccount();
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
            mProgressDialog.hide();
        }
    }

    private void enableChangeData(boolean enabled) {
        mEmailEditText.setEnabled(enabled);
        mPasswordEditText.setEnabled(enabled);
        mSignInButton.setEnabled(enabled);
    }

    @Override
    public void showUserSignedIn() {
        Toast.makeText(getContext(),R.string.signed_in, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void showInvalidEmailAndPassword() {
        Toast.makeText(getContext(),R.string.invalid_email_and_password, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void showNoInternetConnection() {
        Toast.makeText(getContext(),R.string.no_internet_connection, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void close() {
        getActivity().finish();
    }

    @Override
    public void openCreateAccount() {
        CreateAccountActivity.start(getContext());
        getActivity().finish();
    }

    @Override
    public void showEmptyEmail() {
        mEmailEditText.setError(mEmptyEmailStr);
    }

    @Override
    public void showEmptyPassword() {
        mPasswordEditText.setError(mEmptyPasswordStr);
    }

    public static Fragment newInstance(){
        return new LoginFragment();
    }
}
