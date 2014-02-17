package com.glasstowerstudios.stainedglass.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationInterceptorReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context aContext, Intent aIntent) {
    Log.d(LOGTAG, "onReceive called");
  }

  // === [ Private Data Members ] ==================================================================

  private static String LOGTAG = "NotificationInterceptorService";
}
