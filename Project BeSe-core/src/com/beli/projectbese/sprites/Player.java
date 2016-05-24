package com.beli.projectbese.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.beli.projectbese.Constants;
import com.beli.projectbese.ProjectBeSe;
import com.beli.projectbese.controller.XBox360Pad;
import com.beli.projectbese.enums.State;
import com.beli.projectbese.screens.PlayScreen;

public class Player extends Sprite {
	public World world;
	public Body b2body;
	public State currentState;
	public State prevState;

	private TextureRegion playerStand;
	private Animation playerRun;
	private Animation playerJump;
	private float stateTimer;
	private boolean runningRight;

	public Player(World world, PlayScreen screen) {
		super(screen.getAtlas().findRegion("player"));
		this.world = world;
		currentState = State.STANDING;
		prevState = State.STANDING;
		stateTimer = 0;
		runningRight = true;

		Array<TextureRegion> frames = new Array<TextureRegion>();
		for (int i = 1; i < 4; i++) {
			frames.add(new TextureRegion(getTexture(), i * Constants.TEXTURE_SIZE, 10, Constants.TEXTURE_SIZE,
					Constants.TEXTURE_SIZE));
		}
		playerRun = new Animation(0.1f, frames);
		frames.clear();

		for (int i = 4; i < 6; i++) {
			frames.add(new TextureRegion(getTexture(), i * Constants.TEXTURE_SIZE, 10, Constants.TEXTURE_SIZE,
					Constants.TEXTURE_SIZE));
		}
		playerJump = new Animation(0.1f, frames);

		playerStand = new TextureRegion(getTexture(), 0, 10, Constants.TEXTURE_SIZE, Constants.TEXTURE_SIZE);

		definePlayer();
		setBounds(0, 0, 16 / Constants.PPM, 16 / Constants.PPM);
		setRegion(playerStand);
	}

	public void update(float dt) {
		setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
		setRegion(getFrame(dt));
	}

	private TextureRegion getFrame(float dt) {
		currentState = getState();

		TextureRegion region;
		switch (currentState) {
		case JUMPING:
			region = playerJump.getKeyFrame(stateTimer);
			break;
		case RUNNING:
			region = playerRun.getKeyFrame(stateTimer, true);
			break;
		case FALLING:
		case STANDING:
		default:
			region = playerStand;
			break;
		}

		if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
			region.flip(true, false);
			runningRight = false;
		} else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
			region.flip(true, false);
			runningRight = true;
		}

		stateTimer = currentState == prevState ? stateTimer + dt : 0;
		prevState = currentState;

		return region;
	}

	private State getState() {
		if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && prevState == State.JUMPING)) {
			return State.JUMPING;
		} else if (b2body.getLinearVelocity().y < 0) {
			return State.FALLING;
		} else if (b2body.getLinearVelocity().x != 0) {
			return State.RUNNING;
		} else {
			return State.STANDING;
		}
	}

	private void definePlayer() {
		BodyDef bdef = new BodyDef();
		bdef.position.set(32 / Constants.PPM, 32 / Constants.PPM);
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);

		FixtureDef fdef = new FixtureDef();
		fdef.filter.categoryBits = Constants.PLAYER_BIT;
		fdef.filter.maskBits = Constants.DEFAULT_BIT | Constants.ITEM_BIT | Constants.BRICK_BIT;

		CircleShape shape = defineBody();
		fdef.shape = shape;
		b2body.createFixture(fdef);

		EdgeShape head = defineHead();
		fdef.shape = head;
		fdef.isSensor = true;
		b2body.createFixture(fdef).setUserData("head");
	}

	private CircleShape defineBody() {
		CircleShape shape = new CircleShape();
		shape.setRadius(6f / Constants.PPM);

		return shape;
	}

	private EdgeShape defineHead() {
		EdgeShape head = new EdgeShape();
		head.set(new Vector2(-2 / Constants.PPM, 6 / Constants.PPM),
				new Vector2(+2 / Constants.PPM, 6 / Constants.PPM));

		return head;
	}

	public void jump() {
		if (currentState != State.JUMPING) {
			b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
		}
	}

	public void moveRight() {
		b2body.applyLinearImpulse(new Vector2(0.1f, 0), b2body.getWorldCenter(), true);
	}

	public void moveLeft() {
		b2body.applyLinearImpulse(new Vector2(-0.1f, 0), b2body.getWorldCenter(), true);
	}

	public void move(float direction) {
		b2body.applyLinearImpulse(new Vector2(direction * 0.1f, 0), b2body.getWorldCenter(), true);
	}
}
