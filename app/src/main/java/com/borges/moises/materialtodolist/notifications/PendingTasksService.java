package com.borges.moises.materialtodolist.notifications;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.borges.moises.materialtodolist.R;
import com.borges.moises.materialtodolist.data.model.TodoItem;
import com.borges.moises.materialtodolist.data.repository.SqliteTodoItemsRepository;
import com.borges.moises.materialtodolist.data.repository.TodoItemsRepository;
import com.borges.moises.materialtodolist.data.repository.specification.QueryAllTodoItemsSqlSpec;
import com.borges.moises.materialtodolist.todoitems.TodoItemsActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Moisés on 25/04/2016.
 */
public class PendingTasksService extends IntentService {

    private static final String NAME = "PendingTasksService";
    public static final int ID = 001;

    public PendingTasksService() {
        super(NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        TodoItemsRepository todoItemRepository = SqliteTodoItemsRepository.getInstance();
        List<TodoItem> todoItems = todoItemRepository.query(new QueryAllTodoItemsSqlSpec());

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        List<TodoItem> pendingItems = new ArrayList<>();
        for (TodoItem todoItem : todoItems) {
            if (isPending(today, todoItem)) {
                pendingItems.add(todoItem);
            }
        }

        notifyPendingTasks(pendingItems);
    }

    private boolean isPending(Date today, TodoItem todoItem) {
        return todoItem.getDate() != null && today.after(todoItem.getDate())
                && !todoItem.isDeleted() && !todoItem.isDone();
    }

    private void notifyPendingTasks(List<TodoItem> pendingItems) {
        if (pendingItems.size() > 0) {

            Resources resources = getResources();
            final int size = pendingItems.size();
            final int backgroundColor = resources.getColor(R.color.primary);
            String contentText = resources.getQuantityString(R.plurals.pending_task, size, size);

            Intent intent = new Intent(this, TodoItemsActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);

            final String title = resources.getString(R.string.tasks_for_today);

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.drawable.ic_notification)
                    .setColor(backgroundColor)
                    .setContentTitle(title)
                    .setContentText(contentText)
                    .setContentIntent(pendingIntent);

            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            manager.notify(ID, builder.build());

        }
    }

    public static void start(Context context) {
        Intent intentService = new Intent(context, PendingTasksService.class);
        context.startService(intentService);
    }

}
