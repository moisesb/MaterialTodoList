package com.borges.moises.materialtodolist.todoitems;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.util.Log;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.borges.moises.materialtodolist.todoitems.TodoItemsFragment.TodoItemsAdapter.*;

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

    private OnStarClickListener mOnStarClickListener = new OnStarClickListener() {
        @Override
        public void onClick(TodoItem todoItem) {
            mPresenter.changeStarred(todoItem);
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
                //TodoItem todoItem = mTodoItemsAdapter.getTodoItem(viewHolder.getAdapterPosition());
                //mPresenter.deleteTodoItem(todoItem);
                // FIXME: 13/07/2016 implemente this for ExplandableRecyclerView
            }
        }
    };

    private TodoItemsAdapter mTodoItemsAdapter = new TodoItemsAdapter(new ArrayList<TodoItem>(0),
            mCheckBoxClickListener,
            mTodoItemClickListener,
            mOnStarClickListener);

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

    public static class TodoItemsGroup implements ParentListItem {

        private final List<TodoItem> mTodoItems;

        public TodoItemsGroup(List<TodoItem> todoItems) {
            mTodoItems = todoItems;
        }

        @Override
        public List<TodoItem> getChildItemList() {
            return mTodoItems;
        }

        @Override
        public boolean isInitiallyExpanded() {
            return false;
        }
    }

    public static class TodoItemsAdapter extends ExpandableRecyclerAdapter<TodoItemsAdapter.GroupViewHolder,TodoItemsAdapter.TodoItemViewHolder> {

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
            mOnCheckBoxClickListener = onCheckBoxClickListener;
            mOnTodoItemClickListener = onTodoItemClickListener;
            mOnStarClickListener = onStarClickListener;
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

        public void addTodoItem(TodoItem todoItem) {
            if (!mTodoItems.contains(todoItem)) {
                mTodoItems.add(todoItem);
                final int position = mTodoItems.indexOf(todoItem);
                notifyItemInserted(position);
            }
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
        public GroupViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
            return null;
        }

        @Override
        public TodoItemViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
            View layout = LayoutInflater.from(childViewGroup.getContext())
                    .inflate(R.layout.cardview_todo_item, childViewGroup, false);
            return new TodoItemViewHolder(layout);
        }

        @Override
        public void onBindParentViewHolder(GroupViewHolder parentViewHolder, int position, ParentListItem parentListItem) {

        }

        @Override
        public void onBindChildViewHolder(TodoItemViewHolder childViewHolder, int position, Object childListItem) {
            final TodoItem todoItem = (TodoItem) childListItem;
            Context context = childViewHolder.mContext;

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

            VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat
                    .create(context.getResources(),
                            todoItem.getPriority() == Priority.HIGH ? R.drawable.ic_star_black_24px : R.drawable.ic_star_border_black_24px,
                            null);
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
            /**
             * Default constructor.
             *
             * @param itemView The {@link View} being hosted in this ViewHolder
             */
            public GroupViewHolder(View itemView) {
                super(itemView);
            }

            @Override
            public boolean shouldItemViewClickToggleExpansion() {
                return true;
            }
        }

        public class TodoItemViewHolder extends ChildViewHolder {

            final TextView mTitleTextView;
            final CheckBox mDoneCheckBox;
            final ImageView mStarImage;
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
