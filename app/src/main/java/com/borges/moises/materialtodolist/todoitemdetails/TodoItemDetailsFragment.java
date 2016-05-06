package com.borges.moises.materialtodolist.todoitemdetails;

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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Moisés on 24/04/2016.
 */
public class TodoItemDetailsFragment extends Fragment implements TodoItemDetailsContract.View{

    private static final String ARG_TODO_ITEM_ID = "com.borges.moises.materialtodolist.todoitemdetails.TodoItemDetailsFragment.todoItemId";
    private long mTodoItemId;

    @Bind(R.id.todo_item_title_edit_text)
    EditText mTitleEditText;

    @Bind(R.id.todo_item_description_edit_text)
    EditText mDescriptionEditText;

    @Bind(R.id.todo_item_date_edit_text)
    EditText mDateEditText;

    @Bind(R.id.todo_item_time_edit_text)
    EditText mTimeEditText;

    private TodoItemDetailsContract.PresenterOps mPresenterOps;
    private Date mDate;

    private final DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mDate = DateUtils.getDate(year, monthOfYear, dayOfMonth);
            mDateEditText.setText(DateUtils.dateToUiString(mDate));
        }
    };


    private final TimePickerDialog.OnTimeSetListener mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            final String time = hourOfDay + ":" + minute;
            mTimeEditText.setText(time);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTodoItemId = getArguments().getLong(ARG_TODO_ITEM_ID, -1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_item,container,false);
        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenterOps = new TodoItemDetailsPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenterOps.openTodoItem(mTodoItemId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenterOps.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_todo_item_details,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deteleTodoItem();
                return true;
            case R.id.action_edit:
                editTodoItem();
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
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

    private void editTodoItem() {
        final String title = mTitleEditText.getText().toString();
        final String description = mDescriptionEditText.getText().toString();
        //final boolean urgent = mUrgentCheckBox.isSelected();
        final String time = mTimeEditText.getText().toString();
        //mPresenterOps.editTodoItem(mTodoItemId, title, description, urgent, mDate, time);
    }

    private void deteleTodoItem() {
        mPresenterOps.deleteTodoItem(mTodoItemId);
    }

    public static Fragment newFragment(long todoItemId) {
        Fragment fragment = new TodoItemDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ARG_TODO_ITEM_ID,todoItemId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void showTitle(String title) {
        mTitleEditText.setText(title);
    }

    @Override
    public void showDescription(String description) {
        mDescriptionEditText.setText(description);
    }

    @Override
    public void showDate(Date date) {
        mDate = date;
        mDateEditText.setText(DateUtils.dateToUiString(date));
    }

    @Override
    public void showUrgent(boolean urgent) {
        //mUrgentCheckBox.setSelected(urgent);
    }

    @Override
    public void showMissingTitle() {

    }

    @Override
    public void showDateInThePast() {

    }

    @Override
    public void close() {
        getActivity().finish();
    }
}
