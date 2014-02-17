package com.glasstowerstudios.stainedglass.test.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.test.ServiceTestCase;

import com.glasstowerstudios.stainedglass.notifications.NotificationBundle;
import com.glasstowerstudios.stainedglass.notifications.NotificationInterceptor;

import java.util.List;

public class NotificationInterceptorTest extends ServiceTestCase<NotificationInterceptor> {

  public NotificationInterceptorTest() {
    super(NotificationInterceptor.class);
  }

  @Override
  public void setUp() {
    setupService();
  }

  public void tearDown() {
    shutdownService();
  }

  public void testServiceStarted() {
    Service s = getService();

    assertNotNull("Service should be non-null", s);
  }

  public void testNotificationIntercepted() throws Exception {
    // Our service is already started. Create and send a notification.
    NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
    builder.setContentTitle("Hello World");
    Notification notif = builder.build();
    NotificationManager mgr =
        (NotificationManager)getContext().getSystemService(Context.NOTIFICATION_SERVICE);
    mgr.notify(1492, notif);

    // Now, get our service and make sure that the notification was intercepted.
    NotificationInterceptor iceptor = getService();
    List<NotificationBundle> notifications = iceptor.getNotificationsSeen();

    assertEquals(1, notifications.size());
  }

}
