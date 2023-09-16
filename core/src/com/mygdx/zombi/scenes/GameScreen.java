package com.mygdx.zombi.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.zombi.MyGame;
import com.mygdx.zombi.managers.ResourceManager;
import com.mygdx.zombi.managers.SpriteManager;
import com.mygdx.zombi.scenes.Hud;


public class GameScreen implements Screen{
	final MyGame game;
	public ResourceManager resourceManager;
	public SpriteManager spriteManager;
	private Hud hud;
	
	public GameScreen(MyGame game){
		this.game=game;
		
		//cojo los recursos de el manager
		this.resourceManager=new ResourceManager();
		ResourceManager.loadAllResources();
		//cargo todos los recurosos
		while(!ResourceManager.update()){}
		
		//genero el spriteManager
		
		hud = new Hud(game.batch);
		spriteManager=new SpriteManager(game,hud);
		
		
	}
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		spriteManager.update(delta);
		Gdx.gl.glClearColor(0.7f, 0.6f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//Gdx.gl.glClearColor(0, 1, 1, 1);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//pinto en pantalla
		spriteManager.draw();
		//handleKeyboard();
		hud.stage.draw();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

}
