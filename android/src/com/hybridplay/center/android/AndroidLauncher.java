package com.hybridplay.center.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.hybridplay.center.HybridPlayCenter;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		HybridPlayCenter mainApp = new HybridPlayCenter();
		mainApp.setPlatformResolver(new AndroidResolver());
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(mainApp, config);
	}
}
