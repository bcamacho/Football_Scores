package barqsoft.footballscores.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import barqsoft.footballscores.R;

public class WidgetBroadcastReceiver extends BroadcastReceiver {
    public static int clickCount = 0;
    private String msg[] = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(WidgetUtils.WIDGET_UPDATE_ACTION)) {
            updateWidgetPictureAndButtonListener(context);
        }
    }

    private void updateWidgetPictureAndButtonListener(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.xml.widget_provider);

        // updating view example
//        remoteViews.setTextViewText(R.id.title, getTitle());

    }
//    private String getTitle() {
//        return "FootBall Title ";
//    }
}