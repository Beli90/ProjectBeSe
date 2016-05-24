package com.beli.projectbese.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.beli.projectbese.Constants;

public class Hud implements Disposable {
	public Stage stage;
	private Viewport viewport;

	private Integer worldTimer;
	private float timeCount;
	private static Integer score;

	Label countDownLabel;
	static Label scoreLabel;
	Label timeLabel;
	Label levelLabel;
	Label worldLabel;
	Label playerLabel;

	public Hud(SpriteBatch sb) {
		worldTimer = 300;
		timeCount = 0;
		score = 0;

		viewport = new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport, sb);
		
		Table table = new Table();
		table.top();
		table.setFillParent(true);
		
		countDownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		playerLabel = new Label("PLAYER", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		
		table.add(playerLabel).expandX().padTop(10);
		table.add(worldLabel).expandX().padTop(10);
		table.add(timeLabel).expandX().padTop(10);
		table.row();
		table.add(scoreLabel).expandX();
		table.add(levelLabel).expandX();
		table.add(countDownLabel).expandX();
		
		stage.addActor(table);
	}
	
	public static void addScore(int value) {
		score += value;
		scoreLabel.setText(String.format("%06d", score));
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}