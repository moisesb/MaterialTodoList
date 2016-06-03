package com.borges.moises.materialtodolist.settings;

import com.borges.moises.materialtodolist.data.model.Settings;

/**
 * Created by moises.anjos on 02/06/2016.
 */
public class SettingsPresenter implements SettingsMvp.Presenter {

    private Settings mSettings;
    private SettingsMvp.View mView;

    public SettingsPresenter() {
        mSettings = Settings.getInstance();
    }

    @Override
    public void loadSettings() {
        if (mSettings.isNotificationEnabled()) {
            mView.showTime(mSettings.getNotificationHour(),mSettings.getNotificationMinute());
        }else {
            mView.hideTime();
        }
        mView.showNotificationEnabled(mSettings.isNotificationEnabled());
    }

    @Override
    public void saveSettings(boolean notificationEnabled, int hour, int minute) {
        if (notificationEnabled && (hour > -1 || minute > -1)) {
            mView.showShouldProvideTimeMessage();
            return;
        }

        mSettings.setNotificationEnabled(notificationEnabled);
        mSettings.setNotificationHour(hour);
        mSettings.setNotificationMinute(minute);
    }

    @Override
    public void bindView(SettingsMvp.View view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }
}
