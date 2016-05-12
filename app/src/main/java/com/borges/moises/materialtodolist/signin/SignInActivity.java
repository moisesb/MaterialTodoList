package com.borges.moises.materialtodolist.signin;

import android.app.ProgressDialog;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.signin.presenter.SignInPresenter;
import com.borges.moises.materialtodolist.signin.presenter.SignInPresenterImpl;
import com.borges.moises.materialtodolist.signin.view.SignInView;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity implements SignInView{

    @BindString(R.string.log_in)
    String mLogInStr;

    @BindString(R.string.empty_email)
    String mEmptyEmailStr;

    @BindString(R.string.empty_password)
    String mEmptyPasswordStr;

    @Bind(R.id.email_edit_text)
    EditText mEmailEditText;

    @Bind(R.id.password_edit_text)
    EditText mPasswordEditText;

    @Bind(R.id.linear_layout)
    LinearLayout mLinearLayout;

    @Bind(R.id.sign_in_button)
    Button mSignInButton;

    private ProgressDialog mProgressDialog;

    private SignInPresenter mPresenter;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        initProgressDialog();

        mPresenter = new SignInPresenterImpl(this);
        mPresenter.bindView(this);
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(mLogInStr);
    }

    @OnClick(R.id.sign_in_button) void onSignInClick() {
        signIn();
    }

    private void signIn() {
        final String email = mEmailEditText.getText().toString();
        final String password = mPasswordEditText.getText().toString();
        mPresenter.signIn(email, password);
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
        Toast.makeText(this,R.string.signed_in, Toast.LENGTH_SHORT)
            .show();
    }

    @Override
    public void showInvalidEmailAndPassword() {
        Log.d("SignIn", "invalid email and password");
    }

    @Override
    public void showNoInternetConnection() {
        Log.d("SignIn", "no internet connection");
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void showEmptyEmail() {
        mEmailEditText.setError(mEmptyEmailStr);
    }

    @Override
    public void showEmptyPassword() {
        mPasswordEditText.setError(mEmptyPasswordStr);
    }
}
