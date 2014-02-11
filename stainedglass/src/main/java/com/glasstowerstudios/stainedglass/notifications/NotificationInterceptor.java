package com.glasstowerstudios.stainedglass.notifications;

import android.accessibilityservice.AccessibilityService;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.util.ArrayList;
import java.util.List;

public class NotificationInterceptor extends AccessibilityService {

  // === [ Public API ] ============================================================================

  public NotificationInterceptor() {
    mNotificationsSeen = new ArrayList<NotificationBundle>();
  }

  // === [ Android Event Handlers ] ================================================================

  @Override
  public void onAccessibilityEvent(AccessibilityEvent event) {
    if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
      final String packageName = String.valueOf(event.getPackageName());
      PackageManager packageManager = this.getPackageManager();

      // We want the icon and name for the application sending the notification.
      try {
        ApplicationInfo appInfo = this.getPackageManager().getApplicationInfo(packageName, 0);
        String appName = packageManager.getApplicationLabel(appInfo).toString();
        Drawable appIcon = packageManager.getApplicationIcon(appInfo);

        mNotificationsSeen.add(new NotificationBundle(appIcon, packageName, appName));
      } catch (android.content.pm.PackageManager.NameNotFoundException e) {
        // How can we get a name not found exception from an event notification?
        Log.e(LOGTAG, "Unable to find package with name: " + packageName);

        // We could use a toast here, if we're in one of our activities, but since we're in a
        // service, we can't do that.
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void onInterrupt() {

  }

  // === [ Private Member Variables ] ==============================================================

  private List<NotificationBundle> mNotificationsSeen;
  private static final String LOGTAG = "NotificationInterceptor";
}
