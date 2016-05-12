package com.borges.moises.materialtodolist.data.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;

import com.borges.moises.materialtodolist.data.model.User;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.regex.Pattern;

/**
 * Created by moises.anjos on 11/05/2016.
 */
public class UserService {

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final int PASSWORD_MIN_LENGHT = 3;

    private static final String ENDPOINT = "https://material-todo-list.firebaseio.com/";
    private static final String USERS = "users";

    private Context mContext;
    private SessionManager mSessionManager;

    public UserService(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }

        mSessionManager = SessionManager.getInstance();
        mContext = context;
    }

    public boolean isUserNameValid(String userName) {
        return userName != null && userName.length() > 0;
    }

    public boolean isPasswordValid(String password) {
        return password != null && password.trim().length() >= PASSWORD_MIN_LENGHT;
    }

    public boolean isEmailValid(String email) {
        return VALID_EMAIL_ADDRESS_REGEX.matcher(email).find();
    }

    @Nullable
    public User getSignedInUser() {
        return mSessionManager.getSignedInUser();
    }

    public void createUser(final String email, final String password, final String userName, final SignUpListener listener) {

        if (!hasInternetConnection()) {
            listener.onNetworkError();
            return;
        }

        final Firebase ref = new Firebase(ENDPOINT);
        ref.createUser(email, password, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        User user = new User();
                        user.setUserName(userName);
                        user.setEmail(email);
                        ref.child(USERS).child(authData.getUid()).setValue(user);

                        mSessionManager.signInUser(user);

                        listener.onSuccess();
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        listener.onNetworkError();
                    }
                });
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                switch (firebaseError.getCode()) {
                    case FirebaseError.EMAIL_TAKEN:
                        listener.onEmailTaken();
                        break;
                    case FirebaseError.NETWORK_ERROR:
                        listener.onNetworkError();
                        break;
                    case FirebaseError.INVALID_EMAIL:
                        listener.onEmailInvalid();
                        break;
                    default:
                        listener.onNetworkError();
                }

            }
        });
    }

    private boolean hasInternetConnection() {
        ConnectivityManager manager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void signIn(final String email, final String password, final SignInListener listener) {
        if (!hasInternetConnection()) {
            listener.onNetworkError();
            return;
        }

        final Firebase ref = new Firebase(ENDPOINT);
        ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                ref.child(USERS).child(authData.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot == null) {
                            listener.onError();
                        }

                        String email = (String) dataSnapshot.child("email").getValue();
                        String userName = (String) dataSnapshot.child("userName").getValue();
                        User user = new User();
                        user.setEmail(email);
                        user.setUserName(userName);
                        mSessionManager.signInUser(user);
                        listener.onSuccess();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        switch (firebaseError.getCode()) {
                            case FirebaseError.NETWORK_ERROR:
                                listener.onNetworkError();
                                break;
                            default:
                                listener.onError();
                        }
                    }
                });
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                switch (firebaseError.getCode()) {
                    case FirebaseError.NETWORK_ERROR:
                        listener.onNetworkError();
                    default:
                        listener.onError();
                }
            }
        });
    }

    public void logout() {
        mSessionManager.logout();
    }

    public interface SignUpListener {
        void onSuccess();

        void onEmailTaken();

        void onEmailInvalid();

        void onNetworkError();
    }

    public interface SignInListener {
        void onSuccess();

        void onError();

        void onNetworkError();
    }

}
