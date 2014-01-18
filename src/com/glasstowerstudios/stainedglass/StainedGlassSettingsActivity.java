package com.glasstowerstudios.stainedglass;

import android.app.*;
import android.os.Bundle;

public class StainedGlassSettingsActivity extends Activity {

  @Override
  public void onCreate(Bundle aSavedInstanceBundle) {
    super.onCreate(aSavedInstanceBundle);
    final ActionBar actionBar = getActionBar();
    actionBar.setTitle(R.string.settings_name);
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
  }

}
