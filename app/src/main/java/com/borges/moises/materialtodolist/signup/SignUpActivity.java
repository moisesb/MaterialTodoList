package com.borges.moises.materialtodolist.signup;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.signup.presenter.SignUpPresenter;
import com.borges.moises.materialtodolist.signup.presenter.SignUpPresenterImpl;
import com.borges.moises.materialtodolist.signup.view.LoginView;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity  implements LoginView{

    @BindString(R.string.creating_account)
    String mCreatingAccountStr;

    @BindString(R.string.invalid_email)
    String mInvalidEmailStr;

    @BindString(R.string.invalid_password)
    String mInvalidPasswordStr;

    @BindString(R.string.invalid_username)
    String mInvalidUserNameStr;

    @Bind(R.id.password_edit_text)
    EditText mPasswordEditText;

    @Bind(R.id.email_edit_text)
    EditText mEmailEditText;

    @Bind(R.id.name_edit_text)
    EditText mNameEditText;

    @Bind(R.id.linear_layout)
    LinearLayout mLinearLayout;

    @Bind(R.id.sign_up_button)
    Button mSignUpButton;

    private ProgressDialog mProgressDialog;

    private SignUpPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        initProgressDialog();

        mPresenter = new SignUpPresenterImpl(this);
        mPresenter.bindView(this);

    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(mCreatingAccountStr);
    }

    @OnClick(R.id.sign_up_button) void onSignUpClick(){
        final String email = mEmailEditText.getText().toString();
        final String password = mPasswordEditText.getText().toString();
        final String userName = mNameEditText.getText().toString();
        mPresenter.signUp(email, password, userName);
    }

    @Override
    protected void onDestroy() {
        mPresenter.unbindView();
        super.onDestroy();
    }

    @Override
    public void showAccountCreated() {
        Log.d("Login", "Account created");
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
        Log.d("Login", "could not login");
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
        mSignUpButton.setEnabled(enable);
        mNameEditText.setEnabled(enable);
        mEmailEditText.setEnabled(enable);
        mPasswordEditText.setEnabled(enable);
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void showNoInternet() {
        Log.d("Login", "no internte connection!");
    }
}
