package com.beli.projectbese;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.beli.projectbese.screens.PlayScreen;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

public class ProjectBeSe extends Game {
	public SpriteBatch batch;
	public static AssetManager manager;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load(Constants.BACKGROUND_MUSIC, Music.class);
		manager.load(Constants.BLING_SOUND, Sound.class);
		manager.load(Constants.BOING_SOUND, Sound.class);
		manager.load(Constants.BUFF_SOUND, Sound.class);
		manager.load(Constants.PEW_SOUND, Sound.class);
		manager.finishLoading();
		
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		manager.dispose();
		batch.dispose();
	}
	
	
}
