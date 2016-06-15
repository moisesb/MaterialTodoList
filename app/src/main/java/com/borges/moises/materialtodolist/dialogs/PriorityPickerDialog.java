package com.borges.moises.materialtodolist.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.data.model.Priority;

/**
 * Created by moises.anjos on 06/05/2016.
 */
public class PriorityPickerDialog extends DialogFragment {

    private static final String TAG = "PriorityPickerDialog";

    private OnPrioritySelectedListener mListener;

    public PriorityPickerDialog() {
        super();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Priority[] priorities = Priority.values();

        final String[] priorityNames = getPriorityNames(priorities);

        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.select_priority)
                .setItems(priorityNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onPrioritySelected(priorities[which]);
                    }
                })
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
    }

    private String[] getPriorityNames(Priority[] priorities) {
        String priorityNames[] = new String[priorities.length];
        for (int i = 0; i < priorityNames.length; i++) {
            Priority priority = priorities[i];
            priorityNames[i] = getResources().getString(priority.stringResId());
        }
        return priorityNames;
    }


    private void setListener(OnPrioritySelectedListener listener) {
        mListener = listener;
    }

    public interface OnPrioritySelectedListener {
        void onPrioritySelected(Priority priority);
    }

    public static void show(FragmentManager fragmentManager, OnPrioritySelectedListener listener) {
        PriorityPickerDialog dialog = new PriorityPickerDialog();
        dialog.setListener(listener);
        dialog.show(fragmentManager, TAG);

    }

}
