package com.hybridplay.center;

import com.badlogic.gdx.Gdx;
import con.hybridplay.platforms.PlatformResolver;

public class IOSResolver implements PlatformResolver{

	@Override
	public void rateGame() {
		System.out.println("iOS");
		Gdx.net.openURI("http://www.hybridplay.com");
	}

}
