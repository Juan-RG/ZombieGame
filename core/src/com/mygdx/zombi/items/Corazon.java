package com.mygdx.zombi.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.zombi.characters.Character;
import com.mygdx.zombi.characters.EnemyWorm.State;
import com.mygdx.zombi.managers.ResourceManager;
import com.mygdx.zombi.managers.SpriteManager;

public class Corazon extends Character{
	public State state;
	public boolean activo;
	
	
	int vidaCorazon=40;
	int banderaSumarvida=0;
	private Animation<TextureRegion> corazon;
	
	ShapeRenderer shapeRenderer;
	
	
	public enum State{
		IDLE
	}

	public Corazon(SpriteManager spriteManager) {
		super(spriteManager);
		
		TextureAtlas atlas=ResourceManager.assets.get("items/corazon.pack", TextureAtlas.class);
		shapeRenderer = new ShapeRenderer();
		corazon=new Animation<TextureRegion>(0.3f,atlas.findRegions("corazon"));
		
		currentFrame = atlas.findRegion("corazon",1);
		position=new Vector2();
		//position.set(300, 500);
		WIDTH=30;
		HEIGHT=30;
		activo=true;
		this.state=State.IDLE;
		this.rect= new Rectangle();
		this.rect.setSize(currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
	
	}
	
	
	
	public void render(Batch spriteBatch){
		//pintamos el stateTime el timpo en el que se pintan las imagenes
		stateTime+=Gdx.graphics.getDeltaTime();
		
		//tipos de estados
		switch(state){
			case IDLE: 
				currentFrame=corazon.getKeyFrame(stateTime,true);
				break;

		}
		//pinto el enemigo y cierro el spriteBach
		spriteBatch.draw(currentFrame, position.x, position.y, this.WIDTH, this.HEIGHT);
		spriteBatch.end();
		
		if(spriteManager.prefs.getBoolean("colision")==true) {
		//habro el pintador y pinto los dos recuadros de colision
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setProjectionMatrix(spriteBatch.getProjectionMatrix());
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.rect(this.rect.x, this.rect.y,this.rect.width,this.rect.height);
		//if(this.cajaAtaque.isActive()){
		shapeRenderer.setColor(Color.RED);
	
		//cierro el pintador y abro el spritebach
		shapeRenderer.end();
		}
		
		spriteBatch.begin();
		
	}

	@Override
	public void update(float dt) {
		
		this.checkCollisions(spriteManager);
		
	}

	@Override
	public void checkCollisions(SpriteManager spriteManager) {
		
		if(this.rect.overlaps(spriteManager.player.rect)) {
			System.out.println("controlador de que sume solo 1 vez "+banderaSumarvida);
			if(banderaSumarvida==0) {
			spriteManager.player.aumentarVida(vidaCorazon);
			activo=false;
			}
			banderaSumarvida++;
		}else {
			banderaSumarvida=0;
		}
		
	}
	
	public void posicion(float x, float y) {
		this.position.set(x, y);
		this.rect.setPosition(x, y);
	}
	


}
