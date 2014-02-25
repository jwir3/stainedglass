package com.glasstowerstudios.stainedglass;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

import java.util.List;

public class StainedGlassPreferenceActivity extends PreferenceActivity {

  @Override
  public void onBuildHeaders(List<Header> aTarget) {
    loadHeadersFromResource(R.xml.pref_headers, aTarget);
  }

  @Override
  public boolean isValidFragment(String aString) {
    Log.d(LOGTAG, "Attempting to find if: " + aString + " is a valid fragment");
    if (aString.equals(GeneralFragment.class.getName())) {
      return true;
    } else if (aString.equals(ServicesFragment.class.getName())) {
      return true;
    }

    Log.d(LOGTAG, "Fragment name does not match " + GeneralFragment.class.getName());
    return false;
  }

  public static class GeneralFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle aSavedBundle) {
      super.onCreate(aSavedBundle);

      addPreferencesFromResource(R.xml.pref_general);
    }
  }

  public static class ServicesFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle aSavedBundle) {
      super.onCreate(aSavedBundle);

      addPreferencesFromResource(R.xml.pref_services);
    }
  }

  // === [ Private Data Members ] ==================================================================

  private static final String LOGTAG = StainedGlassPreferenceActivity.class.getCanonicalName();
}
