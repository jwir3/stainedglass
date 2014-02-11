package com.glasstowerstudios.stainedglass.notifications;

import android.graphics.drawable.Drawable;

public class NotificationBundle {

  // === [ Public API ] ============================================================================

  public NotificationBundle(Drawable aIcon, String aAppPackage, String aAppName) {
    mIcon = aIcon;
    mAppPackage = aAppPackage;
    mName = aAppName;
  }

  public void setContent(String aContent) {
    mContent = aContent;
  }

  // === [ Private Member Variables ] ==============================================================

  // The package name of the application sending the notification
  private String mAppPackage;

  // The human-readable name of the application sending the notification
  private String mName;

  // The content of the notification (message)
  private String mContent;

  // The icon of the application sending the notification
  private Drawable mIcon;
}
