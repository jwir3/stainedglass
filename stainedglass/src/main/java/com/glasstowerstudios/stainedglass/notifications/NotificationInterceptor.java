package com.glasstowerstudios.stainedglass.notifications;

import android.content.IntentFilter;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NotificationInterceptor extends NotificationListenerService {

  // === [ Android Event Handlers ] ================================================================

  @Override
  public void onCreate() {
    super.onCreate();
    Log.d(LOGTAG, "Notification interceptor created");
    mNotificationsSeen = new ArrayList<NotificationBundle>();

    // We'll need the broadcast receiver to communicate with the main activity/app.
    // The main app will also need one to receive messages from us.
    mReceiver = new NotificationInterceptorReceiver();
    IntentFilter filter = new IntentFilter();
    filter.addAction("com.glasstowerstudios.stainedglass.notifications.NOTIFY_LED_BLINK");
    registerReceiver(mReceiver, filter);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    unregisterReceiver(mReceiver);
  }


  @Override
  public void onNotificationPosted(StatusBarNotification aNotification) {
    Log.d(LOGTAG, "onNotificationPosted called!");
//    final String packageName = String.valueOf(aNotification.getPackageName());
//    PackageManager packageManager = this.getPackageManager();
//
//    // We want the icon and name for the application sending the notification.
//    try {
//      ApplicationInfo appInfo = this.getPackageManager().getApplicationInfo(packageName, 0);
//      String appName = packageManager.getApplicationLabel(appInfo).toString();
//      Drawable appIcon = packageManager.getApplicationIcon(appInfo);
//
//      mNotificationsSeen.add(new NotificationBundle(appIcon, packageName, appName));
//    } catch (android.content.pm.PackageManager.NameNotFoundException e) {
//      // How can we get a name not found exception from an event notification?
//      Log.e(LOGTAG, "Unable to find package with name: " + packageName);
//
//      // We could use a toast here, if we're in one of our activities, but since we're in a
//      // service, we can't do that.
//      throw new RuntimeException(e);
//    }

  }

  @Override
  public void onNotificationRemoved(StatusBarNotification aNotification) {

  }

  // === [ Public API ] ============================================================================
  public List<NotificationBundle> getNotificationsSeen() {
    return mNotificationsSeen;
  }


  // === [ Private Member Variables ] ==============================================================

  private List<NotificationBundle> mNotificationsSeen;
  private NotificationInterceptorReceiver mReceiver;

  private static final String LOGTAG = "NotificationInterceptor";
}
