package com.mygdx.zombi;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.zombi.managers.ConfigurationManager;
import com.mygdx.zombi.screens.GameScreen;
import com.mygdx.zombi.screens.SplashScreen;

public class MyGame extends Game {
	public SpriteBatch batch;
	//Texture img;
	public BitmapFont font;
	public Skin skin;
    public ConfigurationManager configurationManager;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		font = new BitmapFont();
        font.getData().setScale(0.5f);
		
        configurationManager = new ConfigurationManager();
       // setScreen(new GameScreen(this));
        setScreen(new SplashScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		this.getScreen().dispose();
	}
	
	public Skin getSkin() {
		if(skin == null ) {
            skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        }
        return skin;
	}

	
}

/*
package com.mygdx.zombi;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.zombi.screens.GameScreen;

public class MyGame extends Game {
	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new GameScreen(this));
		
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		this.getScreen().dispose();
	}
	
	
}
*/