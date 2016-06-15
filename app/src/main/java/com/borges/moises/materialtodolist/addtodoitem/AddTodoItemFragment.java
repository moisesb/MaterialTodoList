package com.borges.moises.materialtodolist.addtodoitem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.baseui.BaseTodoItemFragment;
import com.borges.moises.materialtodolist.data.model.Tag;

import java.util.List;

/**
 * Created by Moisés on 14/04/2016.
 */
public class AddTodoItemFragment extends BaseTodoItemFragment implements AddTodoItemMvp.View {

    protected AddTodoItemMvp.Presenter mPresenter;

    public AddTodoItemFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new AddTodoItemPresenter();
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

    public static Fragment newInstance() {
        return new AddTodoItemFragment();
    }

    private void addTodoItem() {
        final String title = mTitleEditText.getText().toString();
        final String description = mDescriptionEditText.getText().toString();
        final String location = mLocationEditText.getText().toString();
        mPresenter.addTodoItem(title,description,mPriority,location,mYear,mMonthOfYear,mDayOfMonth,mHourOfDay,mMinute, mTag);
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
    public void showTodoItemAdded() {
        Toast.makeText(getContext(),R.string.todo_item_added,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addTags(List<Tag> tags) {
        mAvaliableTags = tags;
    }
}
