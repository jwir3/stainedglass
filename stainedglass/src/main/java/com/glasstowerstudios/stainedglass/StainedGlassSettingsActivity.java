package com.glasstowerstudios.stainedglass;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class StainedGlassSettingsActivity extends Activity {

  // === [ Android Activity Callbacks ] ============================================================

  @Override
  public void onCreate(Bundle aSavedInstanceBundle) {
    super.onCreate(aSavedInstanceBundle);
    setContentView(R.layout.activity_stained_glass_settings);

    final ActionBar actionBar = getActionBar();
    actionBar.setTitle(R.string.settings_name);
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

    setupSaveButton();
    setupInputs();
  }

  // === [ Public API ] ============================================================================

  public void saveSettings() {
    int numTotalFlash = Integer.parseInt(mTotalFlashLengthInput.getText().toString());
    int numSingleFlash = Integer.parseInt(mSingleFlashLengthInput.getText().toString());

    SharedPreferences prefs =
        getSharedPreferences("com.glasstowerstudios.stainedglass.prefs", MODE_PRIVATE);

    SharedPreferences.Editor editor = prefs.edit();
    editor.putInt("totalFlashLength", numTotalFlash);
    editor.putInt("singleFlashLength", numSingleFlash);
    editor.commit();
  }

  // === [ Private API ] ===========================================================================

  private void setupInputs() {
    refreshFromPreferences();

    mTotalFlashLengthInput = (EditText) findViewById(R.id.lengthFlashingInput);
    mSingleFlashLengthInput = (EditText) findViewById(R.id.singleFlashInput);

    mTotalFlashLengthInput.setText(Integer.toString(mTotalFlashLength));
    mSingleFlashLengthInput.setText(Integer.toString(mSingleFlashLength));
  }

  private void setupSaveButton() {
    mSaveSettingsButton = (Button) findViewById(R.id.settingSaveButton);

    mSaveSettingsButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        StainedGlassSettingsActivity.this.saveSettings();
        finish();
      }
    });
  }

  private void refreshFromPreferences() {
    SharedPreferences prefs =
        getSharedPreferences("com.glasstowerstudios.stainedglass.prefs", MODE_PRIVATE);
    mTotalFlashLength = prefs.getInt("totalFlashLength", 100);
    mSingleFlashLength = prefs.getInt("singleFlashLength", 100);
  }

  // === [ Private Member Variables ] ==============================================================

  private Button mSaveSettingsButton;
  private EditText mTotalFlashLengthInput;
  private EditText mSingleFlashLengthInput;

  private int mTotalFlashLength;
  private int mSingleFlashLength;
}
