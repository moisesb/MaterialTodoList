package com.borges.moises.materialtodolist.todoitemdetails;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.baseui.BaseTodoItemFragment;
import com.borges.moises.materialtodolist.data.model.Priority;
import com.borges.moises.materialtodolist.todoitemdetails.presenter.TodoItemDetailsPresenter;
import com.borges.moises.materialtodolist.todoitemdetails.presenter.TodoItemDetailsPresenterImpl;
import com.borges.moises.materialtodolist.todoitemdetails.view.TodoItemDetailsView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Moisés on 24/04/2016.
 */
public class TodoItemDetailsFragment extends BaseTodoItemFragment implements TodoItemDetailsView {

    private static final String ARG_TODO_ITEM_ID = "com.borges.moises.materialtodolist.todoitemdetails.TodoItemDetailsFragment.todoItemId";
    private long mTodoItemId;

    private TodoItemDetailsPresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTodoItemId = getArguments().getLong(ARG_TODO_ITEM_ID, -1);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new TodoItemDetailsPresenterImpl(this);
        mPresenter.bindView(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.openTodoItem(mTodoItemId);
    }

    @Override
    public void onDestroy() {
        mPresenter.unbindView();
        super.onDestroy();
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

    private void editTodoItem() {
        final String title = mTitleEditText.getText().toString();
        final String description = mDescriptionEditText.getText().toString();
        final String location = mLocationEditText.getText().toString();
        mPresenter.editTodoItem(mTodoItemId, title, description, mPriority, location, mYear, mMonthOfYear, mDayOfMonth, mHourOfDay, mMinute);
    }

    private void deteleTodoItem() {
        mPresenter.openDeleteConfirmationDialog();
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
         if (date == null) {
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        mYear = calendar.get(Calendar.YEAR);
        mMonthOfYear = calendar.get(Calendar.MONTH);
        mDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        mDateEditText.setText(getDate(mYear,mMonthOfYear,mDayOfMonth));

        mHourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        mTimeEditText.setText(getTime(mHourOfDay,mMinute));

    }

    @Override
    public void showPriority(Priority priority) {
        String priorityStr = getResources().getString(priority.stringResId());
        mPriorityEditText.setText(priorityStr);
    }

    @Override
    public void showLocation(String location) {
        mLocationEditText.setText(location);
    }

    @Override
    public void showMissingTitle() {
        mTitleEditText.setError(mTitleRequiedString);
    }

    @Override
    public void close() {
        getActivity().finish();
    }

    @Override
    public void showTodoItemEdited() {
        Toast.makeText(getContext(),R.string.todo_item_edited,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDeleteConfirmationDialog() {
        ConfirmationDialog.show(getFragmentManager(), new ConfirmationDialog.OnDeleteClickListener() {
            @Override
            public void onClick() {
                mPresenter.deleteTodoItem(mTodoItemId);
            }
        });
    }

    public static class ConfirmationDialog extends DialogFragment {

        public static final String TAG = "ConfirmationDialog";
        private OnDeleteClickListener mListener;

        private void setListener(OnDeleteClickListener listener) {
            mListener = listener;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getContext())
                    .setTitle(R.string.delete_confirmation_dialog_title)
                    .setMessage(R.string.delete_confirmation_dialog_message)
                    .setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mListener.onClick();
                        }
                    })
                    .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create();
        }

        public static void show(FragmentManager fragmentManager,
                                @NonNull OnDeleteClickListener listener) {
            ConfirmationDialog confirmationDialog = new ConfirmationDialog();
            confirmationDialog.setListener(listener);
            confirmationDialog.show(fragmentManager, TAG);
        }

        public interface OnDeleteClickListener {
            void onClick();
        }
    }
}
