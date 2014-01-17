package com.glasstowerstudios.stainedglass;

import android.app.*;
import android.content.DialogInterface;
import android.os.*;
import android.support.v4.app.*;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class StainedGlassMainActivity extends FragmentActivity implements
    ActionBar.OnNavigationListener {

  private static final String LOGTAG = "StainedGlassMainActivity";

  private static final int NOTIFICATION_ID = 1730;
  private static final int LED_TIME_ON = 10000;

  Runnable mClearLEDTask = new Runnable() {

    @Override
    public void run() {
      synchronized(StainedGlassMainActivity.this) {
        clearLED();
      }
    }
  };

  Handler mLedHandler = new Handler();

  /**
   * The serialization (saved instance state) Bundle key representing the
   * current dropdown position.
   */
  private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_stained_glass_main);

    // Set up the action bar to show a dropdown list.
    final ActionBar actionBar = getActionBar();
    actionBar.setDisplayShowTitleEnabled(false);
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

    // Set up the dropdown list navigation in the action bar.
    actionBar.setListNavigationCallbacks(
    // Specify a SpinnerAdapter to populate the dropdown list.
    new ArrayAdapter<String>(actionBar.getThemedContext(),
        android.R.layout.simple_list_item_1, android.R.id.text1, new String[] {
            getString(R.string.title_section1) }), this);

    setupButtonHandler();
  }

  private void setupButtonHandler() {
    LinearLayout ll = (LinearLayout)findViewById(R.id.topLevelLayout);
    Button btnTurnOnLed = (Button)ll.findViewById(R.id.turnOnLedButton);
    btnTurnOnLed.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        new AlertDialog.Builder(v.getContext(), AlertDialog.THEME_DEVICE_DEFAULT_DARK)
        .setCancelable(false)
        .setTitle("Confirm")
        .setMessage("Are you sure you want to turn on the LED?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              setLEDColor(255, 0, 0);
              mLedHandler.postDelayed(mClearLEDTask, LED_TIME_ON);
              dialogInterface.dismiss();
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              clearAllNotifications();
              dialogInterface.dismiss();
            }
        })
        .show();
      }

    });
  }

  @Override
  public void onStop() {
    super.onPause();
    //mLedHandler.removeCallbacks(mClearLEDTask);
  }

  @Override
  public void onRestoreInstanceState(Bundle savedInstanceState) {
    // Restore the previously serialized current dropdown position.
    if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
      getActionBar()
          .setSelectedNavigationItem(savedInstanceState
                                         .getInt(STATE_SELECTED_NAVIGATION_ITEM));
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    // Serialize the current dropdown position.
    outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
        .getSelectedNavigationIndex());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.stained_glass_main, menu);
    return true;
  }

  @Override
  public boolean onNavigationItemSelected(int position, long id) {
    // When the given dropdown item is selected, show its contents in the
    // container view.
//    Fragment fragment = new DummySectionFragment();
//    Bundle args = new Bundle();
//    args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
//    fragment.setArguments(args);
//    getSupportFragmentManager().beginTransaction()
//      .replace(R.id.mainActivityLayout, fragment).commit();
    return true;
  }

  private void clearLED() {
    Log.d(LOGTAG, "Clearing LED notification request #" + NOTIFICATION_ID);
    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    Notification notif = new Notification();
    notif.ledARGB = 0x00000000; //Color.rgb(red, green, blue);
    notif.flags = Notification.FLAG_SHOW_LIGHTS;
    notif.ledOnMS = 0;
    notif.ledOffMS = 0;
    nm.notify(NOTIFICATION_ID, notif);
    nm.cancel(NOTIFICATION_ID);
  }

  private void clearAllNotifications() {
    Log.d(LOGTAG, "Clearing all notifications currently present");
    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    clearLED();
    nm.cancelAll();
  }

  private void setLEDColor(int red, int green, int blue) {
    NotificationCompat.Builder mBuilder =
        new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle("My notification")
        .setContentText("Hello World!");

    Log.d(LOGTAG, "Setting LED color to: " + red + ", " + green + ", " + blue);
    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    Notification notif = mBuilder.build();
    notif.ledARGB = 0xFFFF0000; //Color.rgb(red, green, blue);
    notif.flags = notif.flags | Notification.FLAG_SHOW_LIGHTS;
    notif.ledOnMS = 100;
    notif.ledOffMS = 100;
    nm.notify(NOTIFICATION_ID, notif);
  }

  public void displaySettings() {
    new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
    .setCancelable(false)
    .setTitle("Settings")
    .setMessage("Settings").show();
  }
}
