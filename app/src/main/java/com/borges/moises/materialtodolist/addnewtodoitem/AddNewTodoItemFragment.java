package com.borges.moises.materialtodolist.addnewtodoitem;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import com.borges.moises.materialtodolist.R;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Moisés on 14/04/2016.
 */
public class AddNewTodoItemFragment extends Fragment {

    @Bind(R.id.todo_item_title_edit_text)
    EditText mTitleEditText;

    @Bind(R.id.todo_item_date_edit_text)
    EditText mDateEditText;

    @Bind(R.id.todo_item_description_edit_text)
    EditText mDescriptionEditText;

    @Bind(R.id.todo_item_urgent_checkbox)
    CheckBox mUrgenteCheckBox;

    DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        }
    };

    public AddNewTodoItemFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_item,container,false);
        ButterKnife.bind(this,view);

        return view;
    }

    @OnClick(R.id.todo_item_date_edit_text) void onDateClick() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getContext(),mOnDateSetListener, year,month,day);
        dialog.show();
    }

    public static Fragment newFragment() {
        return new AddNewTodoItemFragment();
    }
}
