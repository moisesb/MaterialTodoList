package com.borges.moises.materialtodolist.todoitems;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.addtodoitem.AddTodoItemActivity;
import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.todoitemdetails.TodoItemDetailsActivity;
import com.borges.moises.materialtodolist.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Moisés on 11/04/2016.
 */
public class TodoItemsFragment extends Fragment implements TodoItemsContract.View {

    @Bind(R.id.todo_items_recyclerview)
    RecyclerView mTodoItemsRecyclerView;

    @Bind(R.id.message_textview)
    TextView mMessageTextView;

    private OnCheckBoxClickListener mCheckBoxClickListener = new OnCheckBoxClickListener() {
        @Override
        public void onClick(TodoItem todoItem, boolean done) {
            mPresenterOps.doneTodoItem(todoItem,done);
        }
    };

    private OnTodoItemClickListener mTodoItemClickListener = new OnTodoItemClickListener() {
        @Override
        public void onClick(TodoItem todoItem) {
            mPresenterOps.openTodoItem(todoItem);
        }
    };

    private ItemTouchHelper.SimpleCallback mItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            Log.d("Test", "from " + viewHolder.getAdapterPosition() + "to " + target.getAdapterPosition());
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            if (direction == ItemTouchHelper.LEFT) {
                TodoItem todoItem = mTodoItemsAdpter.getTodoItem(viewHolder.getAdapterPosition());
                mPresenterOps.deleteTodoItem(todoItem);
            }
        }
    };

    private TodoItemsAdpter mTodoItemsAdpter = new TodoItemsAdpter(new ArrayList<TodoItem>(0),
            mCheckBoxClickListener,
            mTodoItemClickListener);

    private FloatingActionButton mAddNoteFloatingActionButton;

    private TodoItemsContract.PresenterOps mPresenterOps;


    public TodoItemsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_items, container, false);
        ButterKnife.bind(this, view);

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
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mTodoItemsRecyclerView);

        mTodoItemsRecyclerView.setHasFixedSize(true);
        mTodoItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mTodoItemsRecyclerView.setAdapter(mTodoItemsAdpter);
    }

    @Override
    public void showTodoItems(List<TodoItem> todoItems) {
        mMessageTextView.setVisibility(View.GONE);
        mTodoItemsRecyclerView.setVisibility(View.VISIBLE);
        mTodoItemsAdpter.replaceData(todoItems);
    }

    @Override
    public void removeTodoItem(TodoItem todoItem) {
        mTodoItemsAdpter.deleteTodoItem(todoItem);
    }

    @Override
    public void openTodoItemDetails(long todoItemId) {
        TodoItemDetailsActivity.start(getContext(),todoItemId);
    }

    @Override
    public void openNewTodoItem() {
        AddTodoItemActivity.start(getContext());
    }

    @Override
    public void showNoTodoItemMessage() {
        mTodoItemsRecyclerView.setVisibility(View.GONE);
        mMessageTextView.setVisibility(View.VISIBLE);
    }

    public interface OnCheckBoxClickListener {
        void onClick(TodoItem todoItem, boolean done);
    }

    public interface OnTodoItemClickListener {
        void onClick(TodoItem todoItem);
    }

    public static class TodoItemsAdpter extends RecyclerView.Adapter<TodoItemsAdpter.ViewHolder> {

        private List<TodoItem> mTodoItems;
        private OnCheckBoxClickListener mOnCheckBoxClickListener;
        private OnTodoItemClickListener mOnTodoItemClickListener;

        public TodoItemsAdpter(List<TodoItem> todoItems,
                               @NonNull OnCheckBoxClickListener onCheckBoxClickListener,
                               @NonNull OnTodoItemClickListener onTodoItemClickListener) {
            mTodoItems = todoItems;
            mOnCheckBoxClickListener = onCheckBoxClickListener;
            mOnTodoItemClickListener = onTodoItemClickListener;
        }

        public void replaceData(List<TodoItem> todoItems) {
            mTodoItems = todoItems;
            notifyDataSetChanged();
        }

        public void deleteTodoItem(TodoItem todoItem) {
            int position = getTodoItemPosition(todoItem);
            mTodoItems.remove(position);
            notifyItemRemoved(position);
        }

        public void updateTodoItem(TodoItem todoItem) {
            int position = getTodoItemPosition(todoItem);
            notifyItemChanged(position);
        }

        private int getTodoItemPosition(TodoItem todoItem) {
            int position = mTodoItems.indexOf(todoItem);
            if (position >= 0) {
                return position;
            } else {
                throw new IllegalArgumentException("Todo item is not in the adapter");
            }
        }

        public TodoItem getTodoItem(int position) {
            return mTodoItems.size() > position ? mTodoItems.get(position) : null;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_todo_item, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            TodoItem todoItem = mTodoItems.get(position);
            holder.bind(todoItem, mOnCheckBoxClickListener, mOnTodoItemClickListener);
        }

        @Override
        public int getItemCount() {
            return mTodoItems.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            private final View mView;
            private final TextView mTitleTextView;
            private final TextView mDateTextView;
            private final CheckBox mDoneCheckBox;
            private Context mContext;

            public ViewHolder(View itemView) {
                super(itemView);
                mContext = itemView.getContext();
                mView = itemView;
                mTitleTextView = (TextView) itemView.findViewById(R.id.todo_item_title);
                mDateTextView = (TextView) itemView.findViewById(R.id.todo_item_date);
                mDoneCheckBox = (CheckBox) itemView.findViewById(R.id.todo_item_done);
            }

            public void bind(final TodoItem todoItem,
                             final OnCheckBoxClickListener onCheckBoxClickListener,
                             final OnTodoItemClickListener onTodoItemClickListener) {
                updateViewHolder(todoItem);
                mDoneCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        onCheckBoxClickListener.onClick(todoItem, isChecked);
                        updateViewHolder(todoItem);
                    }
                });
                mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onTodoItemClickListener.onClick(todoItem);
                    }
                });
            }

            private void updateViewHolder(final TodoItem todoItem) {
                if (todoItem.getDate() == null) {
                    mDateTextView.setVisibility(View.GONE);
                } else {
                    mDateTextView.setText(DateUtils.dateToUiString(todoItem.getDate()));
                    mDateTextView.setVisibility(View.VISIBLE);
                }

                if (todoItem.isCompleted()) {
                    mTitleTextView.setTextColor(ContextCompat.getColor(mContext,R.color.grey));
                    mDateTextView.setTextColor(ContextCompat.getColor(mContext,R.color.grey));
                    SpannableString spanTitle = new SpannableString(todoItem.getTitle());
                    spanTitle.setSpan(new StrikethroughSpan(),0,spanTitle.length(),0);
                    mTitleTextView.setText(spanTitle);
                }else {
                    mTitleTextView.setTextColor(ContextCompat.getColor(mContext,android.R.color.black));
                    mDateTextView.setTextColor(ContextCompat.getColor(mContext,R.color.red));
                    mTitleTextView.setText(todoItem.getTitle());
                }

                if (mDoneCheckBox.isChecked() != todoItem.isCompleted()) {
                    mDoneCheckBox.setChecked(todoItem.isCompleted());
                }
            }
        }
    }
}
