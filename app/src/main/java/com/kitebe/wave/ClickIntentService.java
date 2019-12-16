package com.kitebe.wave;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;

import static com.kitebe.wave.MyWidget.updateAppWidget;

public class ClickIntentService extends IntentService {
    public static final String ACTION_CLICK = "com.kitebe.wave.widgets.click";

    public ClickIntentService() {
        super("ClickIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            if (ACTION_CLICK.equals(action)) {
                handleClick();
            }
        }
    }

    private void handleClick() {
        int clicks = getSharedPreferences("sp", MODE_PRIVATE).getInt("clicks", 0);
        clicks++;
        getSharedPreferences("sp", MODE_PRIVATE)
                .edit()
                .putInt("clicks", clicks)
                .apply();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, MyWidget.class));
        for (int appWidgetId : widgetIds) {
            updateAppWidget(getApplicationContext(), appWidgetManager, appWidgetId);

        }
    }
}