package com.borges.moises.materialtodolist.settings;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.utils.DateUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by moises.anjos on 02/06/2016.
 */
public class SettingsActivity extends AppCompatActivity implements SettingsMvp.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.notification_enabled_checkbox)
    CheckBox mEnabledCheckBox;

    @BindView(R.id.notification_time_edit_text)
    EditText mTimeEditText;

    private final TimePickerDialog.OnTimeSetListener mListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            notificationHour = hourOfDay;
            notificationMinute = minute;
            showTime(hourOfDay,minute);
        }
    };

    private int notificationHour = -1;
    private int notificationMinute = -1;

    private SettingsMvp.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setupToolbar();

        mPresenter = new SettingsPresenter();
        mPresenter.bindView(this);
        mPresenter.loadSettings();
    }

    @Override
    protected void onDestroy() {
        mPresenter.unbindView();
        super.onDestroy();
    }

    @OnClick(R.id.notification_time_edit_text)
    void onTimeClick() {
        TimePickerDialog dialog = new TimePickerDialog(this, mListener, notificationHour, notificationMinute, true);
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveSettings() {
        final boolean enabled = mEnabledCheckBox.isChecked();
        mPresenter.saveSettings(enabled,notificationHour,notificationMinute);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void showNotificationEnabled(boolean enabled) {
        mEnabledCheckBox.setChecked(enabled);
    }

    @Override
    public void hideTime() {
        mTimeEditText.setVisibility(View.GONE);
    }

    @Override
    public void showTime(int hour, int minute) {
        notificationHour = hour;
        notificationMinute = minute;
        mTimeEditText.setText(DateUtils.getTime(hour, minute));
        mTimeEditText.setVisibility(View.VISIBLE);
    }

    @Override
    public void showShouldProvideTimeMessage() {

    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void showSettingsSaved() {
        Toast.makeText(this,R.string.settings_saved,Toast.LENGTH_SHORT)
            .show();
    }
}
