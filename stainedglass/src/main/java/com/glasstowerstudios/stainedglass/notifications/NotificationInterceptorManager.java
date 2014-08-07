package com.glasstowerstudios.stainedglass.notifications;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.glasstowerstudios.stainedglass.registry.StainedGlassServiceRegistry;

// @Singleton
public class NotificationInterceptorManager {

  // === [ Public API ] ============================================================================

  public static NotificationInterceptorManager getInstance() {
    return NotificationInterceptorManagerHolder.sManager;
  }

  public void startService() {
    Context context = StainedGlassServiceRegistry.getInstance().getApplicationContext();
    if (mCommChannel == null) {
      Intent servIntent = new Intent(context, NotificationInterceptor.class);
      context.startService(servIntent);
    }

    connectToService();
  }

  public void stopService() {
    Context context = StainedGlassServiceRegistry.getInstance().getApplicationContext();
    if (mCommChannel != null) {
      Intent servIntent = new Intent(context, NotificationInterceptor.class);
      context.stopService(servIntent);
    }
  }

  public void connectToService() {
    Context context = StainedGlassServiceRegistry.getInstance().getApplicationContext();
    Intent servIntent = new Intent(context, NotificationInterceptor.class);
    boolean bound = context.bindService(servIntent, mServiceConnection, 0);
    if (!bound) {
      // Well, that's not good.
      Log.w(LOGTAG, "Unable to connect to NotificationInterceptor service");
    }
  }

  // === [ Private API ] ===========================================================================

  // Singleton holder class for thread-safe lazy initialization
  private static final class NotificationInterceptorManagerHolder {
    public static NotificationInterceptorManager sManager = new NotificationInterceptorManager();
  }

  private NotificationInterceptorManager() {
    mServiceConnection = new ServiceConnection() {

      @Override
      public void onServiceConnected(ComponentName name, IBinder service) {
        mCommChannel = service;
      }

      @Override
      public void onServiceDisconnected(ComponentName name) {
        mCommChannel = null;
      }
    };
  }

  // === [ Private Data Members ] ==================================================================

  private ServiceConnection mServiceConnection;
  private IBinder mCommChannel;

  private static final String LOGTAG = NotificationInterceptorManager.class.getSimpleName();
}
