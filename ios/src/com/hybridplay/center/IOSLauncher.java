package com.hybridplay.center;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

public class IOSLauncher extends IOSApplication.Delegate {
	class InnerClass {
		
    }
	
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        
        config.orientationLandscape = true;
        config.orientationPortrait = false;
        
        HybridPlayCenter mainApp = new HybridPlayCenter();
		mainApp.setPlatformResolver(new IOSResolver());
        
        return new IOSApplication(mainApp, config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}