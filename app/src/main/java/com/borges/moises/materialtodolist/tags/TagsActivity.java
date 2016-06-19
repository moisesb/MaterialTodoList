package com.borges.moises.materialtodolist.tags;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.data.model.TasksByTag;
import com.borges.moises.materialtodolist.data.repository.SqliteTagsRepository;
import com.borges.moises.materialtodolist.data.services.TodoItemService;
import com.borges.moises.materialtodolist.dialogs.DeleteTagDialog;
import com.borges.moises.materialtodolist.dialogs.TagDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by moises.anjos on 14/06/2016.
 */

public class TagsActivity extends AppCompatActivity implements TagsMvp.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tags_recycler_view)
    RecyclerView mTagsRecyclerView;

    private TagsMvp.Presenter mPresenter;

    private TagAdapter mAdapter = new TagAdapter(new ArrayList<TasksByTag>(0),
            new DeleteCallback() {
                @Override
                public void onDeleteTag(TasksByTag tasksByTag) {
                    mPresenter.askOrDeleteTag(tasksByTag);
                }
            },
            new EditCallback() {
                @Override
                public void onEditTag(TasksByTag tasksByTag) {
                    TagDialog.showEditTag(getSupportFragmentManager(), tasksByTag, new TagDialog.EditCallback() {
                        @Override
                        public void onEditTag(TasksByTag tasksByTag, String newTagName) {
                            mPresenter.renameTag(tasksByTag, newTagName);
                        }
                    });
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        ButterKnife.bind(this);
        setupToolbar();
        initRecyclerView();
        mPresenter = new TagsPresenter(SqliteTagsRepository.getInstance(), new TodoItemService());
        mPresenter.bindView(this);
    }

    private void initRecyclerView() {
        mTagsRecyclerView.setHasFixedSize(true);
        mTagsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTagsRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadTags();
    }

    @Override
    protected void onDestroy() {
        mPresenter.unbindView();
        super.onDestroy();
    }

    @OnClick(R.id.fab)
    void onAddTagClick() {
        TagDialog.showAddTag(getSupportFragmentManager(), new TagDialog.AddCallback() {
            @Override
            public void onAddTag(String tagName) {
                mPresenter.addTag(tagName);
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, TagsActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void showTags(List<TasksByTag> tasksByTags) {
        mAdapter.replaceData(tasksByTags);
    }

    @Override
    public void showTagNotAddedError() {

    }

    @Override
    public void showTagAdded(TasksByTag tasksByTag) {
        mAdapter.addTag(tasksByTag);
        Toast.makeText(this, R.string.tag_added, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showTagDeleted(TasksByTag tasksByTag) {
        mAdapter.removeTag(tasksByTag);
        Toast.makeText(this, R.string.tag_deleted, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showTagNotEditedError() {
        Toast.makeText(this, R.string.tag_not_edited, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAskToDeleteTagDialog(TasksByTag tasksByTag) {
        DeleteTagDialog.show(getSupportFragmentManager(), tasksByTag, new DeleteTagDialog.DeleteTagCallback() {
            @Override
            public void onDeleteTag(TasksByTag tasksByTag) {
                mPresenter.deleteTag(tasksByTag);
            }
        });
    }

    @Override
    public void updateTag(TasksByTag tasksByTag) {
        mAdapter.updateTag(tasksByTag);
        Toast.makeText(this, R.string.tag_edited, Toast.LENGTH_SHORT).show();
    }

    public interface DeleteCallback {
        void onDeleteTag(TasksByTag tasksByTag);
    }

    public interface EditCallback {
        void onEditTag(TasksByTag tasksByTag);
    }

    public static class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

        private final List<TasksByTag> mTasksByTags;
        private final DeleteCallback mDeleteCallback;
        private final EditCallback mEditCallback;

        public TagAdapter(@NonNull ArrayList<TasksByTag> tasksByTags,
                          @NonNull DeleteCallback deleteCallback,
                          @NonNull EditCallback editCallback) {
            mTasksByTags = tasksByTags;
            mDeleteCallback = deleteCallback;
            mEditCallback = editCallback;
        }

        public void replaceData(@NonNull List<TasksByTag> tags) {
            mTasksByTags.clear();
            mTasksByTags.addAll(tags);
            notifyDataSetChanged();
        }

        public void addTag(TasksByTag tasksByTag) {
            for (int i = 0; i < mTasksByTags.size(); i++) {
                TasksByTag tasksByTagFromAdapter = mTasksByTags.get(i);
                if (tasksByTagFromAdapter.getTag().getName().compareTo(tasksByTag.getTag().getName()) > 0) {
                    mTasksByTags.add(i, tasksByTag);
                    notifyItemInserted(i);
                    break;
                }
            }
        }

        @Override
        public TagAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tag_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TagAdapter.ViewHolder holder, int position) {
            final TasksByTag tasksByTag = mTasksByTags.get(position);
            holder.tagName.setText(tasksByTag.getTag().getName());
            final int numOfTasks = tasksByTag.getNumOfTasks();
            final String numOfTasksMessage = numOfTasksMessage(holder.itemView.getContext(), numOfTasks);
            holder.numOfTasks.setText(numOfTasksMessage);
            holder.deleteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDeleteCallback.onDeleteTag(tasksByTag);
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditCallback.onEditTag(tasksByTag);
                }
            });
        }

        private String numOfTasksMessage(Context context, int numOfTasks) {
            if (numOfTasks > 0) {
                 return context.getResources()
                         .getQuantityString(R.plurals.number_of_tasks,numOfTasks,numOfTasks);
            }else {
                return context.getResources()
                        .getString(R.string.zero_tasks);
            }
        }

        @Override
        public int getItemCount() {
            return mTasksByTags.size();
        }

        public void removeTag(TasksByTag tasksByTag) {
            for (int i = 0; i < mTasksByTags.size(); i++) {
                if (mTasksByTags.get(i).getTag().getId() == tasksByTag.getTag().getId()) {
                    mTasksByTags.remove(i);
                    notifyItemRemoved(i);
                    break;
                }
            }
        }

        public void updateTag(TasksByTag tasksByTag) {
            for (int i = 0; i < mTasksByTags.size(); i++) {
                if (mTasksByTags.get(i).getTag().getId() == tasksByTag.getTag().getId()) {
                    notifyItemChanged(i);
                    break;
                }
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tagName;
            TextView numOfTasks;
            ImageView deleteImg;

            public ViewHolder(View itemView) {
                super(itemView);

                tagName = (TextView) itemView.findViewById(R.id.tag_name_text_view);
                numOfTasks = (TextView) itemView.findViewById(R.id.num_of_tasks_text_view);
                deleteImg = (ImageView) itemView.findViewById(R.id.delete_tag_image);
            }
        }
    }
}
