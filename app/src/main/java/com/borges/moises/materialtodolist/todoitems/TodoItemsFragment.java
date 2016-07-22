package com.borges.moises.materialtodolist.todoitems;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.addtodoitem.AddTodoItemActivity;
import com.borges.moises.materialtodolist.data.model.Priority;
import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.edittodoitem.EditTodoItemActivity;
import com.borges.moises.materialtodolist.events.TodoItemsListUpdateEvent;
import com.borges.moises.materialtodolist.notifications.ServiceScheduler;
import com.borges.moises.materialtodolist.sync.SyncService;
import com.borges.moises.materialtodolist.utils.DateUtils;
import com.borges.moises.materialtodolist.utils.FirebaseAnalyticsHelper;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.borges.moises.materialtodolist.todoitems.TodoItemsFragment.TodoItemsAdapter.OnCheckBoxClickListener;
import static com.borges.moises.materialtodolist.todoitems.TodoItemsFragment.TodoItemsAdapter.OnStarClickListener;
import static com.borges.moises.materialtodolist.todoitems.TodoItemsFragment.TodoItemsAdapter.OnTodoItemClickListener;

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

    private FirebaseAnalytics mFirebaseAnalytics;

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

    private OnStarClickListener mOnStarClickListener = new OnStarClickListener() {
        @Override
        public void onClick(TodoItem todoItem) {
            mPresenter.changeStarred(todoItem);
        }
    };

    private ItemTouchHelper.SimpleCallback mItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof TodoItemsAdapter.GroupViewHolder) {
                return makeMovementFlags(0, 0);
            } else {
                return super.getMovementFlags(recyclerView, viewHolder);
            }
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            if (direction == ItemTouchHelper.LEFT) {
                TodoItem todoItem = ((TodoItemsAdapter.TodoItemViewHolder) viewHolder).getTodoItem();
                mPresenter.deleteTodoItem(todoItem);
            }
        }
    };

    private TodoItemsAdapter mTodoItemsAdapter = new TodoItemsAdapter(new ArrayList<TodoItemsGroup>(0),
            mCheckBoxClickListener,
            mTodoItemClickListener,
            mOnStarClickListener);

    private TodoItemsMvp.Presenter mPresenter;

    private boolean mTodoItemsLoaded = false;

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

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

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
        if (!mTodoItemsLoaded) {
            mPresenter.loadTodoItems(mTag);
            mTodoItemsLoaded = true;
        }
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mTodoItemsAdapter.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mTodoItemsAdapter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AddTodoItemActivity.REQUEST_CODE:
                if (resultCode == AddTodoItemActivity.ADDED_RESULT_CODE) {
                    long todoItemId = data.getLongExtra(AddTodoItemActivity.TODO_ITEM_ID, -1);
                    if (todoItemId == -1) {
                        throw new IllegalStateException("should receive the todo item id");
                    }
                    mPresenter.loadTodoItem(todoItemId);
                }
                break;
            case EditTodoItemActivity.REQUEST_CODE:
                if (resultCode == EditTodoItemActivity.EDITED_RESULT_CODE) {
                    long todoItemId = data.getLongExtra(EditTodoItemActivity.TODO_ITEM_ID, -1);
                    if (todoItemId == -1) {
                        throw new IllegalStateException("should receive the todo item id");
                    }
                    mPresenter.updateTodoItem(todoItemId);
                } else if (resultCode == EditTodoItemActivity.DELETED_RESULT_CODE) {
                    final long todoItemId = data.getLongExtra(EditTodoItemActivity.TODO_ITEM_ID, -1);
                    if (todoItemId == -1) {
                        throw new IllegalStateException("should receive the todo item id");
                    }
                    mPresenter.deleteTodoItem(todoItemId);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
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
        FirebaseAnalyticsHelper.notifyActionPerformed(mFirebaseAnalytics,"remove_todo_item_from_list");
    }

    @Override
    public void openTodoItemDetails(long todoItemId) {
        EditTodoItemActivity.start(getActivity(), todoItemId);
        FirebaseAnalyticsHelper.notifyActionPerformed(mFirebaseAnalytics,"edit_todo_item");
    }

    @Override
    public void openNewTodoItem() {
        AddTodoItemActivity.start(getActivity());
        FirebaseAnalyticsHelper.notifyActionPerformed(mFirebaseAnalytics,"add_new_todo_item");
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

    @Override
    public void showTodoItems(List<TodoItem> todoItems) {
        mTodoItemsAdapter.replaceData(todoItems);
    }

    public static class TodoItemsGroup implements ParentListItem {

        private final List<TodoItem> mTodoItems;
        private int mName;

        public TodoItemsGroup(List<TodoItem> todoItems, @StringRes int name) {
            mTodoItems = todoItems;
            mName = name;
        }

        @Override
        public List<TodoItem> getChildItemList() {
            return mTodoItems;
        }

        @Override
        public boolean isInitiallyExpanded() {
            return false;
        }

        @StringRes
        public int getName() {
            return mName;
        }

    }

    public static class TodoItemsAdapter extends ExpandableRecyclerAdapter<TodoItemsAdapter.GroupViewHolder, TodoItemsAdapter.TodoItemViewHolder> {

        private List<TodoItemsGroup> mTodoItemsGroups;
        private TodoItemsGroup mLateGroup;
        private TodoItemsGroup mTodayGroup;
        private TodoItemsGroup mFutureGroup;

        private final OnStarClickListener mOnStarClickListener;
        private final OnCheckBoxClickListener mOnCheckBoxClickListener;
        private final OnTodoItemClickListener mOnTodoItemClickListener;

        /**
         * Primary constructor. Sets up {@link #mParentItemList} and {@link #mItemList}.
         * <p>
         * Changes to {@link #mParentItemList} should be made through add/remove methods in
         * {@link ExpandableRecyclerAdapter}
         *
         * @param parentItemList List of all {@link ParentListItem} objects to be
         *                       displayed in the RecyclerView that this
         *                       adapter is linked to
         */
        public TodoItemsAdapter(@NonNull List<TodoItemsGroup> parentItemList,
                                @NonNull OnCheckBoxClickListener onCheckBoxClickListener,
                                @NonNull OnTodoItemClickListener onTodoItemClickListener,
                                @NonNull OnStarClickListener onStarClickListener) {
            super(parentItemList);
            mTodoItemsGroups = parentItemList;
            mOnCheckBoxClickListener = onCheckBoxClickListener;
            mOnTodoItemClickListener = onTodoItemClickListener;
            mOnStarClickListener = onStarClickListener;
        }

        public void deleteTodoItem(TodoItem todoItem) {
            Position position = getTodoItemPosition(todoItem);
            mTodoItemsGroups.get(position.parentPos())
                    .getChildItemList()
                    .remove(position.childPos());
            notifyChildItemRemoved(position.parentPos(), position.childPos());
            notifyParentItemChanged(position.parentPos());
        }

        public void updateTodoItem(TodoItem todoItem) {
            Position position = getTodoItemPosition(todoItem);
            final List<TodoItem> childItemList = mTodoItemsGroups.get(position.parentPos())
                    .getChildItemList();
            childItemList.remove(position.childPos());
            childItemList.add(position.childPos(), todoItem);
            notifyChildItemChanged(position.parentPos(), position.childPos());
        }

        public void addTodoItem(TodoItem todoItem) {
            if (todoItem.getDate() == null) {
                mFutureGroup.getChildItemList().add(todoItem);
            } else {
                switch (DateUtils.compareDateWithToday(todoItem.getDate())) {
                    case -1:
                        mLateGroup.getChildItemList().add(todoItem);
                        break;
                    case 0:
                        mTodayGroup.getChildItemList().add(todoItem);
                        break;
                    case 1:
                        mFutureGroup.getChildItemList().add(todoItem);
                        break;
                }
            }

            Position position = getTodoItemPosition(todoItem);
            notifyChildItemInserted(position.parentPos(), position.childPos());
            notifyParentItemChanged(position.parentPos());

        }

        public void replaceData(List<TodoItem> todoItems) {
            List<TodoItem> lateTasks = new ArrayList<>();
            List<TodoItem> todayTasks = new ArrayList<>();
            List<TodoItem> futureTasks = new ArrayList<>();

            for (TodoItem todoItem : todoItems) {
                if (todoItem.getDate() == null) {
                    futureTasks.add(todoItem);
                    continue;
                }
                switch (DateUtils.compareDateWithToday(todoItem.getDate())) {
                    case -1:
                        lateTasks.add(todoItem);
                        break;
                    case 0:
                        todayTasks.add(todoItem);
                        break;
                    case 1:
                        futureTasks.add(todoItem);
                        break;
                }
            }

            mTodoItemsGroups.clear();
            mLateGroup = new TodoItemsGroup(lateTasks, R.string.late_tasks_group);
            mTodayGroup = new TodoItemsGroup(todayTasks, R.string.today_tasks_group);
            mFutureGroup = new TodoItemsGroup(futureTasks, R.string.future_tasks_group);

            mTodoItemsGroups.add(mLateGroup);
            mTodoItemsGroups.add(mTodayGroup);
            mTodoItemsGroups.add(mFutureGroup);

            notifyParentItemRangeInserted(0, mTodoItemsGroups.size());
        }

        public void clearTodoItems() {
            for (int parentPos = 0; parentPos < mTodoItemsGroups.size(); parentPos++) {
                TodoItemsGroup todoItemsGroup = mTodoItemsGroups.get(parentPos);
                for (int childPos = todoItemsGroup.getChildItemList().size() - 1; childPos >= 0; childPos--) {
                    notifyChildItemRemoved(parentPos, childPos);
                }
            }
        }

        private Position getTodoItemPosition(TodoItem todoItem) {
            int childPosition = Collections.binarySearch(mLateGroup.getChildItemList(),
                    todoItem,
                    TodoItem.comparator());
            int parentPosition;
            if (childPosition >= 0) {
                parentPosition = mTodoItemsGroups.indexOf(mLateGroup);
                return new Position(parentPosition, childPosition);
            }
            childPosition = Collections.binarySearch(mTodayGroup.getChildItemList(),
                    todoItem,
                    TodoItem.comparator());
            if (childPosition >= 0) {
                parentPosition = mTodoItemsGroups.indexOf(mTodayGroup);
                return new Position(parentPosition, childPosition);
            }
            childPosition = Collections.binarySearch(mFutureGroup.getChildItemList(),
                    todoItem,
                    TodoItem.comparator());
            if (childPosition >= 0) {
                parentPosition = mTodoItemsGroups.indexOf(mFutureGroup);
                return new Position(parentPosition, childPosition);
            } else {
                throw new IllegalArgumentException("Todo item is not in the adapter");
            }
        }


        @Override
        public GroupViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
            View layout = LayoutInflater.from(parentViewGroup.getContext())
                    .inflate(R.layout.view_todo_item_group, parentViewGroup, false);
            return new GroupViewHolder(layout);
        }

        @Override
        public TodoItemViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
            View layout = LayoutInflater.from(childViewGroup.getContext())
                    .inflate(R.layout.view_todo_item, childViewGroup, false);
            return new TodoItemViewHolder(layout);
        }

        @Override
        public void onBindParentViewHolder(GroupViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
            final TodoItemsGroup todoItemGroup = (TodoItemsGroup) parentListItem;
            parentViewHolder.mTodoItemGroupNameView.setText(todoItemGroup.getName());
            parentViewHolder.mExpandImageView.setImageResource(parentViewHolder.isExpanded() ?
                    R.drawable.ic_expand_less_black_24dp : R.drawable.ic_expand_more_black_24dp);
            parentViewHolder.mNumOfTasksTextView.setText(String.valueOf(todoItemGroup.mTodoItems.size()));
        }

        @Override
        public void onBindChildViewHolder(TodoItemViewHolder childViewHolder, int position, Object childListItem) {
            final TodoItem todoItem = (TodoItem) childListItem;
            Context context = childViewHolder.mContext;
            childViewHolder.setTodoItem(todoItem);

            if (todoItem.isDone()) {
                childViewHolder.mTitleTextView.setTextColor(ContextCompat.getColor(context, R.color.grey));
                SpannableString spanTitle = new SpannableString(todoItem.getTitle());
                spanTitle.setSpan(new StrikethroughSpan(), 0, spanTitle.length(), 0);
                childViewHolder.mTitleTextView.setText(spanTitle);
            } else {
                childViewHolder.mTitleTextView.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                childViewHolder.mTitleTextView.setText(todoItem.getTitle());
            }

            if (childViewHolder.mDoneCheckBox.isChecked() != todoItem.isDone()) {
                childViewHolder.mDoneCheckBox.setChecked(todoItem.isDone());
            }

            final Resources resources = context.getResources();
            VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat
                    .create(resources,
                            todoItem.getPriority() == Priority.HIGH ? R.drawable.ic_star_black_24px : R.drawable.ic_star_border_black_24px,
                            null);
            vectorDrawableCompat.setTint(resources.getColor(todoItem.getPriority() == Priority.HIGH ? R.color.gold : android.R.color.black));
            childViewHolder.mStarImage.setImageDrawable(vectorDrawableCompat.mutate());
            childViewHolder.mStarImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnStarClickListener.onClick(todoItem);
                }
            });

            childViewHolder.mDoneCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.isPressed()) {
                        mOnCheckBoxClickListener.onClick(todoItem, isChecked);
                    }
                }
            });
            childViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnTodoItemClickListener.onClick(todoItem);
                }
            });
        }

        public class GroupViewHolder extends ParentViewHolder {
            final TextView mTodoItemGroupNameView;
            final ImageView mExpandImageView;
            final TextView mNumOfTasksTextView;

            /**
             * Default constructor.
             *
             * @param itemView The {@link View} being hosted in this ViewHolder
             */
            public GroupViewHolder(View itemView) {
                super(itemView);
                mTodoItemGroupNameView = (TextView) itemView.findViewById(R.id.todo_item_group_name_text_view);
                mExpandImageView = (ImageView) itemView.findViewById(R.id.expand_tasks_image);
                mNumOfTasksTextView = (TextView) itemView.findViewById(R.id.num_of_tasks_text_view);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isExpanded()) {
                            collapseView();
                            turnExpandIcon(-180);
                        } else {
                            expandView();
                            turnExpandIcon(180);
                        }
                    }
                });
            }

            private void turnExpandIcon(int value) {
                mExpandImageView.animate()
                        .rotationBy(value)
                        .setDuration(300)
                        .start();
            }

            @Override
            public boolean shouldItemViewClickToggleExpansion() {
                return false;
            }
        }

        public class TodoItemViewHolder extends ChildViewHolder {

            final TextView mTitleTextView;
            final CheckBox mDoneCheckBox;
            final ImageView mStarImage;
            private WeakReference<TodoItem> mTodoItemWeakReference;
            Context mContext;

            /**
             * Default constructor.
             *
             * @param itemView The {@link View} being hosted in this ViewHolder
             */
            public TodoItemViewHolder(View itemView) {
                super(itemView);
                mContext = itemView.getContext();
                mTitleTextView = (TextView) itemView.findViewById(R.id.todo_item_title);
                mDoneCheckBox = (CheckBox) itemView.findViewById(R.id.todo_item_done);
                mStarImage = (ImageView) itemView.findViewById(R.id.todo_item_stared);
            }

            public void setTodoItem(TodoItem todoItem) {
                mTodoItemWeakReference = new WeakReference<TodoItem>(todoItem);
            }

            public TodoItem getTodoItem() {
                if (mTodoItemWeakReference == null) {
                    return null;
                }
                return mTodoItemWeakReference.get();
            }
        }

        private class Position {
            private final int parentPos;
            private final int childPos;

            public Position(int parentPos, int childPos) {
                this.parentPos = parentPos;
                this.childPos = childPos;
            }

            public int parentPos() {
                return parentPos;
            }

            public int childPos() {
                return childPos;
            }
        }

        public interface OnCheckBoxClickListener {
            void onClick(TodoItem todoItem, boolean done);
        }

        public interface OnTodoItemClickListener {
            void onClick(TodoItem todoItem);
        }

        public interface OnStarClickListener {
            void onClick(TodoItem todoItem);
        }
    }
}
