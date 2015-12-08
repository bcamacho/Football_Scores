package barqsoft.footballscores.service;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class WidgetService extends RemoteViewsService {

    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_MATCHTIME = 2;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int[] widgetIds = intent.getExtras().getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        ComponentName widget = new ComponentName(this, WidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        RemoteViews updateViews = buildUpdate(this, manager, widgetIds);
        manager.updateAppWidget(widget, updateViews);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return null;
    }

    public RemoteViews buildUpdate(Context context, AppWidgetManager manager, int[] appWidgetIds) {

    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activity_widget);
    manager.updateAppWidget(appWidgetIds, views);
    //Retrieve current game score
    Date now = new Date(System.currentTimeMillis());
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
    String[] args = {simpleDate.format(now)};

    // Get score data
    Uri uri = DatabaseContract.scores_table.buildScoreWithDate();

    Cursor cursor = getContentResolver().query(uri, null, null, args, null);
    // Move to beginning of cursor.
    if(cursor.moveToFirst()){
        views.setTextViewText(R.id.home_name, cursor.getString(COL_HOME));
        views.setTextViewText(R.id.away_name, cursor.getString(COL_AWAY));
        views.setImageViewResource(R.id.home_crest, Utilies.getTeamCrestByTeamName(cursor.getString(COL_HOME)));
        views.setImageViewResource(R.id.away_crest, Utilies.getTeamCrestByTeamName(cursor.getString(COL_AWAY)));
        views.setTextViewText(R.id.match_score, Utilies.getScores(cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS)));
        views.setTextViewText(R.id.match_time, cursor.getString(COL_MATCHTIME));
     } else {
        views.setTextViewText(R.id.home_name, "");
        views.setTextViewText(R.id.away_name, "");
        views.setTextViewText(R.id.match_score, "No Game");
        views.setTextViewText(R.id.match_time, "Currently there is no game");
    }
    cursor.close();
    // Create an Intent to launch ExampleActivity
    Intent intent = new Intent(context, MainActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
    views.setOnClickPendingIntent(R.id.btn_view_game, pendingIntent);


    return views;
    }

    }