package com.borges.moises.materialtodolist.data.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.util.Patterns;

import com.borges.moises.materialtodolist.data.model.User;
import com.facebook.login.LoginManager;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by moises.anjos on 11/05/2016.
 */
public class UserService {
    public static final int PASSWORD_MIN_LENGHT = 3;

    private static final String ENDPOINT = "https://material-todo-list.firebaseio.com/";
    private static final String USERS = "users";
    public static final String FACEBOOK_PROVIDER = "facebook";

    private final Firebase ref = new Firebase(ENDPOINT);

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
        return Patterns.EMAIL_ADDRESS.matcher(email).find();
    }

    @Nullable
    public User getSignedInUser() {
        return mSessionManager.getLoggedUser();
    }

    public void createUser(final String email, final String password, final String userName, final CreateAccountListener listener) {

        if (!hasInternetConnection()) {
            listener.onNetworkError();
            return;
        }


        ref.createUser(email, password, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        createUserOnServer(authData.getUid(), userName, email, null, listener);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        listener.onNetworkError();
                    }
                });
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                handleFirebaseError(firebaseError, listener);

            }
        });
    }

    private void handleFirebaseError(FirebaseError firebaseError, CreateAccountListener listener) {
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

    private void createUserOnServer(final String uid, String userName, String email, String imageURL, final CreateAccountListener listener) {
        final User user = new User();
        user.setUid(uid);
        user.setUserName(userName);
        user.setEmail(email);
        user.setImageUrl(imageURL);
        ref.child(USERS).orderByKey()
                .equalTo(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            ref.child(USERS).child(uid).setValue(user);
                            mSessionManager.signInUser(user);
                            listener.onSuccess();
                        } else {
                            listener.onEmailTaken();
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        handleFirebaseError(firebaseError, listener);
                    }
                });

    }

    public void createUser(String authToken, final CreateAccountListener listener) {
        if (!hasInternetConnection()) {
            listener.onNetworkError();
            return;
        }

        ref.authWithOAuthToken("facebook", authToken, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                final String uid = authData.getUid();
                final String email = (String) authData.getProviderData().get("email");
                final String name = (String) authData.getProviderData().get("displayName");
                final String imageURL = (String) authData.getProviderData().get("profileImageURL");
                createUserOnServer(uid, name, email, imageURL, listener);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                handleFirebaseError(firebaseError, listener);
            }
        });

    }

    private boolean hasInternetConnection() {
        ConnectivityManager manager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void login(final String email, final String password, final LoginListener listener) {
        if (!hasInternetConnection()) {
            listener.onNetworkError();
            return;
        }

        ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                handleAuthentication(authData, listener);
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

    private void handleLogin(DataSnapshot dataSnapshot, LoginListener listener) {
        if (dataSnapshot == null) {
            listener.onError();
            return;
        }

        final String email = (String) dataSnapshot.child("email").getValue();
        final String userName = (String) dataSnapshot.child("userName").getValue();
        final String pictureUrl = (String) dataSnapshot.child("imageUrl").getValue();
        User user = new User();
        user.setUid(dataSnapshot.getKey());
        user.setEmail(email);
        user.setUserName(userName);
        user.setImageUrl(pictureUrl);
        mSessionManager.signInUser(user);
        listener.onSuccess();
    }

    private void handleError(FirebaseError firebaseError, LoginListener listener) {
        switch (firebaseError.getCode()) {
            case FirebaseError.NETWORK_ERROR:
                listener.onNetworkError();
                break;
            default:
                listener.onError();
        }
    }

    public void loginWithFacebook(final String authToken, final LoginListener listener) {
        if (!hasInternetConnection()) {
            listener.onNetworkError();
            return;
        }

        ref.authWithOAuthToken(FACEBOOK_PROVIDER, authToken, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                handleAuthentication(authData, listener);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                handleError(firebaseError,listener);
            }
        });
    }

    private void handleAuthentication(AuthData authData, final LoginListener listener) {
        ref.child(USERS).child(authData.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                handleLogin(dataSnapshot, listener);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                handleError(firebaseError, listener);
            }
        });
    }

    public void logout() {
        ref.unauth();
        LoginManager.getInstance().logOut();
        mSessionManager.logout();
    }

    public User getLoggedUser() {
        return mSessionManager.getLoggedUser();
    }

    public boolean hasCreatedAccount() {
        return mSessionManager.hasCreatedAccount();
    }

    public interface CreateAccountListener {
        void onSuccess();

        void onEmailTaken();

        void onEmailInvalid();

        void onNetworkError();
    }

    public interface LoginListener {
        void onSuccess();

        void onError();

        void onNetworkError();
    }

}
