package com.hybridplay.center.android;

import com.badlogic.gdx.Gdx;
import con.hybridplay.platforms.PlatformResolver;

public class AndroidResolver implements PlatformResolver {

	@Override
	public void rateGame() {
		System.out.println("Android");
		Gdx.net.openURI("https://www.hybridplay.com");
	}

}
