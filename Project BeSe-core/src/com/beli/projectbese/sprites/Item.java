package com.beli.projectbese.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.beli.projectbese.Constants;
import com.beli.projectbese.ProjectBeSe;
import com.beli.projectbese.scenes.Hud;

public class Item extends InteractiveTileObject {
	private static TiledMapTileSet tileSet;
	private final String TILESET_NAME = "tileset_gutter";
	private final int BLANK_ITEM = 28;
	
	public Item(World world, TiledMap map, Rectangle bounds) {
		super(world, map, bounds);
		tileSet = map.getTileSets().getTileSet(TILESET_NAME);
		fixture.setUserData(this);
		setCategoryFilter(Constants.ITEM_BIT);
	}

	@Override
	public void onHeadHit() {
		Gdx.app.log("Item", "Collision");		
		if(getCell().getTile().getId() == BLANK_ITEM) {
			ProjectBeSe.manager.get(Constants.BOING_SOUND, Sound.class).play();
		} else {
			ProjectBeSe.manager.get(Constants.BLING_SOUND, Sound.class).play();
		}
		getCell().setTile(tileSet.getTile(BLANK_ITEM));
		Hud.addScore(1);
	}
}
