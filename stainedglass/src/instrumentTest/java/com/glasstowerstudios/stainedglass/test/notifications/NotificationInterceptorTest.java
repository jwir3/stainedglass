package com.glasstowerstudios.stainedglass.test.notifications;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.os.UserManager;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.test.ServiceTestCase;

import com.glasstowerstudios.stainedglass.R;
import com.glasstowerstudios.stainedglass.notifications.NotificationBundle;
import com.glasstowerstudios.stainedglass.notifications.NotificationInterceptor;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

public class NotificationInterceptorTest extends ServiceTestCase<NotificationInterceptor> {

  public NotificationInterceptorTest() {
    super(NotificationInterceptor.class);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
  }

  public void tearDown() throws Exception {
    shutdownService();
    super.tearDown();
  }

  public void testServiceStartedAndDestroyed() {
    Intent testIntent = new Intent(getContext(), NotificationInterceptor.class);
    startService(testIntent);

    NotificationInterceptor s = getService();

    assertNotNull("Service should be non-null", s);
    assertTrue(s.isReady());

    shutdownService();
    assertFalse(s.isReady());
  }

  public void testServiceBoundAndDestroyed() {
    Intent testIntent = new Intent(getContext(), NotificationInterceptor.class);
    bindService(testIntent);

    NotificationInterceptor s = getService();

    assertNotNull("Service should be non-null", s);
    assertTrue(s.isReady());

    shutdownService();
    assertFalse(s.isReady());
  }

  public void testMultipleCalls() {
    Intent testIntent = new Intent(getContext(), NotificationInterceptor.class);

    startService(testIntent);
    startService(testIntent);

    assertEquals(1, getService().getNumOnCreates());
    assertEquals(2, getService().getNumOnStartCommands());

    shutdownService();

    assertFalse(getService().isReady());
  }


  public void testNotificationIntercepted() throws Exception {
    bindService(new Intent(getContext(), NotificationInterceptor.class));

    // Our service is already started. Create and send a notification.
    NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
    builder.setContentTitle("Hello World");
    builder.setSmallIcon(R.drawable.ic_launcher);
    builder.setContentText("What's up?");
    Notification notif = builder.build();
//    NotificationManager mgr =
//        (NotificationManager)getContext().getSystemService(Context.NOTIFICATION_SERVICE);
//    mgr.notify(1492, notif);

    // Now, get our service and make sure that the notification was intercepted.
    // We're sort of mocking this out, because we can't get it to connect _quite_ right at this
    // time.
    final NotificationInterceptor interceptor = getService();
    UserManager userManager = (UserManager)getSystemContext().getSystemService(Context.USER_SERVICE);
    Method getUserHandle = UserManager.class.getMethod("getUserHandle");
    int userHandle = (Integer) getUserHandle.invoke(userManager);

    UserHandle uh = userManager.getUserForSerialNumber(userHandle);
    StatusBarNotification received = new StatusBarNotification("com.glasstowerstudios.stainedglass", "com.glasstowerstudios.stainedglass.test", 1492, "NotificationInterceptorTest", 24, 98, 100, notif, uh, new Date().getTime());
    interceptor.onNotificationPosted(received);

    List<NotificationBundle> notifications = interceptor.getNotificationsSeen();

    assertEquals(1, notifications.size());

//    mgr.cancel(1492);
  }



}
