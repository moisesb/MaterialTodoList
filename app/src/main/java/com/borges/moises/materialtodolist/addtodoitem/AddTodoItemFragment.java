package com.borges.moises.materialtodolist.addtodoitem;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by Moisés on 14/04/2016.
 */
public class AddTodoItemFragment extends Fragment implements AddTodoItemContract.View{

    @BindString(R.string.title_requied)
    String mTitleRequiedString;

    @BindString(R.string.invalid_date)
    String mDateInvalidString;

    @Bind(R.id.todo_item_title_edit_text)
    EditText mTitleEditText;

    @Bind(R.id.todo_item_date_edit_text)
    EditText mDateEditText;

    @Bind(R.id.todo_item_description_edit_text)
    EditText mDescriptionEditText;

    @Bind(R.id.todo_item_time_edit_text)
    EditText mTimeEditText;

    @Bind(R.id.todo_item_urgent_checkbox)
    CheckBox mUrgenteCheckBox;

    @Bind(R.id.todo_item_title_input_layout)
    TextInputLayout mTitleInputLayout;

    @Bind(R.id.todo_item_date_input_layout)
    TextInputLayout mDateInputLayout;

    private AddTodoItemContract.PresenterOps mPresenterOps;

    private Date mDate;

    private final DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            final Calendar calendar = Calendar.getInstance();
            calendar.set(year,monthOfYear,dayOfMonth);
            mDate = calendar.getTime();
            mDateEditText.setText(DateUtils.uiDateToString(mDate));
        }
    };

    private final TimePickerDialog.OnTimeSetListener mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            final String time = hourOfDay + ":" + minute;
            mTimeEditText.setText(time);
        }
    };

    public AddTodoItemFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_item,container,false);
        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenterOps = new AddTodoItemPresenter(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_todo_item,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                addTodoItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnTextChanged(R.id.todo_item_title_edit_text) void onTitleTextChanged(CharSequence text) {
        mTitleInputLayout.setError(null);
    }

    @OnTextChanged(R.id.todo_item_date_edit_text) void onDateTextChanged(CharSequence text) {
        mDateInputLayout.setError(null);
    }

    @OnClick(R.id.todo_item_date_edit_text) void onDateClick() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getContext(),mOnDateSetListener, year,month,day);
        dialog.show();
    }

    @OnClick(R.id.todo_item_time_edit_text) void onTimeClick() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog dialog = new TimePickerDialog(getContext(),mOnTimeSetListener,hour,minute,true);
        dialog.show();
    }

    public static Fragment newFragment() {
        return new AddTodoItemFragment();
    }

    private void addTodoItem() {
        final String title = mTitleEditText.getText().toString();
        final String description = mDescriptionEditText.getText().toString();
        final String time = mTimeEditText.getText().toString();
        final boolean isUrgent = mUrgenteCheckBox.isChecked();
        mPresenterOps.addTodoItem(title,description,isUrgent,mDate,time);
    }

    @Override
    public void showMissingTitle() {
        mTitleInputLayout.setError(mTitleRequiedString);
    }

    @Override
    public void showDateInThePast() {
        mDateInputLayout.setError(mDateInvalidString);
    }

    @Override
    public void close() {
        getActivity().finish();
    }
}
