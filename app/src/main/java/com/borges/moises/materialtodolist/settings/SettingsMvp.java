package com.borges.moises.materialtodolist.settings;

import com.borges.moises.materialtodolist.mvp.MvpPresenter;
import com.borges.moises.materialtodolist.mvp.MvpView;

/**
 * Created by moises.anjos on 02/06/2016.
 */
public interface SettingsMvp {
    interface View extends MvpView {
        void showNotificationEnabled(boolean enabled);

        void hideTime();

        void showTime(int hour, int minute);

        void showShouldProvideTimeMessage();

        void close();

        void showSettingsSaved();
    }

    interface Presenter extends MvpPresenter<View> {
        void loadSettings();

        void saveSettings(boolean notificationEnabled, int hour, int minute);
    }
}
