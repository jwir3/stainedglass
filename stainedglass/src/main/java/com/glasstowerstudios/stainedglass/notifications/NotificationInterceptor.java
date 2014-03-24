package com.glasstowerstudios.stainedglass.notifications;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NotificationInterceptor extends NotificationListenerService {

  // === [ Public API ] ============================================================================

  public NotificationInterceptor() {
    mIsReady = false;
    mOnStartCommands = 0;
    mOnCreates = 0;
  }

  // === [ Android Event Handlers ] ================================================================

  @Override
  public void onCreate() {
    super.onCreate();

    Log.d(LOGTAG, "NotificationInterceptor.onCreate() called");
    mNotificationsSeen = new ArrayList<NotificationBundle>();
    mIsReady = true;
    mOnCreates++;

    // We'll need the broadcast receiver to communicate with the main activity/app.
    // The main app will also need one to receive messages from us.
//    mReceiver = new NotificationInterceptorReceiver();
//    IntentFilter filter = new IntentFilter();
//    filter.addAction("com.glasstowerstudios.stainedglass.notifications.NOTIFY_LED_BLINK");
//    registerReceiver(mReceiver, filter);
  }

  @Override
  public int onStartCommand(Intent aIntent, int aFlags, int aStartId) {
    mOnStartCommands++;
    return super.onStartCommand(aIntent, aFlags, aStartId);
  }

  @Override
  public void onDestroy() {
    Log.d(LOGTAG, "Calling onDestroy");
    mIsReady = false;
    super.onDestroy();
//    unregisterReceiver(mReceiver);
  }


  @Override
  public void onNotificationPosted(StatusBarNotification aNotification) {
    Log.d(LOGTAG, "onNotificationPosted called!");

    // We want the icon and name for the application sending the notification.
    try {
      NotificationBundle bundle = getBundleFromStatusBarNotification(aNotification);
      mNotificationsSeen.add(bundle);
    } catch (NotificationTranslationException e) {
      Log.e(LOGTAG, "Error encountered while trying to translate notification", e);
    }
  }

  @Override
  public void onNotificationRemoved(StatusBarNotification aNotification) {
    Log.d(LOGTAG, "onNotificationRemoved() called!");
  }

  // === [ Public API ] ============================================================================

  public boolean isReady() {
    return mIsReady;
  }

  public int getNumOnCreates() {
    return mOnCreates;
  }

  public int getNumOnStartCommands() {
    return mOnStartCommands;
  }


  public List<NotificationBundle> getNotificationsSeen() {
//    refreshNotifications();
    return mNotificationsSeen;
  }


  // === [ Private API ] ===========================================================================

  private class NotificationTranslationException extends Exception {

  }

  private NotificationBundle
  getBundleFromStatusBarNotification(StatusBarNotification aNotification)
  throws NotificationTranslationException {
    final String packageName = String.valueOf(aNotification.getPackageName());
    PackageManager packageManager = this.getPackageManager();

    try {
      ApplicationInfo appInfo = this.getPackageManager().getApplicationInfo(packageName, 0);
      String appName = packageManager.getApplicationLabel(appInfo).toString();
      Drawable appIcon = packageManager.getApplicationIcon(appInfo);

      return new NotificationBundle(appIcon, packageName, appName);
    } catch (android.content.pm.PackageManager.NameNotFoundException e) {
      // How can we get a name not found exception from an event notification?
      Log.e(LOGTAG, "Unable to find package with name: " + packageName);

      // We could use a toast here, if we're in one of our activities, but since we're in a
      // service, we can't do that.
      throw new NotificationTranslationException();
    }
  }

  private void refreshNotifications() {
    StatusBarNotification[] notifications = getActiveNotifications();
    for (StatusBarNotification notif : notifications) {
      try {
        NotificationBundle bundle = getBundleFromStatusBarNotification(notif);
        mNotificationsSeen.add(bundle);
      } catch (NotificationTranslationException e) {
        Log.e(LOGTAG, "Error encountered while trying to translate notification", e);
      }
    }
  }
  // === [ Private Member Variables ] ==============================================================

  private List<NotificationBundle> mNotificationsSeen;
//  private NotificationInterceptorReceiver mReceiver;

  private boolean mIsReady;
  private int mOnCreates;
  private int mOnStartCommands;

  private static final String LOGTAG = "NotificationInterceptor";
}
