package com.beli.projectbese.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

public class CollisionObject extends InteractiveTileObject{
	
	public CollisionObject(World world, TiledMap map, Rectangle bounds) {
		super(world, map, bounds);
		fixture.setUserData(this);
	}

	@Override
	public void onHeadHit() {
		Gdx.app.log("Object", "Collision");
	}
}
