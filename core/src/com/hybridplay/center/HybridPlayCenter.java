package com.hybridplay.center;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import con.hybridplay.platforms.PlatformResolver;

public class HybridPlayCenter extends InputAdapter implements ApplicationListener {
	
	SpriteBatch batch;
	Texture img;
	private Logger logger;
	private boolean renderInterrupted = true;
	
	protected static PlatformResolver m_platformResolver = null;

	public static PlatformResolver getPlatformResolver() {
		return m_platformResolver;
	}
	
	public void setPlatformResolver(PlatformResolver platformResolver) {
		m_platformResolver = platformResolver;
	}
	
	@Override
	public void create () {
		logger = new Logger("Application lifecycle", Logger.INFO);
		
		logger.info("create");
		
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		if (renderInterrupted) {
			logger.info("render");
			renderInterrupted = false;
		}
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void resize(int width, int height) {
		logger.info("resize");
	    renderInterrupted = true;
	}

	@Override
	public void pause() {
		logger.info("pause");
	    renderInterrupted = true;
	}

	@Override
	public void resume() {
		logger.info("resume");
	    renderInterrupted = true;
	}

	@Override
	public void dispose() {
		logger.info("dispose");
	}
}
