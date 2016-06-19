package com.borges.moises.materialtodolist.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.data.model.TasksByTag;

/**
 * Created by Moisés on 19/06/2016.
 */

public class DeleteTagDialog extends DialogFragment {

    private TasksByTag mTasksByTag;
    private DeleteTagCallback mDeleteTagCallback;

    public void setTasksByTag(TasksByTag tasksByTag) {
        mTasksByTag = tasksByTag;
    }

    public void setDeleteTagCallback(DeleteTagCallback deleteTagCallback) {
        mDeleteTagCallback = deleteTagCallback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String deleteMessage = getResources().getQuantityString(R.plurals.delete_tag_message,
                mTasksByTag.getNumOfTasks(),
                mTasksByTag.getTag().getName(),
                mTasksByTag.getNumOfTasks());

        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.delete_tag)
                .setMessage(deleteMessage)
                .setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDeleteTagCallback.onDeleteTag(mTasksByTag);
                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
    }

    public interface DeleteTagCallback {
        void onDeleteTag(TasksByTag tasksByTag);
    }

    public static void show(@NonNull FragmentManager fragmentManager,
                            @NonNull TasksByTag tasksByTag,
                            @NonNull DeleteTagCallback callback) {
        DeleteTagDialog dialog = new DeleteTagDialog();
        dialog.setTasksByTag(tasksByTag);
        dialog.setDeleteTagCallback(callback);
        dialog.show(fragmentManager,"DeleteTagDialog");
    }
}
