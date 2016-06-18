package com.borges.moises.materialtodolist.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.data.model.TasksByTag;

/**
 * Created by moises.anjos on 15/06/2016.
 */

public class TagDialog extends DialogFragment {

    private TasksByTag mTasksByTag;
    private AddCallback mAddCallback;
    private EditCallback mEditCallback;

    public void setTasksByTag(TasksByTag tasksByTag) {
        mTasksByTag = tasksByTag;
    }

    public void setAddCallback(AddCallback mAddCallback) {
        this.mAddCallback = mAddCallback;
    }

    public void setEditCallback(EditCallback editCallback) {
        mEditCallback = editCallback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final View layout = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_tag,null,false);
        final EditText nameEditTExt = (EditText) layout.findViewById(R.id.tag_name_edit_text);
        if (mTasksByTag != null) {
            nameEditTExt.setText(mTasksByTag.getTag().getName());
        }
        return new AlertDialog.Builder(getContext())
                .setTitle(dialogTitle())
                .setView(layout)
                .setPositiveButton(dialogActionText(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mTasksByTag == null) {
                            mAddCallback.onAddTag(nameEditTExt.getText().toString());
                        }else {
                            mEditCallback.onEditTag(mTasksByTag,nameEditTExt.getText().toString());
                        }
                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
    }

    private int dialogActionText() {
        return mTasksByTag == null ? R.string.add : R.string.edit;
    }

    private int dialogTitle() {
        return mTasksByTag == null? R.string.add_tag: R.string.edit_tag;
    }

    public interface AddCallback {
        void onAddTag(String tagName);
    }

    public interface EditCallback {
        void onEditTag(TasksByTag tasksByTag, String newTagName);
    }

    public static void showAddTag(@NonNull FragmentManager fragmentManager,
                                  @NonNull AddCallback addCallback) {
        TagDialog dialog = new TagDialog();
        dialog.setAddCallback(addCallback);
        dialog.show(fragmentManager,"TagDialog");
    }

    public static void showEditTag(@NonNull FragmentManager fragmentManager,
                                   @NonNull TasksByTag tasksByTag,
                                   @NonNull EditCallback aeditCallbackdCallback) {
        TagDialog dialog = new TagDialog();
        dialog.setEditCallback(aeditCallbackdCallback);
        dialog.setTasksByTag(tasksByTag);
        dialog.show(fragmentManager,"TagDialog");
    }
}
