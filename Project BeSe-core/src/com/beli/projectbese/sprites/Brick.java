package com.beli.projectbese.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.beli.projectbese.Constants;
import com.beli.projectbese.ProjectBeSe;
import com.beli.projectbese.scenes.Hud;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

public class Brick extends InteractiveTileObject {

	public Brick(World world, TiledMap map, Rectangle bounds) {
		super(world, map, bounds);
		fixture.setUserData(this);
		setCategoryFilter(Constants.BRICK_BIT);
	}

	@Override
	public void onHeadHit() {
		Gdx.app.log("Brick", "Collision");
		ProjectBeSe.manager.get(Constants.BUFF_SOUND, Sound.class).play();
		setCategoryFilter(Constants.DESTROYED_BIT);
		getCell().setTile(null);
		Hud.addScore(1);
	}
}
