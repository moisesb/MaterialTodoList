package com.borges.moises.materialtodolist.todoitems;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.addtodoitem.AddTodoItemActivity;
import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.events.TodoItemsListUpdateEvent;
import com.borges.moises.materialtodolist.notifications.ServiceScheduler;
import com.borges.moises.materialtodolist.sync.SyncService;
import com.borges.moises.materialtodolist.edittodoitem.EditTodoItemActivity;
import com.borges.moises.materialtodolist.utils.DateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Moisés on 11/04/2016.
 */
public class TodoItemsFragment extends Fragment implements TodoItemsMvp.View {

    @Bind(R.id.todo_items_recyclerview)
    RecyclerView mTodoItemsRecyclerView;

    @Bind(R.id.no_todo_items_layout)
    LinearLayout mNoTodoItemsLayout;

    @Bind(R.id.first_time_layout)
    LinearLayout mFirstTimeLayout;

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.add_todo_item_button)
    AppCompatButton mAddTodoItemButton;

    private boolean mFirstRun = false;

    CoordinatorLayout mCoordinatorLayout;

    private OnCheckBoxClickListener mCheckBoxClickListener = new OnCheckBoxClickListener() {
        @Override
        public void onClick(TodoItem todoItem, boolean done) {
            mPresenter.doneTodoItem(todoItem, done);
        }
    };

    private OnTodoItemClickListener mTodoItemClickListener = new OnTodoItemClickListener() {
        @Override
        public void onClick(TodoItem todoItem) {
            mPresenter.openTodoItem(todoItem);
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
                TodoItem todoItem = mTodoItemsAdapter.getTodoItem(viewHolder.getAdapterPosition());
                mPresenter.deleteTodoItem(todoItem);
            }
        }
    };

    private TodoItemsAdapter mTodoItemsAdapter = new TodoItemsAdapter(new ArrayList<TodoItem>(0),
            mCheckBoxClickListener,
            mTodoItemClickListener);

    private TodoItemsMvp.Presenter mPresenter;


    public TodoItemsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_items, container, false);
        ButterKnife.bind(this, view);

        mCoordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);

        final FloatingActionButton mAddNoteFloatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        mAddNoteFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewTodoItem();
            }
        });

        setupRecyclerView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new TodoItemsPresenter();
        mPresenter.bindView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadTodoItems();
        registerForEvents();
        startServicesOnFirstRun();
    }

    private void registerForEvents() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        unregisterForEvents();
        super.onDestroy();
    }

    @OnClick(R.id.add_todo_item_button) void onAddTodoItemClick(){
        mPresenter.addNewTodoItem();
    }

    private void unregisterForEvents() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void handleTodoItemsListChanged(TodoItemsListUpdateEvent event) {
        // TODO: 31/05/2016 finish handle todo items list changed event
        mPresenter.loadTodoItems();
    }

    public static Fragment newInstace() {
        return new TodoItemsFragment();
    }

    private void setupRecyclerView() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mTodoItemsRecyclerView);

        mTodoItemsRecyclerView.setHasFixedSize(true);
        mTodoItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mTodoItemsRecyclerView.setAdapter(mTodoItemsAdapter);
    }

    private void startServicesOnFirstRun() {
        final String preference = "PREFERENCE";
        final String appFirstRun = "appFirstRun";

        mFirstRun =  getActivity()
                .getSharedPreferences(preference, Context.MODE_PRIVATE)
                .getBoolean(appFirstRun, true);

        //if (isFirstRun) {
        SyncService.start(getContext());

        ServiceScheduler serviceScheduler = new ServiceScheduler();
        serviceScheduler.setAlarm(getContext());

        getActivity().getSharedPreferences(preference, Context.MODE_PRIVATE)
                .edit().putBoolean(appFirstRun, false)
                .commit();
        //}
    }

    @Override
    public void showTodoItem(final TodoItem todoItem) {
        mNoTodoItemsLayout.setVisibility(View.GONE);
        mTodoItemsRecyclerView.setVisibility(View.VISIBLE);
        mTodoItemsAdapter.addTodoItem(todoItem);
    }

    @Override
    public void removeTodoItem(TodoItem todoItem) {
        mTodoItemsAdapter.deleteTodoItem(todoItem);
    }

    public void cleanTodoItems() {
        mTodoItemsAdapter.cleanTodoItems();
    }

    @Override
    public void openTodoItemDetails(long todoItemId) {
        EditTodoItemActivity.start(getContext(), todoItemId);
        cleanTodoItems();
    }

    @Override
    public void openNewTodoItem() {
        AddTodoItemActivity.start(getContext());
        cleanTodoItems();
    }

    @Override
    public void showNoTodoItemMessage() {
        mTodoItemsRecyclerView.setVisibility(View.GONE);
        mNoTodoItemsLayout.setVisibility(mFirstRun? View.GONE: View.VISIBLE);
        mFirstTimeLayout.setVisibility(mFirstRun? View.VISIBLE: View.GONE);

        if (mFirstRun){
            mAddTodoItemButton.setEnabled(true);
        }

    }

    @Override
    public void showUndoDeleteOption(final TodoItem todoItem) {
        Snackbar.make(mCoordinatorLayout, R.string.task_deleted, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.undoDelete(todoItem);
                    }
                })
                .show()
        ;
    }

    @Override
    public void showProgress(final boolean loading) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(loading);
            }
        });
    }

    public interface OnCheckBoxClickListener {
        void onClick(TodoItem todoItem, boolean done);
    }

    public interface OnTodoItemClickListener {
        void onClick(TodoItem todoItem);
    }

    public static class TodoItemsAdapter extends RecyclerView.Adapter<TodoItemsAdapter.ViewHolder> {

        private List<TodoItem> mTodoItems;
        private OnCheckBoxClickListener mOnCheckBoxClickListener;
        private OnTodoItemClickListener mOnTodoItemClickListener;

        public TodoItemsAdapter(List<TodoItem> todoItems,
                                @NonNull OnCheckBoxClickListener onCheckBoxClickListener,
                                @NonNull OnTodoItemClickListener onTodoItemClickListener) {
            mTodoItems = todoItems;
            mOnCheckBoxClickListener = onCheckBoxClickListener;
            mOnTodoItemClickListener = onTodoItemClickListener;
        }

        public void deleteTodoItem(TodoItem todoItem) {
            int position = getTodoItemPosition(todoItem);
            mTodoItems.remove(position);
            notifyItemRemoved(position);
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

        public void addTodoItem(TodoItem todoItem) {
            if (!mTodoItems.contains(todoItem)) {
                mTodoItems.add(todoItem);
                final int position = mTodoItems.indexOf(todoItem);
                notifyItemInserted(position);
            }
        }

        public void cleanTodoItems() {
            mTodoItems.clear();
            notifyDataSetChanged();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            private final View mView;
            private final TextView mTitleTextView;
            private final TextView mDateTextView;
            private final CheckBox mDoneCheckBox;
            private final View mPriorityStripeView;
            private Context mContext;

            public ViewHolder(View itemView) {
                super(itemView);
                mContext = itemView.getContext();
                mView = itemView;
                mTitleTextView = (TextView) itemView.findViewById(R.id.todo_item_title);
                mDateTextView = (TextView) itemView.findViewById(R.id.todo_item_date);
                mDoneCheckBox = (CheckBox) itemView.findViewById(R.id.todo_item_done);
                mPriorityStripeView = itemView.findViewById(R.id.todo_item_priority);


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

                if (todoItem.isDone()) {
                    mTitleTextView.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
                    mDateTextView.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
                    SpannableString spanTitle = new SpannableString(todoItem.getTitle());
                    spanTitle.setSpan(new StrikethroughSpan(), 0, spanTitle.length(), 0);
                    mTitleTextView.setText(spanTitle);
                } else {
                    mTitleTextView.setTextColor(ContextCompat.getColor(mContext, android.R.color.black));
                    mDateTextView.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                    mTitleTextView.setText(todoItem.getTitle());
                }

                if (mDoneCheckBox.isChecked() != todoItem.isDone()) {
                    mDoneCheckBox.setChecked(todoItem.isDone());
                }

                switch (todoItem.getPriority()) {
                    case HIGH:
                        setPriorityStripBackground(ContextCompat.getDrawable(mContext, R.color.red));
                        break;
                    case NORMAL:
                        setPriorityStripBackground(ContextCompat.getDrawable(mContext, R.color.blue));
                        break;
                    case LOW:
                        setPriorityStripBackground(ContextCompat.getDrawable(mContext, R.color.green));
                        break;
                }
            }

            private void setPriorityStripBackground(Drawable drawable) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mPriorityStripeView.setBackground(drawable);
                } else {
                    mPriorityStripeView.setBackgroundDrawable(drawable);
                }
            }
        }
    }
}
