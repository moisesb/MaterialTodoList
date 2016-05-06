package com.borges.moises.materialtodolist.addtodoitem;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.addtodoitem.presenter.AddTodoItemPresenter;
import com.borges.moises.materialtodolist.addtodoitem.presenter.AddTodoItemPresenterImpl;
import com.borges.moises.materialtodolist.addtodoitem.view.AddTodoItemView;
import com.borges.moises.materialtodolist.data.model.Priority;
import com.borges.moises.materialtodolist.dialogs.PriorityPickerDialog;
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
public class AddTodoItemFragment extends Fragment implements AddTodoItemView, PriorityPickerDialog.OnPrioritySelectedListener {

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

    @Bind(R.id.todo_item_location_edit_text)
    EditText mLocationEditText;

    @Bind(R.id.todo_item_priority_edit_text)
    EditText mPriorityEditText;

    private int mYear = -1, mMonthOfYear = -1 , mDayOfMonth = -1, mHourOfDay = -1, mMinute = -1;
    private Priority mPriority = null;

    private AddTodoItemPresenter mPresenter;

    private final DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            final Date date = DateUtils.getDate(year, monthOfYear, dayOfMonth);
            mYear = year;
            mMonthOfYear = monthOfYear;
            mDayOfMonth = dayOfMonth;
            mDateEditText.setText(DateUtils.dateToUiString(date));
        }
    };


    private final TimePickerDialog.OnTimeSetListener mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            final String time = hourOfDay + ":" + minute;
            mHourOfDay = hourOfDay;
            mMinute = minute;
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
        mPresenter = new AddTodoItemPresenterImpl();
        mPresenter.bindView(this);
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

    @Override
    public void onDestroy() {
        mPresenter.unbindView();
        super.onDestroy();
    }

    @OnTextChanged(R.id.todo_item_title_edit_text) void onTitleTextChanged(CharSequence text) {
        //mTitleInputLayout.setError(null);
    }

    @OnTextChanged(R.id.todo_item_date_edit_text) void onDateTextChanged(CharSequence text) {
        //mDateInputLayout.setError(null);
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

    @OnClick(R.id.todo_item_priority_edit_text) void onPriorityClick() {
        PriorityPickerDialog.show(getFragmentManager(),this);
    }

    public static Fragment newFragment() {
        return new AddTodoItemFragment();
    }

    private void addTodoItem() {
        final String title = mTitleEditText.getText().toString();
        final String description = mDescriptionEditText.getText().toString();
        final String location = mLocationEditText.getText().toString();
        mPresenter.addTodoItem(title,description,mPriority,location,mYear,mMonthOfYear,mDayOfMonth,mHourOfDay,mMinute);
    }

    @Override
    public void showMissingTitle() {
        // TODO: 06/05/2016 Implement ASAP
    }

    @Override
    public void close() {
        getActivity().finish();
    }


    @Override
    public void onPrioritySelected(Priority priority) {
        mPriority = priority;
        String priorityText = getResources().getString(priority.stringResId());
        mPriorityEditText.setText(priorityText);
    }
}
