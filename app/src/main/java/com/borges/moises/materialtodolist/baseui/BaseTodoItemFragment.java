package com.borges.moises.materialtodolist.baseui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.data.model.Priority;
import com.borges.moises.materialtodolist.data.model.Tag;
import com.borges.moises.materialtodolist.dialogs.PriorityPickerDialog;
import com.borges.moises.materialtodolist.dialogs.TagPickerDialog;
import com.borges.moises.materialtodolist.tags.TagsActivity;
import com.borges.moises.materialtodolist.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Moisés on 14/04/2016.
 */
public abstract class BaseTodoItemFragment extends Fragment implements PriorityPickerDialog.OnPrioritySelectedListener, TagPickerDialog.OnTagSelectedListener{

    @BindString(R.string.title_required)
    protected String mTitleRequiedString;

    @BindView(R.id.todo_item_title_edit_text)
    protected EditText mTitleEditText;

    @BindView(R.id.todo_item_date_edit_text)
    protected EditText mDateEditText;

    @BindView(R.id.todo_item_description_edit_text)
    protected EditText mDescriptionEditText;

    @BindView(R.id.todo_item_time_edit_text)
    protected EditText mTimeEditText;

    @BindView(R.id.todo_item_location_edit_text)
    protected EditText mLocationEditText;

    @BindView(R.id.todo_item_priority_edit_text)
    protected EditText mPriorityEditText;

    @BindView(R.id.todo_item_tag_edit_text)
    protected EditText mTagEditText;

    protected int mYear = -1, mMonthOfYear = -1 , mDayOfMonth = -1, mHourOfDay = -1, mMinute = -1;
    protected Priority mPriority = null;
    protected Tag mTag = null;
    protected List<Tag> mAvaliableTags;

    private final DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonthOfYear = monthOfYear;
            mDayOfMonth = dayOfMonth;
            mDateEditText.setText(getDate(year, monthOfYear, dayOfMonth));
        }
    };

    private final TimePickerDialog.OnTimeSetListener mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mHourOfDay = hourOfDay;
            mMinute = minute;
            mTimeEditText.setText(DateUtils.getTime(hourOfDay, minute));
        }
    };



    protected String getDate(int year, int monthOfYear, int dayOfMonth) {
        final Date date = DateUtils.getDate(year, monthOfYear, dayOfMonth);
        return DateUtils.dateToUiString(date);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_item,container,false);
        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);
        return view;
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

    @OnClick(R.id.todo_item_tag_edit_text) void onTagClick() {
        TagPickerDialog.show(getFragmentManager(),this, mAvaliableTags);
    }

    @Override
    public void onPrioritySelected(Priority priority) {
        mPriority = priority;
        String priorityText = getResources().getString(priority.stringResId());
        mPriorityEditText.setText(priorityText);
    }

    @Override
    public void onNoTagSelected() {
        mTag = null;
        mTagEditText.setText("");
    }

    @Override
    public void onCreateNewTagSelected() {
        TagsActivity.start(getContext());
    }

    @Override
    public void onTagSelected(Tag tag) {
        mTag = tag;
        mTagEditText.setText(tag.getName());
    }
}
