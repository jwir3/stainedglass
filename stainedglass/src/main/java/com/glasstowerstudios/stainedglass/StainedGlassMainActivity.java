package com.glasstowerstudios.stainedglass;

import android.app.*;
import android.content.*;
import android.graphics.Color;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.support.v4.app.*;

import com.larswerkman.holocolorpicker.*;

public class StainedGlassMainActivity extends FragmentActivity implements
    ActionBar.OnNavigationListener {

  private static final String LOGTAG = "StainedGlassMainActivity";

  private static final int NOTIFICATION_ID = 1730;
  private static final int LED_DELAY = 2000;

  private int mLEDColor;

  private Runnable mClearLEDTask = new Runnable() {

    @Override
    public void run() {
      synchronized(StainedGlassMainActivity.this) {
        clearLED();
      }
    }
  };

  private Runnable mRunLEDTask = new Runnable() {

    @Override
    public void run() {
      synchronized(StainedGlassMainActivity.this) {
        refreshFromPreferences();
        int red = Color.red(mLEDColor);
        int green = Color.green(mLEDColor);
        int blue = Color.blue(mLEDColor);
        setLEDColor(red, green, blue);
        mLedHandler.postDelayed(mClearLEDTask, mTotalFlashLength);
      }
    }
  };

  private Handler mLedHandler = new Handler();

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
            getString(R.string.home_section) }), this);

    setupButtonHandler();
  }

  private void setupButtonHandler() {
    LinearLayout ll = (LinearLayout)findViewById(R.id.topLevelLayout);
    Button btnTurnOnLed = (Button)ll.findViewById(R.id.turnOnLedButton);
    btnTurnOnLed.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        ColorPicker picker = (ColorPicker)findViewById(R.id.picker);
        mLEDColor = picker.getColor();
        new AlertDialog.Builder(v.getContext(), AlertDialog.THEME_DEVICE_DEFAULT_DARK)
        .setCancelable(false)
        .setTitle("Confirm")
        .setMessage("Are you sure you want to turn on the LED? (LED will not turn on if screen is on, LED notification will be sent in " + (int)(LED_DELAY/1000f) + " seconds)")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              mLedHandler.postDelayed(mRunLEDTask, LED_DELAY);
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

    Intent loadSettingsIntent = new Intent(this, StainedGlassSettingsActivity.class);
    MenuItem settingsMenuItem = menu.findItem(R.id.settings_menu_id);
    settingsMenuItem.setIntent(loadSettingsIntent);

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
    nm.cancel(NOTIFICATION_ID);
  }

  private void clearAllNotifications() {
    Log.d(LOGTAG, "Clearing all notifications currently present");
    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    clearLED();
    nm.cancelAll();
  }

  private void setLEDColor(int red, int green, int blue) {
    refreshFromPreferences();

    NotificationCompat.Builder mBuilder =
        new NotificationCompat.Builder(this);

    Log.d(LOGTAG, "Setting LED color to: " + red + ", " + green + ", " + blue);
    Log.d(LOGTAG, "Led on/off for: " + mSingleFlashLength + "ms");

    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    Notification notif = mBuilder.build();
    notif.ledARGB = Color.rgb(red, green, blue);
    notif.flags = notif.flags | Notification.FLAG_SHOW_LIGHTS;
    notif.ledOnMS = mSingleFlashLength;
    notif.ledOffMS = mSingleFlashLength;
    nm.notify(NOTIFICATION_ID, notif);
  }

  public void displaySettings() {
    new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
    .setCancelable(false)
    .setTitle("Settings")
    .setMessage("Settings").show();
  }

  // === [ Private API ] ===========================================================================

  private void refreshFromPreferences() {
    SharedPreferences prefs =
        getSharedPreferences("com.glasstowerstudios.stainedglass.prefs", MODE_PRIVATE);
    mTotalFlashLength = prefs.getInt("totalFlashLength", 100);
    mSingleFlashLength = prefs.getInt("singleFlashLength", 100);
  }

  // === [ Private Member Variables ] ==============================================================

  private int mTotalFlashLength;
  private int mSingleFlashLength;
}
