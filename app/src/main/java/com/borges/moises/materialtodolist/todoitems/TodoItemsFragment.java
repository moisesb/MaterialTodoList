package com.borges.moises.materialtodolist.todoitems;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.addnewtodoitem.AddNewTodoItemActivity;
import com.borges.moises.materialtodolist.data.model.TodoItem;

import java.util.List;

import butterknife.Bind;
import butterknife.BindInt;
import butterknife.ButterKnife;

/**
 * Created by Moisés on 11/04/2016.
 */
public class TodoItemsFragment extends Fragment implements TodoItemsContract.View {

    @Bind(R.id.todo_items_recyclerview)
    RecyclerView mTodoItemsRecyclerView;

    @Bind(R.id.message_textview)
    TextView mMessageTextView;

    @BindInt(R.integer.todo_items_columns)
    int mTodoItemsColumns;

    FloatingActionButton mAddNoteFloatingActionButton;

    private TodoItemsContract.PresenterOps mPresenterOps;

    public TodoItemsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_items,container,false);
        ButterKnife.bind(this,view);

        mAddNoteFloatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        mAddNoteFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenterOps.addNewTodoItem();
            }
        });

        setupRecyclerView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenterOps = new TodoItemsPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenterOps.loadTodoItems();
    }

    @Override
    public void onDestroy() {
        mPresenterOps.onDestroy();
        super.onDestroy();
    }

    public static Fragment newInstace() {
        return new TodoItemsFragment();
    }

    private void setupRecyclerView() {
        mTodoItemsRecyclerView.setHasFixedSize(true);
        mTodoItemsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), mTodoItemsColumns));
    }

    @Override
    public void showTodoItems(List<TodoItem> todoItems) {
        mMessageTextView.setVisibility(View.GONE);
        mTodoItemsRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNewTodoItem() {
        Intent intent = AddNewTodoItemActivity.newIntent(getContext());
        startActivity(intent);
    }

    @Override
    public void showNoTodoItemMessage() {
        mTodoItemsRecyclerView.setVisibility(View.GONE);
        mMessageTextView.setVisibility(View.VISIBLE);
    }
}
