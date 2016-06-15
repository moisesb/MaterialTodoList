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
import com.borges.moises.materialtodolist.data.model.Tag;

/**
 * Created by moises.anjos on 15/06/2016.
 */

public class TagDialog extends DialogFragment {

    private Tag mTag;
    private AddCallback mAddCallback;
    private EditCallback mEditCallback;

    public void setTag(Tag mTag) {
        this.mTag = mTag;
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
        if (mTag != null) {
            nameEditTExt.setText(mTag.getName());
        }
        return new AlertDialog.Builder(getContext())
                .setTitle(dialogTitle())
                .setView(layout)
                .setPositiveButton(dialogActionText(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mTag == null) {
                            mAddCallback.onAddTag(nameEditTExt.getText().toString());
                        }else {
                            mEditCallback.onEditTag(mTag,nameEditTExt.getText().toString());
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
        return mTag == null ? R.string.add : R.string.edit;
    }

    private int dialogTitle() {
        return mTag == null? R.string.add_tag: R.string.edit_tag;
    }

    public interface AddCallback {
        void onAddTag(String tagName);
    }

    public interface EditCallback {
        void onEditTag(Tag tag, String newTagName);
    }

    public static void showAddTag(@NonNull FragmentManager fragmentManager,
                                  @NonNull AddCallback addCallback) {
        TagDialog dialog = new TagDialog();
        dialog.setAddCallback(addCallback);
        dialog.show(fragmentManager,"TagDialog");
    }

    public static void showEditTag(@NonNull FragmentManager fragmentManager,
                                   @NonNull Tag tag,
                                   @NonNull EditCallback aeditCallbackdCallback) {
        TagDialog dialog = new TagDialog();
        dialog.setEditCallback(aeditCallbackdCallback);
        dialog.setTag(tag);
        dialog.show(fragmentManager,"TagDialog");
    }
}
