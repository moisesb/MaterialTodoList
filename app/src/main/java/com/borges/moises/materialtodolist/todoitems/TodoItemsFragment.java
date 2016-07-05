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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Moisés on 11/04/2016.
 */
public class TodoItemsFragment extends Fragment implements TodoItemsMvp.View {

    private static final String TAG_ARG =
            "com.borges.moises.materialtodolist.todoitems.TodoItemsFragment.tag";

    @BindView(R.id.todo_items_recyclerview)
    RecyclerView mTodoItemsRecyclerView;

    @BindView(R.id.no_todo_items_layout)
    LinearLayout mNoTodoItemsLayout;

    @BindView(R.id.first_time_layout)
    LinearLayout mFirstTimeLayout;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.add_todo_item_button)
    AppCompatButton mAddTodoItemButton;

    private Long mTag = null;

    private boolean mFirstRun = false;

    private CoordinatorLayout mCoordinatorLayout;

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

        if (getArguments() != null) {
            mTag = getArguments().getLong(TAG_ARG);
        }
        mCoordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);

        final FloatingActionButton mAddNoteFloatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        mAddNoteFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewTodoItem();
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showProgress(false);
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
        startServicesOnFirstRun();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerForEvents();
        mPresenter.loadTodoItems(mTag);
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

    @OnClick(R.id.add_todo_item_button)
    void onAddTodoItemClick() {
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
        mPresenter.loadTodoItems(mTag);
    }

    public static Fragment newInstace(Long tagId) {
        final TodoItemsFragment fragment = new TodoItemsFragment();
        if (tagId != null) {
            Bundle bundle = new Bundle();
            bundle.putLong(TAG_ARG, tagId);
            fragment.setArguments(bundle);
        }
        return fragment;
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

        mFirstRun = getActivity()
                .getSharedPreferences(preference, Context.MODE_PRIVATE)
                .getBoolean(appFirstRun, true);

        if (mFirstRun) {
            SyncService.start(getContext());

            ServiceScheduler serviceScheduler = new ServiceScheduler();
            serviceScheduler.setAlarm(getContext());

            getActivity().getSharedPreferences(preference, Context.MODE_PRIVATE)
                    .edit().putBoolean(appFirstRun, false)
                    .apply();
        }
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

    @Override
    public void openTodoItemDetails(long todoItemId) {
        EditTodoItemActivity.start(getContext(), todoItemId);
    }

    @Override
    public void openNewTodoItem() {
        AddTodoItemActivity.start(getContext());
    }

    @Override
    public void showNoTodoItemMessage() {
        mTodoItemsRecyclerView.setVisibility(View.GONE);
        mNoTodoItemsLayout.setVisibility(mFirstRun ? View.GONE : View.VISIBLE);
        mFirstTimeLayout.setVisibility(mFirstRun ? View.VISIBLE : View.GONE);

        if (mFirstRun) {
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

    @Override
    public void clearTodoItems() {
        mTodoItemsAdapter.clearTodoItems();
    }

    @Override
    public void updateTodoItem(TodoItem todoItem) {
        mTodoItemsAdapter.updateTodoItem(todoItem);
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

        public void updateTodoItem(TodoItem todoItem) {
            int position = getTodoItemPosition(todoItem);
            notifyItemChanged(position);
        }

        public void clearTodoItems() {
            mTodoItems.clear();
            notifyDataSetChanged();
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
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final TodoItem todoItem = mTodoItems.get(position);
            Context context = holder.mContext;

            if (todoItem.getDate() == null) {
                holder.mDateTextView.setVisibility(View.GONE);
            } else {
                holder.mDateTextView.setText(DateUtils.dateToUiString(todoItem.getDate()));
                holder.mDateTextView.setVisibility(View.VISIBLE);
            }

            if (todoItem.isDone()) {
                holder.mTitleTextView.setTextColor(ContextCompat.getColor(context, R.color.grey));
                holder.mDateTextView.setTextColor(ContextCompat.getColor(context, R.color.grey));
                SpannableString spanTitle = new SpannableString(todoItem.getTitle());
                spanTitle.setSpan(new StrikethroughSpan(), 0, spanTitle.length(), 0);
                holder.mTitleTextView.setText(spanTitle);
            } else {
                holder.mTitleTextView.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                holder.mDateTextView.setTextColor(ContextCompat.getColor(context, R.color.red));
                holder.mTitleTextView.setText(todoItem.getTitle());
            }

            if (holder.mDoneCheckBox.isChecked() != todoItem.isDone()) {
                holder.mDoneCheckBox.setChecked(todoItem.isDone());
            }

            switch (todoItem.getPriority()) {
                case HIGH:
                    setPriorityStripBackground(ContextCompat.getDrawable(context, R.color.red), holder);
                    break;
                case NORMAL:
                    setPriorityStripBackground(ContextCompat.getDrawable(context, R.color.blue), holder);
                    break;
                case LOW:
                    setPriorityStripBackground(ContextCompat.getDrawable(context, R.color.green), holder);
                    break;
            }

            holder.mDoneCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.isPressed()) {
                        mOnCheckBoxClickListener.onClick(todoItem, isChecked);
                    }
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnTodoItemClickListener.onClick(todoItem);
                }
            });
        }

        private void setPriorityStripBackground(Drawable drawable, ViewHolder holder) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.mPriorityStripeView.setBackground(drawable);
            } else {
                holder.mPriorityStripeView.setBackgroundDrawable(drawable);
            }
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

        public static class ViewHolder extends RecyclerView.ViewHolder {

            final View mView;
            final TextView mTitleTextView;
            final TextView mDateTextView;
            final CheckBox mDoneCheckBox;
            final View mPriorityStripeView;
            Context mContext;

            public ViewHolder(View itemView) {
                super(itemView);
                mContext = itemView.getContext();
                mView = itemView;
                mTitleTextView = (TextView) itemView.findViewById(R.id.todo_item_title);
                mDateTextView = (TextView) itemView.findViewById(R.id.todo_item_date);
                mDoneCheckBox = (CheckBox) itemView.findViewById(R.id.todo_item_done);
                mPriorityStripeView = itemView.findViewById(R.id.todo_item_priority);
            }


        }
    }
}
