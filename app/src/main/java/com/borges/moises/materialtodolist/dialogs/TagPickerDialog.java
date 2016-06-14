package com.borges.moises.materialtodolist.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.data.model.Tag;

import java.util.List;

/**
 * Created by Moisés on 13/06/2016.
 */

public class TagPickerDialog extends DialogFragment {

    private List<Tag> mTags;
    private OnTagSelectedListener mListener;
    private String noTag;
    private String createNewTag;

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
        noTag = getResources().getString(R.string.no_tag);
        createNewTag = getResources().getString(R.string.create_tag);

        final String[] tagNames = tagNames();

        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.select_tag)
                .setItems(tagNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onTagSelected(mTags.get(which));
                    }
                })
                .create();
    }

    private String[] tagNames() {
        String[] tagNames = new String[mTags.size()+2];
        int pos = 0;
        tagNames[pos++] = noTag;
        for (Tag tag : mTags) {
            tagNames[pos++] = tag.getName();
        }
        tagNames[pos] = createNewTag;
        return new String[0];
    }

    public interface OnTagSelectedListener {
        void onTagSelected(Tag tag);
    }

    public static void show(@NonNull FragmentManager fragmentManager,
                            @NonNull OnTagSelectedListener onTagSelectedListener,
                            @NonNull List<Tag> tags) {

    }
}
