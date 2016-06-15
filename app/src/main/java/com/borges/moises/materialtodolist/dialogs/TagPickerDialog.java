package com.borges.moises.materialtodolist.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.data.model.Tag;

import java.util.List;

/**
 * Created by Moisés on 13/06/2016.
 */

public class TagPickerDialog extends DialogFragment {

    private List<Tag> mTags;
    private OnTagSelectedListener mListener;
    public void setListener(OnTagSelectedListener listener) {
        mListener = listener;
    }

    public void setTags(List<Tag> tags) {
        mTags = tags;
    }

    public TagPickerDialog() {
        super();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final String[] tagNames = tagNames();

        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.select_tag)
                .setItems(tagNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            mListener.onNoTagSelected();
                            return;
                        }
                        if (which == tagNames.length - 1) {
                            mListener.onCreateNewTagSelected();
                            return;
                        }

                        mListener.onTagSelected(mTags.get(which - 1));
                    }
                })
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onNoTagSelected();
                    }
                })
                .create();
    }

    private String[] tagNames() {
        final String noTag = getResources().getString(R.string.no_tag);
        final String createNewTag = getResources().getString(R.string.create_tag);
        String[] tagNames = new String[mTags.size()+2];
        int pos = 0;
        tagNames[pos++] = noTag;
        for (Tag tag : mTags) {
            tagNames[pos++] = tag.getName();
        }
        tagNames[pos] = createNewTag;
        return tagNames;
    }

    public interface OnTagSelectedListener {
        void onNoTagSelected();
        void onTagSelected(Tag tag);
        void onCreateNewTagSelected();
    }

    public static void show(@NonNull FragmentManager fragmentManager,
                            @NonNull OnTagSelectedListener listener,
                            @NonNull List<Tag> tags) {
        TagPickerDialog dialog = new TagPickerDialog();
        dialog.setTags(tags);
        dialog.setListener(listener);
        dialog.show(fragmentManager,"TagPickerDialog");
    }
}
