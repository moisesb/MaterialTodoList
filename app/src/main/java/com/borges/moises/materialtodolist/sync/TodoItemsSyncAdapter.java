package com.borges.moises.materialtodolist.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by moises.anjos on 25/05/2016.
 */
public class TodoItemsSyncAdapter extends AbstractThreadedSyncAdapter {

    public TodoItemsSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d("SyncAdapter", "sync todo items with firebase!!!");
    }
}
