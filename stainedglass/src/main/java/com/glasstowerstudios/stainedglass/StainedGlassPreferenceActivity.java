package com.glasstowerstudios.stainedglass;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.util.Log;

import com.glasstowerstudios.stainedglass.notifications.NotificationInterceptorManager;

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

      SwitchPreference serviceOnOff = (SwitchPreference) findPreference(getString(R.string.notificationServicePref));

      if (serviceOnOff == null) {
        // Well, this is awkward.
        Log.e(LOGTAG, "No switch to turn the service on or off was found...");
        throw new NullPointerException("No switch to turn the service on or off was found.");
      }

      serviceOnOff.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
          boolean switchVal = ((Boolean) newValue).booleanValue();
          if (preference.getKey().equals(getString(R.string.notificationServicePref))) {
            Log.d(LOGTAG, "Saw service preference change to: " + switchVal);
              NotificationInterceptorManager mgr = NotificationInterceptorManager.getInstance();
              Activity curAct = ServicesFragment.this.getActivity();
            if (switchVal) {
              mgr.startService(curAct);
            } else {
              mgr.stopService(curAct);
            }

            return true;
          }

          Log.w(LOGTAG, "Unable to find preference with key: " + preference.getKey());
          return false;
        }
      });
    }
  }

  // === [ Private Data Members ] ==================================================================

  private static final String LOGTAG = StainedGlassPreferenceActivity.class.getCanonicalName();
}
