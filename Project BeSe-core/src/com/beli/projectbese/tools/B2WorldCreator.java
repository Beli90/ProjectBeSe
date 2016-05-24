package com.beli.projectbese.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.beli.projectbese.Constants;
import com.beli.projectbese.sprites.Brick;
import com.beli.projectbese.sprites.CollisionObject;
import com.beli.projectbese.sprites.Item;

public class B2WorldCreator {
	private TiledMap map;
	private World world;

	public B2WorldCreator(World world, TiledMap map) {
		//create ground bodies/fixtures
		this.world = world;
		this.map = map;
		createBody(2);
		createBody(3);
		createBody(4);
		createBody(5);
	}
	
	private void createBody(int layer) {
		switch(layer) {
		case 2:
			BodyDef bdef = new BodyDef();
			PolygonShape shape = new PolygonShape();
			FixtureDef fdef = new FixtureDef();
			Body body;
			
			for(MapObject object : map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class)) {
				Rectangle rect = ((RectangleMapObject) object).getRectangle();
				
				bdef.type = BodyDef.BodyType.StaticBody;
				bdef.position.set((rect.getX() + rect.getWidth() / 2) / Constants.PPM, (rect.getY() + rect.getHeight() / 2) / Constants.PPM);
				
				body = world.createBody(bdef);
				
				shape.setAsBox((rect.getWidth() / 2) / Constants.PPM, (rect.getHeight() / 2) / Constants.PPM);
				fdef.shape = shape;
				body.createFixture(fdef);
			}
			break;
		case 3:
			for(MapObject object : map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class)) {
				Rectangle rect = ((RectangleMapObject) object).getRectangle();
				new CollisionObject(world, map, rect);
			}
			break;
		case 4:
			for(MapObject object : map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class)) {
				Rectangle rect = ((RectangleMapObject) object).getRectangle();
				new Item(world, map, rect);
			}
			break;
		case 5:
			for(MapObject object : map.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class)) {
				Rectangle rect = ((RectangleMapObject) object).getRectangle();
				new Brick(world, map, rect);
			}
			break;
		}
		
	}
}
