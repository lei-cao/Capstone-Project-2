package com.cao.lei.fit.widget;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.RemoteViews;

import com.cao.lei.fit.DetailActivity;
import com.cao.lei.fit.MainActivity;
import com.cao.lei.fit.R;
import com.cao.lei.fit.data.TrainingSetsContract;
import com.cao.lei.fit.models.TrainingSet;
import com.google.firebase.analytics.FirebaseAnalytics;

public class TodayWidgetIntentService extends IntentService {

    private Context mContext;

    public TodayWidgetIntentService() {
        super("TodayWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mContext = getApplicationContext();
        // Retrieve all of the Today widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, TodayWidgetProvider.class));

        // First, check if the location with this city name exists in the db
        final Cursor trainingSetsCursor = mContext.getContentResolver().query(
                TrainingSetsContract.TrainingsetsEntry.CONTENT_URI,
                MainActivity.TRAININGSETS_COLUMNS,
                null,
                null,
                null);


        if (trainingSetsCursor== null) {
            return;
        }
        if (!trainingSetsCursor.moveToFirst()) {
            trainingSetsCursor.close();
            return;
        }


        // Extract the match data from the Cursor
        String desc = getResources().getString(R.string.widget_remote_content_description);
        String title = trainingSetsCursor.getString(1);
        final String thumbnail = trainingSetsCursor.getString(2);
        String description = trainingSetsCursor.getString(3);
        String videUrl = trainingSetsCursor.getString(4);
        // Perform this loop procedure for each Today widget
        for (int appWidgetId : appWidgetIds) {
            // Find the correct layout based on the widget's width
            int layoutId = R.layout.widget_today;
            final RemoteViews views = new RemoteViews(getPackageName(), layoutId);

            // Add the data to the RemoteViews
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                setRemoteContentDescription(views, desc);
            }

            /*
            having problem using Picasso here.

            final Target target = new Target() {
                @Override
                public void onBitmapLoaded( Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                    views.setImageViewBitmap(R.id.widget_thumbnail, bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable drawable) {
                }

                @Override
                public void onPrepareLoad(Drawable drawable) {
                }
            };
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(mContext)
                            .load(thumbnail)
                            .noFade()
                            .into(target);
                }
            });
            */
            views.setTextViewText(R.id.widget_training_title, title);

            // Create an Intent to launch MainActivity

            TrainingSet ts = new TrainingSet();
            ts.title = title;
            ts.videourl = videUrl;
            ts.description = description;
            ts.thumbnail = thumbnail;

            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, ts.title);

            Intent i = new Intent(mContext, DetailActivity.class).putExtra(DetailActivity.DETAIL_TRAININGSET, ts);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
            views.setOnClickPendingIntent(R.id.widget_trainingSet, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private int getWidgetWidth(AppWidgetManager appWidgetManager, int appWidgetId) {
        // Prior to Jelly Bean, widgets were always their default size
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return getResources().getDimensionPixelSize(R.dimen.widget_today_default_width);
        }
        // For Jelly Bean and higher devices, widgets can be resized
        // the current size can be retrieved from the newly added App Widget Options
        return getWidgetWidthFromOptions(appWidgetManager, appWidgetId);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private int getWidgetWidthFromOptions(AppWidgetManager appWidgetManager, int appWidgetId) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        if (options.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)) {
            int minWidthDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
            // The width returned is in dp, but we'll convert it to pixels to match the other widths
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, minWidthDp, displayMetrics);
        }
        return getResources().getDimensionPixelSize(R.dimen.widget_today_default_width);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description) {
    }
}
