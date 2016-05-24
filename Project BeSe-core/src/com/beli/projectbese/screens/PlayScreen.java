package com.beli.projectbese.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.beli.projectbese.Constants;
import com.beli.projectbese.ProjectBeSe;
import com.beli.projectbese.controller.XBox360Pad;
import com.beli.projectbese.enums.State;
import com.beli.projectbese.scenes.Hud;
import com.beli.projectbese.sprites.Player;
import com.beli.projectbese.tools.B2WorldCreator;
import com.beli.projectbese.tools.WorldContactListener;

public class PlayScreen implements Screen {
	private ProjectBeSe game;
	private OrthographicCamera gameCam;
	private TextureAtlas atlas;
	private Controller controller;

	/**
	 * Is a FitViewPort to show the graphics the same on different screen-sizes
	 */
	private Viewport gamePort;

	/**
	 * Contains the infos displayed fixed on screen like time, player, level
	 * etc.
	 */
	private Hud hud;

	// Tiled map variables
	private TmxMapLoader mapLoader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;

	// Box2d variables
	private World world;
	private Box2DDebugRenderer b2dr;

	private Player player;

	public PlayScreen(ProjectBeSe game) {
		atlas = new TextureAtlas("Player_and_Enemies.pack");

		this.game = game;

		// create cam used to follow mario through cam world
		gameCam = new OrthographicCamera();

		// create a FitViewport to maintain virtual aspect ratio despite
		gamePort = new FitViewport(Constants.V_WIDTH / Constants.PPM, Constants.V_HEIGHT / Constants.PPM, gameCam);

		// create game HUD for scores/timers/level-info
		hud = new Hud(game.batch);

		// Load map and setup map renderer
		mapLoader = new TmxMapLoader();
		map = mapLoader.load("level1.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.PPM);

		// initially set gamecam to be centered correctly at the start of the
		// game
		gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

		world = new World(new Vector2(0, -10), true);
		// allows for debug lines of the box2d world
		b2dr = new Box2DDebugRenderer();

		new B2WorldCreator(world, map);

		player = new Player(world, this);

		playMusic();

		world.setContactListener(new WorldContactListener());

//		if (Controllers.getControllers().size > 0) {
//			controller = Controllers.getControllers().first();
//			controller.setAccelerometerSensitivity(0f);
//		}
	}

	private void playMusic() {
		Music music = ProjectBeSe.manager.get(Constants.BACKGROUND_MUSIC, Music.class);
		music.setLooping(true);
		music.setVolume(0.2f);
		music.play();
	}

	public TextureAtlas getAtlas() {
		return atlas;
	}

	public void update(float deltaTime) {
		// handle user input first
		handleInput(deltaTime);

		// takes 1 step in the physics simulation (60 times per second)
		world.step(1 / 60f, 6, 2);

		player.update(deltaTime);

		gameCam.position.x = player.b2body.getPosition().x;

		// update gamecam with correct coordinates after changes
		gameCam.update();

		// tell renderer to draw only what camera can see in game world
		renderer.setView(gameCam);
	}

	private void handleInput(float deltaTime) {
		// Keyboard Controls
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			player.jump();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2) {
			player.moveRight();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2) {
			player.moveLeft();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			ProjectBeSe.manager.get(Constants.PEW_SOUND, Sound.class).play();
		}

		// Xbox-360 Controls
//		if (Controllers.getControllers().size > 0) {
//			if (controller.getButton(XBox360Pad.BUTTON_A) && player.b2body.getLinearVelocity().y <= 2) {
//				player.jump();
//			}
//			if (((controller.getAxis(XBox360Pad.AXIS_LEFT_X) > 0.2f && player.b2body.getLinearVelocity().x <= 2)
//					|| controller.getAxis(XBox360Pad.AXIS_LEFT_X) < -0.2f)
//					&& player.b2body.getLinearVelocity().x >= -2) {
//				player.move(controller.getAxis(XBox360Pad.AXIS_LEFT_X));
//			}
//			if (controller.getAxis(XBox360Pad.AXIS_LEFT_Y) > 0.2f
//					|| controller.getAxis(XBox360Pad.AXIS_LEFT_Y) < -0.2f) {
//				// TODO
//			}
//		}
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		// seperate update logic from render
		update(delta);

		// Clear the game screen with Black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// render game map
		renderer.render();

		// render Box2DDebugLines
		// b2dr.render(world, gameCam.combined);

		game.batch.setProjectionMatrix(gameCam.combined);
		game.batch.begin();
		player.draw(game.batch);
		game.batch.end();

		// Set batch to now draw what the Hud camera sees
		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
		world.dispose();
		b2dr.dispose();
		hud.dispose();
	}

}
