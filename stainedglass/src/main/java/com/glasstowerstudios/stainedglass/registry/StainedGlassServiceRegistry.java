package com.glasstowerstudios.stainedglass.registry;

import android.content.Context;

public class StainedGlassServiceRegistry {

  public static StainedGlassServiceRegistry getInstance() {
    return StainedGlassServiceRegistryHolder.sInstance;
  }

  public void init(Context aContext) {
    mContext = aContext.getApplicationContext();
  }

  public Context getApplicationContext() {
    if (mContext == null) {
      throw new UnsupportedOperationException("Context has not yet been initialized. Call init() first.");
    }

    return mContext;
  }

  private static final class StainedGlassServiceRegistryHolder {
    public static StainedGlassServiceRegistry sInstance = new StainedGlassServiceRegistry();
  }

  private StainedGlassServiceRegistry() {

  }

  private Context mContext;
}
